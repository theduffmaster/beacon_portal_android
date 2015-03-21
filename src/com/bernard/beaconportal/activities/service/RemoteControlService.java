package com.bernard.beaconportal.activities.service;

import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_ACCOUNT_UUID;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_ALL_ACCOUNTS;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_BACKGROUND_OPERATIONS;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_NOTIFICATION_ENABLED;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_POLL_CLASSES;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_POLL_FREQUENCY;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_PUSH_CLASSES;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_RING_ENABLED;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_THEME;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_VIBRATE_ENABLED;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.Account.FolderMode;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.MAIL.BACKGROUND_OPS;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl;

public class RemoteControlService extends CoreService {
	private final static String RESCHEDULE_ACTION = "com.bernard.beaconportal.activities.service.RemoteControlService.RESCHEDULE_ACTION";
	private final static String PUSH_RESTART_ACTION = "com.bernard.beaconportal.activities.service.RemoteControlService.PUSH_RESTART_ACTION";

	private final static String SET_ACTION = "com.bernard.beaconportal.activities.service.RemoteControlService.SET_ACTION";

	public static void set(Context context, Intent i, Integer wakeLockId) {
		// Intent i = new Intent();
		i.setClass(context, RemoteControlService.class);
		i.setAction(RemoteControlService.SET_ACTION);
		addWakeLockId(context, i, wakeLockId, true);
		context.startService(i);
	}

	public static final int REMOTE_CONTROL_SERVICE_WAKE_LOCK_TIMEOUT = 20000;

