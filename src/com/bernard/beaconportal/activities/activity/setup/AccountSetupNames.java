package com.bernard.beaconportal.activities.activity.setup;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bernard.beaconportal.activities.*;
import com.bernard.beaconportal.activities.MainActivity.Update;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.K9Activity;
import com.bernard.beaconportal.activities.helper.Utility;
import com.bernard.beaconportal.activities.R;

public class AccountSetupNames extends K9Activity implements OnClickListener {
	private static final String EXTRA_ACCOUNT = "account";

	private String Data, Band, Number, Class, Teacher, Title, Date, Type,
			Description, DescriptionAll, DescriptionCheck, due_today_shared,
			due_today_shared_content;

	private static final String TAG = "K9MailExtension";

	public static final String PREF_NAME = "pref_name";

	static final Uri k9AccountsUri = Uri
			.parse("content://com.bernard.beaconportal.activities.messageprovider/accounts/");
	static final String k9UnreadUri = "content://com.bernard.beaconportal.activities.messageprovider/account_unread/";
	static final String k9MessageProvider = "content://com.bernard.beaconportal.activities.messageprovider/";

	ContentObserver contentObserver = null;
	BroadcastReceiver receiver = null;
	IntentFilter filter = null;

	DrawerLayout mDrawerLayout;
	LinearLayout mDrawerLinear;
	TextView mWelcomePerson;
	TextView mWelcome;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	private MenuListAdapter mMenuAdapter;
	String actionbar_colors, background_colorsString;
	private String Show_View;
	String[] title;
	String[] count;
	int[] icon;
	private String counterss;
	private int counters;
	private static ProgressDialog pd;
	private static Context context;
	Fragment fragment1 = new FragmentsView();
	Fragment fragment2 = new FragmentsHomeworkDue();
	Fragment fragment3 = new FragmentSettings();
	private CharSequence mDrawerTitle;
	private CharSequence mDrawerTitleCheck;
	private CharSequence mTitle;
	private String KEY_STATE_TITLE;

	ProgressDialog LoginDialog;
	
	private HttpResponse response;

	private int starts = 0;

	private String checkbox_edit;

	private BaseAccount mSelectedContextAccount;

	private int shared1;

	private int number;

	private int countersss1;

	private int mUnreadMessageCount = 0;

	private String due_tommorow_shared, due_tommorow_shared_content;

	private List<Due_Today_List> due_today_list;

	private List<String> read_due_today_list;

	private String K9count;

	private View swipe;

	private int shared;

	private int countersss;

	private ArrayAdapter<Due_Today_List> adapter;

	private EditText mDescription;

	private EditText mName;

	private Account mAccount;

	private Button mDoneButton;

	public static void actionSetNames(Context context, Account account) {
		Intent i = new Intent(context, AccountSetupNames.class);
		i.putExtra(EXTRA_ACCOUNT, account.getUuid());
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup_names);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

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

		mDescription = (EditText) findViewById(R.id.account_description);
		mName = (EditText) findViewById(R.id.account_name);
		mDoneButton = (Button) findViewById(R.id.done);
		mDoneButton.setOnClickListener(this);

		TextWatcher validationTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				validateFields();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		mName.addTextChangedListener(validationTextWatcher);

