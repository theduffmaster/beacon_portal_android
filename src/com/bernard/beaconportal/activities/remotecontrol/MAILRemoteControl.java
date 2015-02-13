package com.bernard.beaconportal.activities.remotecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

/**
 * Utillity definitions for Android applications to control the behavior of Mail
 * Mail. All such applications must declare the following permission:
 * <uses-permission android:name=
 * "com.bernard.beaconportal.activities.permission.REMOTE_CONTROL"/> in their
 * AndroidManifest.xml In addition, all applications sending remote control
 * messages to Mail Mail must
 * 
 * An application that wishes to act on a particular Account in Mail needs to
 * fetch the list of configured Accounts by broadcasting an {@link Intent} using
 * MAIL_REQUEST_ACCOUNTS as the Action. The broadcast must be made using the
 * {@link ContextWrapper} sendOrderedBroadcast(Intent intent, String
 * receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int
 * initialCode, String initialData, Bundle initialExtras).sendOrderedBroadcast}
 * method in order to receive the list of Account UUIDs and descriptions that
 * Mail will provide.
 * 
 * @author Daniel I. Applebaum
 * 
 */
public class MAILRemoteControl {
	/**
	 * Permission that every application sending a broadcast to Mail for Remote
	 * Control purposes should send on every broadcast. Prevent other
	 * applications from intercepting the broadcasts.
	 */
	public final static String MAIL_REMOTE_CONTROL_PERMISSION = "com.bernard.beaconportal.activities.permission.REMOTE_CONTROL";
	/**
	 * {@link Intent} Action to be sent to Mail using
	 * {@link ContextWrapper.sendOrderedBroadcast} in order to fetch the list of
	 * configured Accounts. The responseData will contain two String[] with keys
	 * MAIL_ACCOUNT_UUIDS and MAIL_ACCOUNT_DESCRIPTIONS
	 */
	public final static String MAIL_REQUEST_ACCOUNTS = "com.bernard.beaconportal.activities.MAILRemoteControl.requestAccounts";
	public final static String MAIL_ACCOUNT_UUIDS = "com.bernard.beaconportal.activities.MAILRemoteControl.accountUuids";
	public final static String MAIL_ACCOUNT_DESCRIPTIONS = "com.bernard.beaconportal.activities.MAILRemoteControl.accountDescriptions";

	/**
	 * The {@link {@link Intent} Action to set in order to cause Mail to check
	 * mail. (Not yet implemented)
	 */
	// public final static String MAIL_CHECK_MAIL =
	// "com.bernard.beaconportal.activities.MAILRemoteControl.checkMail";

	/**
	 * The {@link {@link Intent} Action to set when remotely changing Mail Mail
	 * settings
	 */
	public final static String MAIL_SET = "com.bernard.beaconportal.activities.MAILRemoteControl.set";
	/**
	 * The key of the {@link Intent} Extra to set to hold the UUID of a single
	 * Account's settings to change. Used only if MAIL_ALL_ACCOUNTS is absent or
	 * false.
	 */
	public final static String MAIL_ACCOUNT_UUID = "com.bernard.beaconportal.activities.MAILRemoteControl.accountUuid";
	/**
	 * The key of the {@link Intent} Extra to set to control if the settings
	 * will apply to all Accounts, or to the one specified with MAIL_ACCOUNT_UUID
	 */
	public final static String MAIL_ALL_ACCOUNTS = "com.bernard.beaconportal.activities.MAILRemoteControl.allAccounts";

	public final static String MAIL_ENABLED = "true";
	public final static String MAIL_DISABLED = "false";

	/*
	 * Key for the {@link Intent} Extra for controlling whether notifications
	 * will be generated for new unread mail. Acceptable values are MAIL_ENABLED
	 * and MAIL_DISABLED
	 */
	public final static String MAIL_NOTIFICATION_ENABLED = "com.bernard.beaconportal.activities.MAILRemoteControl.notificationEnabled";
	/*
	 * Key for the {@link Intent} Extra for controlling whether Mail will sound
	 * the ringtone for new unread mail. Acceptable values are MAIL_ENABLED and
	 * MAIL_DISABLED
	 */
	public final static String MAIL_RING_ENABLED = "com.bernard.beaconportal.activities.MAILRemoteControl.ringEnabled";
	/*
	 * Key for the {@link Intent} Extra for controlling whether Mail will
	 * activate the vibrator for new unread mail. Acceptable values are
	 * MAIL_ENABLED and MAIL_DISABLED
	 */
	public final static String MAIL_VIBRATE_ENABLED = "com.bernard.beaconportal.activities.MAILRemoteControl.vibrateEnabled";

