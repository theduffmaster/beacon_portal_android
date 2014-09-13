package com.bernard.beaconportal.activities.activity.setup;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.EmailAddressValidator;
import com.bernard.beaconportal.activities.K9;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.K9Activity;
import com.bernard.beaconportal.activities.activity.setup.AccountSetupCheckSettings.CheckDirection;
import com.bernard.beaconportal.activities.helper.Utility;

/**
 * Prompts the user for the email address and password. Attempts to lookup
 * default settings for the domain the user specified. If the domain is known
 * the settings are handed off to the AccountSetupCheckSettings activity. If no
 * settings are found the settings are handed off to the AccountSetupAccountType
 * activity.
 */
@SuppressLint("ResourceAsColor")
public class AccountSetupBasics extends K9Activity implements OnClickListener,
		TextWatcher {
	private final static String EXTRA_ACCOUNT = "com.bernard.beaconportal.activities.AccountSetupBasics.account";
	private final static int DIALOG_NOTE = 1;
	private final static String STATE_KEY_PROVIDER = "com.bernard.beaconportal.activities.AccountSetupBasics.provider";
	private final static String STATE_KEY_CHECKED_INCOMING = "com.bernard.beaconportal.activities.AccountSetupBasics.checkedIncoming";

	private EditText mEmailView;
	private EditText mPasswordView;
	private Button mNextButton;
	private Button mManualSetupButton;
	private Account mAccount;
	private Provider mProvider;

	private EmailAddressValidator mEmailValidator = new EmailAddressValidator();
	private boolean mCheckedIncoming = false;

	public static void actionNewAccount(Context context) {
		Intent i = new Intent(context, AccountSetupBasics.class);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup_basics);

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#03a9f4")));

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		// getActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +
		// "Login" + "</font>")));

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		mEmailView = (EditText) findViewById(R.id.account_email);
		mPasswordView = (EditText) findViewById(R.id.account_password);
		mNextButton = (Button) findViewById(R.id.next);
		mManualSetupButton = (Button) findViewById(R.id.manual_setup);

		mNextButton.setOnClickListener(this);
		mManualSetupButton.setOnClickListener(this);

		mEmailView.addTextChangedListener(this);
		mPasswordView.addTextChangedListener(this);

		mEmailView.setText("@beaconschool.org");

	}

	@Override
	public void onResume() {
		super.onResume();
		validateFields();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAccount != null) {
			outState.putString(EXTRA_ACCOUNT, mAccount.getUuid());
		}
		if (mProvider != null) {
			outState.putSerializable(STATE_KEY_PROVIDER, mProvider);
		}
		outState.putBoolean(STATE_KEY_CHECKED_INCOMING, mCheckedIncoming);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState.containsKey(EXTRA_ACCOUNT)) {
			String accountUuid = savedInstanceState.getString(EXTRA_ACCOUNT);
			mAccount = Preferences.getPreferences(this).getAccount(accountUuid);
		}

		if (savedInstanceState.containsKey(STATE_KEY_PROVIDER)) {
			mProvider = (Provider) savedInstanceState
					.getSerializable(STATE_KEY_PROVIDER);
		}

		mCheckedIncoming = savedInstanceState
				.getBoolean(STATE_KEY_CHECKED_INCOMING);
	}

	@Override
	public void afterTextChanged(Editable s) {
		validateFields();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private void validateFields() {
		String email = mEmailView.getText().toString();
		boolean valid = Utility.requiredFieldValid(mEmailView)
				&& Utility.requiredFieldValid(mPasswordView)
				&& mEmailValidator.isValidAddressOnly(email);

		mNextButton.setEnabled(valid);
		mManualSetupButton.setEnabled(valid);
		/*
		 * Dim the next button's icon to 50% if the button is disabled. TODO
		 * this can probably be done with a stateful drawable. Check into it.
		 * android:state_enabled
		 */
		Utility.setCompoundDrawablesAlpha(mNextButton,
				mNextButton.isEnabled() ? 255 : 128);
	}

	private String getOwnerName() {
		String name = null;
		try {
			name = getDefaultAccountName();
		} catch (Exception e) {
			Log.e(K9.LOG_TAG, "Could not get default account name", e);
		}

		if (name == null) {
			name = "";
		}
		return name;
	}

	private String getDefaultAccountName() {
		String name = null;
		Account account = Preferences.getPreferences(this).getDefaultAccount();
		if (account != null) {
			name = account.getName();
		}
		return name;
	}

	@Override
	public Dialog onCreateDialog(int id) {
		if (id == DIALOG_NOTE) {
			if (mProvider != null && mProvider.note != null) {
				return new AlertDialog.Builder(this)
						.setMessage(mProvider.note)
						.setPositiveButton(getString(R.string.okay_action),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finishAutoSetup();
									}
								})
						.setNegativeButton(getString(R.string.cancel_action),
								null).create();
			}
		}
		return null;
	}

	private void finishAutoSetup() {
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String[] emailParts = splitEmail(email);
		String user = emailParts[0];
		String domain = emailParts[1];
		URI incomingUri = null;
		URI outgoingUri = null;
		try {
			String userEnc = URLEncoder.encode(user, "UTF-8");
			String passwordEnc = URLEncoder.encode(password, "UTF-8");

			String incomingUsername = mProvider.incomingUsernameTemplate;
			incomingUsername = incomingUsername.replaceAll("\\$email", email);
			incomingUsername = incomingUsername.replaceAll("\\$user", userEnc);
			incomingUsername = incomingUsername.replaceAll("\\$domain", domain);

			URI incomingUriTemplate = mProvider.incomingUriTemplate;
			incomingUri = new URI(incomingUriTemplate.getScheme(),
					incomingUsername + ":" + passwordEnc,
					incomingUriTemplate.getHost(),
					incomingUriTemplate.getPort(), null, null, null);

			String outgoingUsername = mProvider.outgoingUsernameTemplate;

			URI outgoingUriTemplate = mProvider.outgoingUriTemplate;

			if (outgoingUsername != null) {
				outgoingUsername = outgoingUsername.replaceAll("\\$email",
						email);
				outgoingUsername = outgoingUsername.replaceAll("\\$user",
						userEnc);
				outgoingUsername = outgoingUsername.replaceAll("\\$domain",
						domain);
				outgoingUri = new URI(outgoingUriTemplate.getScheme(),
						outgoingUsername + ":" + passwordEnc,
						outgoingUriTemplate.getHost(),
						outgoingUriTemplate.getPort(), null, null, null);

			} else {
				outgoingUri = new URI(outgoingUriTemplate.getScheme(), null,
						outgoingUriTemplate.getHost(),
						outgoingUriTemplate.getPort(), null, null, null);

			}
			if (mAccount == null) {
				mAccount = Preferences.getPreferences(this).newAccount();
			}
			mAccount.setName(getOwnerName());
			mAccount.setEmail(email);
			mAccount.setStoreUri(incomingUri.toString());
			mAccount.setTransportUri(outgoingUri.toString());
			mAccount.setDraftsFolderName(getString(R.string.special_mailbox_name_drafts));
			mAccount.setTrashFolderName(getString(R.string.special_mailbox_name_trash));
			mAccount.setArchiveFolderName(getString(R.string.special_mailbox_name_archive));
			// Yahoo! has a special folder for Spam, called "Bulk Mail".
			if (incomingUriTemplate.getHost().toLowerCase(Locale.US)
					.endsWith(".yahoo.com")) {
				mAccount.setSpamFolderName("Bulk Mail");
			} else {
				mAccount.setSpamFolderName(getString(R.string.special_mailbox_name_spam));
			}
			mAccount.setSentFolderName(getString(R.string.special_mailbox_name_sent));
			if (incomingUri.toString().startsWith("imap")) {
				mAccount.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
			} else if (incomingUri.toString().startsWith("pop3")) {
				mAccount.setDeletePolicy(Account.DELETE_POLICY_NEVER);
			}
			// Check incoming here. Then check outgoing in onActivityResult()
			AccountSetupCheckSettings.actionCheckSettings(this, mAccount,
					CheckDirection.INCOMING);
		} catch (UnsupportedEncodingException enc) {
			// This really shouldn't happen since the encoding is hardcoded to
			// UTF-8
			Log.e(K9.LOG_TAG, "Couldn't urlencode username or password.", enc);
		} catch (URISyntaxException use) {
			/*
			 * If there is some problem with the URI we give up and go on to
			 * manual setup.
			 */
			onManualSetup();
		}

		SharedPreferences.Editor localEditor = getSharedPreferences(
				"Login_Info", Context.MODE_PRIVATE).edit();

		localEditor.putString("username", email);

		localEditor.apply();

	}

	protected void onNext() {

		showDatePicker();

	}

	public void showDatePicker() {
		// Initializiation
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		View customView = inflater.inflate(R.layout.bdaypicker, null);
		dialogBuilder.setView(customView);
		Calendar now = Calendar.getInstance();
		final DatePicker datePicker = (DatePicker) customView
				.findViewById(R.id.dialog_datepicker);
		final TextView dateTextView = (TextView) customView
				.findViewById(R.id.dialog_dateview);
		final SimpleDateFormat dateViewFormatter = new SimpleDateFormat(
				"MM/dd/yyyy");
		final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		// Minimum date
		Calendar minDate = Calendar.getInstance();
		try {
			minDate.setTime(formatter.parse("1.1.1920"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// View settings
		dialogBuilder.setTitle("Please Enter Your Date of Birth");
		Calendar choosenDate = Calendar.getInstance();
		int year = choosenDate.get(Calendar.YEAR);
		int month = choosenDate.get(Calendar.MONTH);
		int day = choosenDate.get(Calendar.DAY_OF_MONTH);
		try {

			year = 2000;
			month = 0;
			day = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar dateToDisplay = Calendar.getInstance();
		dateToDisplay.set(year, month, day);
		dateTextView.setText(dateViewFormatter.format(dateToDisplay.getTime()));
		// Buttons
		dialogBuilder.setNegativeButton("Go Back",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialogBuilder.setPositiveButton("Login",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar choosen = Calendar.getInstance();

						int year = datePicker.getYear();
						int month = datePicker.getMonth();
						int day = datePicker.getDayOfMonth();

						String email = mEmailView.getText().toString();
						String[] emailParts = splitEmail(email);
						String domain = emailParts[1];
						mProvider = findProviderForDomain(domain);
						if (mProvider == null) {
							/*
							 * We don't have default settings for this account,
							 * start the manual setup process.
							 */
							onManualSetup();
							return;
						}

						if (mProvider.note != null) {
							showDialog(DIALOG_NOTE);
						} else {
							finishAutoSetup();
						}

						SharedPreferences.Editor localEditor = getSharedPreferences(
								"Login_Info", Context.MODE_PRIVATE).edit();

						localEditor.putString("email", email);

						localEditor.putInt("Day", day);

						localEditor.putInt("Year", year);

						localEditor.putInt("Month", month);

						localEditor.apply();

						dialog.dismiss();
					}
				});
		final AlertDialog dialog = dialogBuilder.create();
		// Initialize datepicker in dialog atepicker
		datePicker.init(year, month, day,
				new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar choosenDate = Calendar.getInstance();
						choosenDate.set(year, monthOfYear, dayOfMonth);
						dateTextView.setText(dateViewFormatter
								.format(choosenDate.getTime()));

						dateTextView.setTextColor(Color.parseColor("#58585b"));
						((Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE))
								.setEnabled(true);
					}

				});

		dialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (!mCheckedIncoming) {
				// We've successfully checked incoming. Now check outgoing.
				mCheckedIncoming = true;
				AccountSetupCheckSettings.actionCheckSettings(this, mAccount,
						CheckDirection.OUTGOING);
			} else {
				// We've successfully checked outgoing as well.
				mAccount.setDescription(mAccount.getEmail());
				mAccount.save(Preferences.getPreferences(this));
				K9.setServicesEnabled(this);
				AccountSetupNames.actionSetNames(this, mAccount);
				finish();
			}
		}
	}

	private void onManualSetup() {
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String[] emailParts = splitEmail(email);
		String user = emailParts[0];
		String domain = emailParts[1];

		if (mAccount == null) {
			mAccount = Preferences.getPreferences(this).newAccount();
		}
		mAccount.setName(getOwnerName());
		mAccount.setEmail(email);
		try {
			String userEnc = URLEncoder.encode(user, "UTF-8");
			String passwordEnc = URLEncoder.encode(password, "UTF-8");

			URI uri = new URI("placeholder", userEnc + ":" + passwordEnc,
					"mail." + domain, -1, null, null, null);
			mAccount.setStoreUri(uri.toString());
			mAccount.setTransportUri(uri.toString());
		} catch (UnsupportedEncodingException enc) {
			// This really shouldn't happen since the encoding is hardcoded to
			// UTF-8
			Log.e(K9.LOG_TAG, "Couldn't urlencode username or password.", enc);
		} catch (URISyntaxException use) {
			/*
			 * If we can't set up the URL we just continue. It's only for
			 * convenience.
			 */
		}
		mAccount.setDraftsFolderName(getString(R.string.special_mailbox_name_drafts));
		mAccount.setTrashFolderName(getString(R.string.special_mailbox_name_trash));
		mAccount.setSentFolderName(getString(R.string.special_mailbox_name_sent));
		mAccount.setArchiveFolderName(getString(R.string.special_mailbox_name_archive));
		// Yahoo! has a special folder for Spam, called "Bulk Mail".
		if (domain.endsWith(".yahoo.com")) {
			mAccount.setSpamFolderName("Bulk Mail");
		} else {
			mAccount.setSpamFolderName(getString(R.string.special_mailbox_name_spam));
		}

		AccountSetupAccountType.actionSelectAccountType(this, mAccount, false);

		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			onNext();
			break;
		case R.id.manual_setup:
			onManualSetup();
			break;
		}
	}

	/**
	 * Attempts to get the given attribute as a String resource first, and if it
	 * fails returns the attribute as a simple String value.
	 * 
	 * @param xml
	 * @param name
	 * @return
	 */
	private String getXmlAttribute(XmlResourceParser xml, String name) {
		int resId = xml.getAttributeResourceValue(null, name, 0);
		if (resId == 0) {
			return xml.getAttributeValue(null, name);
		} else {
			return getString(resId);
		}
	}

	private Provider findProviderForDomain(String domain) {
		try {
			XmlResourceParser xml = getResources().getXml(R.xml.providers);
			int xmlEventType;
			Provider provider = null;
			while ((xmlEventType = xml.next()) != XmlPullParser.END_DOCUMENT) {
				if (xmlEventType == XmlPullParser.START_TAG
						&& "provider".equals(xml.getName())
						&& domain.equalsIgnoreCase(getXmlAttribute(xml,
								"domain"))) {
					provider = new Provider();
					provider.id = getXmlAttribute(xml, "id");
					provider.label = getXmlAttribute(xml, "label");
					provider.domain = getXmlAttribute(xml, "domain");
					provider.note = getXmlAttribute(xml, "note");
				} else if (xmlEventType == XmlPullParser.START_TAG
						&& "incoming".equals(xml.getName()) && provider != null) {
					provider.incomingUriTemplate = new URI(getXmlAttribute(xml,
							"uri"));
					provider.incomingUsernameTemplate = getXmlAttribute(xml,
							"username");
				} else if (xmlEventType == XmlPullParser.START_TAG
						&& "outgoing".equals(xml.getName()) && provider != null) {
					provider.outgoingUriTemplate = new URI(getXmlAttribute(xml,
							"uri"));
					provider.outgoingUsernameTemplate = getXmlAttribute(xml,
							"username");
				} else if (xmlEventType == XmlPullParser.END_TAG
						&& "provider".equals(xml.getName()) && provider != null) {
					return provider;
				}
			}
		} catch (Exception e) {
			Log.e(K9.LOG_TAG, "Error while trying to load provider settings.",
					e);
		}
		return null;
	}

	private String[] splitEmail(String email) {
		String[] retParts = new String[2];
		String[] emailParts = email.split("@");
		retParts[0] = (emailParts.length > 0) ? emailParts[0] : "";
		retParts[1] = (emailParts.length > 1) ? emailParts[1] : "";
		return retParts;
	}

	static class Provider implements Serializable {
		private static final long serialVersionUID = 8511656164616538989L;

		public String id;

		public String label;

		public String domain;

		public URI incomingUriTemplate;

		public String incomingUsernameTemplate;

		public URI outgoingUriTemplate;

		public String outgoingUsernameTemplate;

		public String note;
	}
}