	@Override
	public int startService(final Intent intent, final int startId) {
		if (MAIL.DEBUG)
			Log.i(MAIL.LOG_TAG, "RemoteControlService started with startId = "
					+ startId);
		final Preferences preferences = Preferences.getPreferences(this);

		if (RESCHEDULE_ACTION.equals(intent.getAction())) {
			if (MAIL.DEBUG)
				Log.i(MAIL.LOG_TAG,
						"RemoteControlService requesting MailService poll reschedule");
			MailService.actionReschedulePoll(this, null);
		}
		if (PUSH_RESTART_ACTION.equals(intent.getAction())) {
			if (MAIL.DEBUG)
				Log.i(MAIL.LOG_TAG,
						"RemoteControlService requesting MailService push restart");
			MailService.actionRestartPushers(this, null);
		} else if (RemoteControlService.SET_ACTION.equals(intent.getAction())) {
			if (MAIL.DEBUG)
				Log.i(MAIL.LOG_TAG,
						"RemoteControlService got request to change settings");
			execute(getApplication(),
					new Runnable() {
						@Override
						public void run() {
							try {
								boolean needsReschedule = false;
								boolean needsPushRestart = false;
								String uuid = intent
										.getStringExtra(MAIL_ACCOUNT_UUID);
								boolean allAccounts = intent.getBooleanExtra(
										MAIL_ALL_ACCOUNTS, false);
								if (MAIL.DEBUG) {
									if (allAccounts) {
										Log.i(MAIL.LOG_TAG,
												"RemoteControlService changing settings for all accounts");
									} else {
										Log.i(MAIL.LOG_TAG,
												"RemoteControlService changing settings for account with UUID "
														+ uuid);
									}
								}
								Account[] accounts = preferences.getAccounts();
								for (Account account : accounts) {
									// warning: account may not be isAvailable()
									if (allAccounts
											|| account.getUuid().equals(uuid)) {

										if (MAIL.DEBUG)
											Log.i(MAIL.LOG_TAG,
													"RemoteControlService changing settings for account "
															+ account
																	.getDescription());

										String notificationEnabled = intent
												.getStringExtra(MAIL_NOTIFICATION_ENABLED);
										String ringEnabled = intent
												.getStringExtra(MAIL_RING_ENABLED);
										String vibrateEnabled = intent
												.getStringExtra(MAIL_VIBRATE_ENABLED);
										String pushClasses = intent
												.getStringExtra(MAIL_PUSH_CLASSES);
										String pollClasses = intent
												.getStringExtra(MAIL_POLL_CLASSES);
										String pollFrequency = intent
												.getStringExtra(MAIL_POLL_FREQUENCY);

										if (notificationEnabled != null) {
											account.setNotifyNewMail(Boolean
													.parseBoolean(notificationEnabled));
										}
										if (ringEnabled != null) {
											account.getNotificationSetting()
													.setRing(
															Boolean.parseBoolean(ringEnabled));
										}
										if (vibrateEnabled != null) {
											account.getNotificationSetting()
													.setVibrate(
															Boolean.parseBoolean(vibrateEnabled));
										}
										if (pushClasses != null) {
											needsPushRestart |= account
													.setFolderPushMode(FolderMode
															.valueOf(pushClasses));
										}
										if (pollClasses != null) {
											needsReschedule |= account
													.setFolderSyncMode(FolderMode
															.valueOf(pollClasses));
										}
										if (pollFrequency != null) {
											String[] allowedFrequencies = getResources()
													.getStringArray(
															R.array.account_settings_check_frequency_values);
											for (String allowedFrequency : allowedFrequencies) {
												if (allowedFrequency
														.equals(pollFrequency)) {
													Integer newInterval = Integer
															.parseInt(allowedFrequency);
													needsReschedule |= account
															.setAutomaticCheckIntervalMinutes(newInterval);
												}
											}
										}
										account.save(Preferences
												.getPreferences(RemoteControlService.this));
									}
								}
								if (MAIL.DEBUG)
									Log.i(MAIL.LOG_TAG,
											"RemoteControlService changing global settings");

								String backgroundOps = intent
										.getStringExtra(MAIL_BACKGROUND_OPERATIONS);
								if (MAILRemoteControl.MAIL_BACKGROUND_OPERATIONS_ALWAYS
										.equals(backgroundOps)
										|| MAILRemoteControl.MAIL_BACKGROUND_OPERATIONS_NEVER
												.equals(backgroundOps)
										|| MAILRemoteControl.MAIL_BACKGROUND_OPERATIONS_WHEN_CHECKED
												.equals(backgroundOps)
										|| MAILRemoteControl.MAIL_BACKGROUND_OPERATIONS_WHEN_CHECKED_AUTO_SYNC
												.equals(backgroundOps)) {
									BACKGROUND_OPS newBackgroundOps = BACKGROUND_OPS
											.valueOf(backgroundOps);
									boolean needsReset = MAIL
											.setBackgroundOps(newBackgroundOps);
									needsPushRestart |= needsReset;
									needsReschedule |= needsReset;
								}

								String theme = intent
										.getStringExtra(MAIL_THEME);
								if (theme != null) {
									MAIL.setMAILTheme(MAILRemoteControl.MAIL_THEME_DARK
											.equals(theme) ? MAIL.Theme.DARK
											: MAIL.Theme.LIGHT);
								}

								SharedPreferences sPrefs = preferences
										.getPreferences();

								Editor editor = sPrefs.edit();
								MAIL.save(editor);
								editor.commit();

								if (needsReschedule) {
									Intent i = new Intent();
									i.setClassName(getApplication()
											.getPackageName(),
											"com.bernard.beaconportal.activities.service.RemoteControlService");
									i.setAction(RESCHEDULE_ACTION);
									long nextTime = System.currentTimeMillis() + 10000;
									BootReceiver.scheduleIntent(
											RemoteControlService.this,
											nextTime, i);
								}
								if (needsPushRestart) {
									Intent i = new Intent();
									i.setClassName(getApplication()
											.getPackageName(),
											"com.bernard.beaconportal.activities.service.RemoteControlService");
									i.setAction(PUSH_RESTART_ACTION);
									long nextTime = System.currentTimeMillis() + 10000;
									BootReceiver.scheduleIntent(
											RemoteControlService.this,
											nextTime, i);
								}
							} catch (Exception e) {
								Log.e(MAIL.LOG_TAG,
										"Could not handle MAIL_SET", e);
								Toast toast = Toast.makeText(
										RemoteControlService.this,
										e.getMessage(), Toast.LENGTH_LONG);
								toast.show();
							}
						}
					},
					RemoteControlService.REMOTE_CONTROL_SERVICE_WAKE_LOCK_TIMEOUT,
					startId);
		}

		return START_NOT_STICKY;
	}

}