	public final static String MAIL_FOLDERS_NONE = "NONE";
	public final static String MAIL_FOLDERS_ALL = "ALL";
	public final static String MAIL_FOLDERS_FIRST_CLASS = "FIRST_CLASS";
	public final static String MAIL_FOLDERS_FIRST_AND_SECOND_CLASS = "FIRST_AND_SECOND_CLASS";
	public final static String MAIL_FOLDERS_NOT_SECOND_CLASS = "NOT_SECOND_CLASS";
	/**
	 * Key for the {@link Intent} Extra to set for controlling which folders to
	 * be synchronized with Push. Acceptable values are MAIL_FOLDERS_ALL,
	 * MAIL_FOLDERS_FIRST_CLASS, MAIL_FOLDERS_FIRST_AND_SECOND_CLASS,
	 * MAIL_FOLDERS_NOT_SECOND_CLASS, MAIL_FOLDERS_NONE
	 */
	public final static String MAIL_PUSH_CLASSES = "com.bernard.beaconportal.activities.MAILRemoteControl.pushClasses";
	/**
	 * Key for the {@link Intent} Extra to set for controlling which folders to
	 * be synchronized with Poll. Acceptable values are MAIL_FOLDERS_ALL,
	 * MAIL_FOLDERS_FIRST_CLASS, MAIL_FOLDERS_FIRST_AND_SECOND_CLASS,
	 * MAIL_FOLDERS_NOT_SECOND_CLASS, MAIL_FOLDERS_NONE
	 */
	public final static String MAIL_POLL_CLASSES = "com.bernard.beaconportal.activities.MAILRemoteControl.pollClasses";

	public final static String[] MAIL_POLL_FREQUENCIES = { "-1", "1", "5", "10",
			"15", "30", "60", "120", "180", "360", "720", "1440" };
	/**
	 * Key for the {@link Intent} Extra to set with the desired poll frequency.
	 * The value is a String representing a number of minutes. Acceptable values
	 * are available in MAIL_POLL_FREQUENCIES
	 */
	public final static String MAIL_POLL_FREQUENCY = "com.bernard.beaconportal.activities.MAILRemoteControl.pollFrequency";

	/**
	 * Key for the {@link Intent} Extra to set for controlling Mail's global
	 * "Background sync" setting. Acceptable values are
	 * MAIL_BACKGROUND_OPERATIONS_ALWAYS, MAIL_BACKGROUND_OPERATIONS_NEVER
	 * MAIL_BACKGROUND_OPERATIONS_WHEN_CHECKED
	 */
	public final static String MAIL_BACKGROUND_OPERATIONS = "com.bernard.beaconportal.activities.MAILRemoteControl.backgroundOperations";
	public final static String MAIL_BACKGROUND_OPERATIONS_WHEN_CHECKED = "WHEN_CHECKED";
	public final static String MAIL_BACKGROUND_OPERATIONS_ALWAYS = "ALWAYS";
	public final static String MAIL_BACKGROUND_OPERATIONS_NEVER = "NEVER";
	public final static String MAIL_BACKGROUND_OPERATIONS_WHEN_CHECKED_AUTO_SYNC = "WHEN_CHECKED_AUTO_SYNC";

	/**
	 * Key for the {@link Intent} Extra to set for controlling which display
	 * theme Mail will use. Acceptable values are MAIL_THEME_LIGHT, MAIL_THEME_DARK
	 */
	public final static String MAIL_THEME = "com.bernard.beaconportal.activities.MAILRemoteControl.theme";
	public final static String MAIL_THEME_LIGHT = "LIGHT";
	public final static String MAIL_THEME_DARK = "DARK";

	protected static String LOG_TAG = "MAILRemoteControl";

	public static void set(Context context, Intent broadcastIntent) {
		broadcastIntent.setAction(MAILRemoteControl.MAIL_SET);
		context.sendBroadcast(broadcastIntent,
				MAILRemoteControl.MAIL_REMOTE_CONTROL_PERMISSION);
	}

	public static void fetchAccounts(Context context, MAILAccountReceptor receptor) {
		Intent accountFetchIntent = new Intent();
		accountFetchIntent.setAction(MAILRemoteControl.MAIL_REQUEST_ACCOUNTS);
		AccountReceiver receiver = new AccountReceiver(receptor);
		context.sendOrderedBroadcast(accountFetchIntent,
				MAILRemoteControl.MAIL_REMOTE_CONTROL_PERMISSION, receiver, null,
				Activity.RESULT_OK, null, null);
	}

}
