package com.bernard.beaconportal.activities.remotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

class AccountReceiver extends BroadcastReceiver {
	MAILAccountReceptor receptor = null;

	protected AccountReceiver(MAILAccountReceptor nReceptor) {
		receptor = nReceptor;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (MAILRemoteControl.MAIL_REQUEST_ACCOUNTS.equals(intent.getAction())) {
			Bundle bundle = getResultExtras(false);
			if (bundle == null) {
				Log.w(MAILRemoteControl.LOG_TAG, "Response bundle is empty");
				return;
			}
			receptor.accounts(
					bundle.getStringArray(MAILRemoteControl.MAIL_ACCOUNT_UUIDS),
					bundle.getStringArray(MAILRemoteControl.MAIL_ACCOUNT_DESCRIPTIONS));
		}
	}

}
