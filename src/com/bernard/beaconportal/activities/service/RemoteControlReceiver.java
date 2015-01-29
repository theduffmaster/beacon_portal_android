package com.bernard.beaconportal.activities.service;

import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_ACCOUNT_DESCRIPTIONS;
import static com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl.MAIL_ACCOUNT_UUIDS;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.remotecontrol.MAILRemoteControl;

public class RemoteControlReceiver extends CoreReceiver {
	@Override
	public Integer receive(Context context, Intent intent, Integer tmpWakeLockId) {
		if (MAIL.DEBUG)
			Log.i(MAIL.LOG_TAG, "RemoteControlReceiver.onReceive" + intent);

		if (MAILRemoteControl.MAIL_SET.equals(intent.getAction())) {
			RemoteControlService.set(context, intent, tmpWakeLockId);
			tmpWakeLockId = null;
		} else if (MAILRemoteControl.MAIL_REQUEST_ACCOUNTS.equals(intent
				.getAction())) {
			try {
				Preferences preferences = Preferences.getPreferences(context);
				Account[] accounts = preferences.getAccounts();
				String[] uuids = new String[accounts.length];
				String[] descriptions = new String[accounts.length];
				for (int i = 0; i < accounts.length; i++) {
					// warning: account may not be isAvailable()
					Account account = accounts[i];

					uuids[i] = account.getUuid();
					descriptions[i] = account.getDescription();
				}
				Bundle bundle = getResultExtras(true);
				bundle.putStringArray(MAIL_ACCOUNT_UUIDS, uuids);
				bundle.putStringArray(MAIL_ACCOUNT_DESCRIPTIONS, descriptions);
			} catch (Exception e) {
				Log.e(MAIL.LOG_TAG, "Could not handle MAIL_RESPONSE_INTENT", e);
			}

		}

		return tmpWakeLockId;
	}

}