		mName.setKeyListener(TextKeyListener.getInstance(false,
				Capitalize.WORDS));

		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		/*
		 * Since this field is considered optional, we don't set this here. If
		 * the user fills in a value we'll reset the current value, otherwise we
		 * just leave the saved value alone.
		 */
		// mDescription.setText(mAccount.getDescription());
		if (mAccount.getName() != null) {
			mName.setText(mAccount.getName());
		}
		if (!Utility.requiredFieldValid(mName)) {
			mDoneButton.setEnabled(false);
		}
	}

	private void validateFields() {
		mDoneButton.setEnabled(Utility.requiredFieldValid(mName));
		Utility.setCompoundDrawablesAlpha(mDoneButton,
				mDoneButton.isEnabled() ? 255 : 128);
	}

	protected void onNext() {

		InputMethodManager im = (InputMethodManager) this
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		if (Utility.requiredFieldValid(mDescription)) {
			mAccount.setDescription(mDescription.getText().toString());
		}
		mAccount.setName(mName.getText().toString());
		mAccount.save(Preferences.getPreferences(this));
		Accounts.listAccounts(this);
		finish();

		String name = mName.getText().toString();

		SharedPreferences.Editor localEditor = getSharedPreferences(
				"Login_info", Context.MODE_PRIVATE).edit();

		localEditor.putString("name", name);

		localEditor.commit();

		Update due_tommorow = new Update();

		due_tommorow.execute();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.done:
			onNext();

			break;
		}
	}

	public class Update extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... urls) {

			SharedPreferences bDay = getSharedPreferences("Login_Info",
					Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = getSharedPreferences("Login_Info",
					Context.MODE_PRIVATE);

			String day = day1.replaceFirst("^0+(?!$)", "");

			String month = month1.replaceFirst("^0+(?!$)", "");

			String year = year1.replaceFirst("^0+(?!$)", "");

			String birthday = month + "/" + day + "/" + year;

			System.out.println("Birthday = " + birthday);

			String user = userName.getString("username", "");

			// String user = (username).split("@")[0];

			System.out.println("Username = " + user);

			try {

				// HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				// HttpGet httpGet = new HttpGet(
				// "http://www2.beaconschool.org/~markovic/lincoln.php");

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://www2.beaconschool.org/~markovic/lincoln.php");

				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs
							.add(new BasicNameValuePair("username", user));
					nameValuePairs.add(new BasicNameValuePair("birthday",
							birthday));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					response = httpclient.execute(httppost);

					Log.d("Http Response:", response.toString());

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

				try {
					Log.d("receiver", "animation stopped and downloaded file");

					String homework = new Scanner(response.getEntity()
							.getContent(), "UTF-8").useDelimiter("\\A").next();

					// String homework =
					// Html.fromHtml(homework_html).toString();

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"homework", Context.MODE_PRIVATE).edit();

					localEditor.putString("homework_content", homework);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

					parse_due_tommorow_string();
					
					parse_due_today_string();
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} finally {

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			Intent intent = new Intent(AccountSetupNames.this,
					MainActivity.class);

			startActivity(intent);

		}

	}

	public void parse_due_tommorow_string() {

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("homework", Context.MODE_PRIVATE);

		String due_tommorow = Today_Homework.getString("homework_content", "");

		due_tommorow = due_tommorow.replaceAll("^\"|\"$", "");

		due_tommorow = due_tommorow.substring(3);

		Log.d("homework due today", due_tommorow);

		StringBuilder DescriptionAll = new StringBuilder();

		InputStream is = new ByteArrayInputStream(due_tommorow.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int value = 0;
			countersss1 = 0;
			int state = 0;
			shared1 = 0;
			int shared_add1 = 0;
			String str = "";
			StringBuilder strb = new StringBuilder();

			while ((value = reader.read()) != -1) {

				char c = (char) value;

				if (c == ',') {
					if (state == 1) {
						state = 2;
					} else {
						state = 0;
						strb.append(c);
					}
				}

				else if (c == '"') {
					if (state == 2) {

						System.out.println("shared_add= " + shared_add1);

						if (shared_add1 == 8) {
							shared_add1 = 0;
							shared1++;

						}

						due_tommorow_shared = "due_tommorow"
								+ Integer.toString(shared1);

						due_tommorow_shared_content = "due_tommorow"
								+ Integer.toString(shared_add1);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = getSharedPreferences(
								due_tommorow_shared, Context.MODE_PRIVATE)
								.edit();

						localEditor
								.putString(due_tommorow_shared_content, strr);

						localEditor.apply();

						System.out.println("shared= " + shared1);

						strb.setLength(0);
						state = 0;
						countersss1++;
						shared_add1++;

					} else {
						state = 1;
						strb.append(c);
					}
				} else {
					strb.append(c);
				}

			}

			String strr = strb.toString().replaceAll("^\"|\"$", "");

			System.out.println("tommorow shared= " + strr);

			due_tommorow_shared_content = "due_tommorow7";

			SharedPreferences.Editor localEditor = getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE).edit();

			localEditor.putString(due_tommorow_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getSharedPreferences(
					"due_tommorow_counter", Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared1);

			localEditor1.apply();

			strb.setLength(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

		}
	}

	public void parse_due_today_string() {

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("homework", Context.MODE_PRIVATE);

		String Due_Today = Today_Homework.getString("homework_content", "");

		Due_Today = Due_Today.replaceAll("^\"|\"$", "");

		Due_Today = Due_Today.substring(3);

		Log.d("homework due today", Due_Today);

		InputStream is = new ByteArrayInputStream(Due_Today.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int value = 0;
			countersss = 0;
			int state = 0;
			shared = 0;
			int shared_add = 0;
			String str = "";
			StringBuilder strb = new StringBuilder();

			while ((value = reader.read()) != -1) {

				char c = (char) value;

				if (c == ',') {
					if (state == 1) {
						state = 2;
					} else {
						state = 0;
						strb.append(c);
					}
				}

				else if (c == '"') {
					if (state == 2) {

						System.out.println("shared_add= " + shared_add);

						if (shared_add == 8) {
							shared_add = 0;
							shared++;

						}

						due_today_shared = "due_today"
								+ Integer.toString(shared);

						due_today_shared_content = "due_today"
								+ Integer.toString(shared_add);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = getSharedPreferences(
								due_today_shared, Context.MODE_PRIVATE).edit();

						localEditor.putString(due_today_shared_content, strr);

						localEditor.apply();

						System.out.println("shared= " + shared);

						strb.setLength(0);
						state = 0;
						countersss++;
						shared_add++;

					} else {
						state = 1;
						strb.append(c);
					}
				} else {
					strb.append(c);
				}

			}

			String strr = strb.toString().replaceAll("^\"|\"$", "");

			System.out.println("shared_pref_final= " + strr);

			due_today_shared_content = "due_today7";

			SharedPreferences.Editor localEditor = getSharedPreferences(
					due_today_shared, Context.MODE_PRIVATE).edit();

			localEditor.putString(due_today_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getSharedPreferences(
					"due_today_counter", Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

		}
	}
	
}
