package com.bernard.beaconportal.activities.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.helper.power.TracingPowerManager.TracingWakeLock;
import com.bernard.beaconportal.activities.mail.Folder;
import com.bernard.beaconportal.activities.mail.Message;
import com.bernard.beaconportal.activities.mail.PushReceiver;
import com.bernard.beaconportal.activities.mail.store.LocalStore;
import com.bernard.beaconportal.activities.mail.store.LocalStore.LocalFolder;
import com.bernard.beaconportal.activities.service.SleepService;

public class MessagingControllerPushReceiver implements PushReceiver {
	final Account account;
	final MessagingController controller;
	final Application mApplication;

	public MessagingControllerPushReceiver(Application nApplication,
			Account nAccount, MessagingController nController) {
		account = nAccount;
		controller = nController;
		mApplication = nApplication;
	}

	@Override
	public void messagesFlagsChanged(Folder folder, List<Message> messages) {
		controller.messagesArrived(account, folder, messages, true);
	}

	@Override
	public void messagesArrived(Folder folder, List<Message> messages) {
		controller.messagesArrived(account, folder, messages, false);
	}

	@Override
	public void messagesRemoved(Folder folder, List<Message> messages) {
		controller.messagesArrived(account, folder, messages, true);
	}

	@Override
	public void syncFolder(Folder folder) {
		if (MAIL.DEBUG)
			Log.v(MAIL.LOG_TAG, "syncFolder(" + folder.getName() + ")");
		final CountDownLatch latch = new CountDownLatch(1);
		controller.synchronizeMailbox(account, folder.getName(),
				new MessagingListener() {
					@Override
					public void synchronizeMailboxFinished(Account account,
							String folder, int totalMessagesInMailbox,
							int numNewMessages) {
						latch.countDown();
					}

					@Override
					public void synchronizeMailboxFailed(Account account,
							String folder, String message) {
						latch.countDown();
					}
				}, folder);

		if (MAIL.DEBUG)
			Log.v(MAIL.LOG_TAG, "syncFolder(" + folder.getName()
					+ ") about to await latch release");
		try {
			latch.await();
			if (MAIL.DEBUG)
				Log.v(MAIL.LOG_TAG, "syncFolder(" + folder.getName()
						+ ") got latch release");
		} catch (Exception e) {
			Log.e(MAIL.LOG_TAG, "Interrupted while awaiting latch release", e);
		}
	}

	@Override
	public void sleep(TracingWakeLock wakeLock, long millis) {
		SleepService.sleep(mApplication, millis, wakeLock,
				MAIL.PUSH_WAKE_LOCK_TIMEOUT);
	}

	@Override
	public void pushError(String errorMessage, Exception e) {
		String errMess = errorMessage;

		controller.notifyUserIfCertificateProblem(mApplication, e, account,
				true);
		if (errMess == null && e != null) {
			errMess = e.getMessage();
		}
		controller.addErrorMessage(account, errMess, e);
	}

	@Override
	public String getPushState(String folderName) {
		LocalFolder localFolder = null;
		try {
			LocalStore localStore = account.getLocalStore();
			localFolder = localStore.getFolder(folderName);
			localFolder.open(Folder.OPEN_MODE_RW);
			return localFolder.getPushState();
		} catch (Exception e) {
			Log.e(MAIL.LOG_TAG, "Unable to get push state from account "
					+ account.getDescription() + ", folder " + folderName, e);
			return null;
		} finally {
			if (localFolder != null) {
				localFolder.close();
			}
		}
	}

	@Override
	public void setPushActive(String folderName, boolean enabled) {
		for (MessagingListener l : controller.getListeners()) {
			l.setPushActive(account, folderName, enabled);
		}
	}

	@Override
	public Context getContext() {
		return mApplication;
	}

}
