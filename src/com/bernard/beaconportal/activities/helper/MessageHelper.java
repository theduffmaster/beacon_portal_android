package com.bernard.beaconportal.activities.helper;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.FolderInfoHolder;
import com.bernard.beaconportal.activities.activity.MessageInfoHolder;
import com.bernard.beaconportal.activities.mail.Address;
import com.bernard.beaconportal.activities.mail.Flag;
import com.bernard.beaconportal.activities.mail.Message;
import com.bernard.beaconportal.activities.mail.Message.RecipientType;
import com.bernard.beaconportal.activities.mail.MessagingException;

public class MessageHelper {

	private static MessageHelper sInstance;

	public synchronized static MessageHelper getInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new MessageHelper(context);
		}
		return sInstance;
	}

	private Context mContext;

	private MessageHelper(final Context context) {
		mContext = context;
	}

	public void populate(final MessageInfoHolder target, final Message message,
			final FolderInfoHolder folder, final Account account) {
		final Contacts contactHelper = MAIL.showContactName() ? Contacts
				.getInstance(mContext) : null;
		try {
			target.message = message;
			target.compareArrival = message.getInternalDate();
			target.compareDate = message.getSentDate();
			if (target.compareDate == null) {
				target.compareDate = message.getInternalDate();
			}

			target.folder = folder;

			target.read = message.isSet(Flag.SEEN);
			target.answered = message.isSet(Flag.ANSWERED);
			target.forwarded = message.isSet(Flag.FORWARDED);
			target.flagged = message.isSet(Flag.FLAGGED);

			Address[] addrs = message.getFrom();

			if (addrs.length > 0 && account.isAnIdentity(addrs[0])) {
				CharSequence to = Address.toFriendly(
						message.getRecipients(RecipientType.TO), contactHelper);
				target.compareCounterparty = to.toString();
				target.sender = new SpannableStringBuilder(
						mContext.getString(R.string.message_to_label))
						.append(to);
			} else {
				target.sender = Address.toFriendly(addrs, contactHelper);
				target.compareCounterparty = target.sender.toString();
			}

			if (addrs.length > 0) {
				target.senderAddress = addrs[0].getAddress();
			} else {
				// a reasonable fallback "whomever we were corresponding with
				target.senderAddress = target.compareCounterparty;
			}

			target.uid = message.getUid();

			target.account = account.getUuid();
			target.uri = "email://messages/" + account.getAccountNumber() + "/"
					+ message.getFolder().getName() + "/" + message.getUid();

		} catch (MessagingException me) {
			Log.w(MAIL.LOG_TAG, "Unable to load message info", me);
		}
	}

	public CharSequence getDisplayName(Account account, Address[] fromAddrs,
			Address[] toAddrs) {
		final Contacts contactHelper = MAIL.showContactName() ? Contacts
				.getInstance(mContext) : null;

		CharSequence displayName;
		if (fromAddrs.length > 0 && account.isAnIdentity(fromAddrs[0])) {
			CharSequence to = Address.toFriendly(toAddrs, contactHelper);
			displayName = new SpannableStringBuilder(
					mContext.getString(R.string.message_to_label)).append(to);
		} else {
			displayName = Address.toFriendly(fromAddrs, contactHelper);
		}

		return displayName;
	}

	public boolean toMe(Account account, Address[] toAddrs) {
		for (Address address : toAddrs) {
			if (account.isAnIdentity(address)) {
				return true;
			}
		}
		return false;
	}
}
