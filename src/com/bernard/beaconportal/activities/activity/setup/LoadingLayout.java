package com.bernard.beaconportal.activities.activity.setup;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.homeworkdue.FragmentsHomeworkDue;
import com.bernard.beaconportal.activities.schedule.view.FragmentsSchedule;
import com.bernard.beaconportal.activities.settings.FragmentSettings;

public class LoadingLayout extends ActionBarActivity {

	private String due_today_shared, due_today_shared_content;

	public static final String PREF_NAME = "pref_name";

	static final Uri mailAccountsUri = Uri
			.parse("content://com.bernard.beaconportal.activities.messageprovider/accounts/");
	static final String mailUnreadUri = "content://com.bernard.beaconportal.activities.messageprovider/account_unread/";
	static final String mailMessageProvider = "content://com.bernard.beaconportal.activities.messageprovider/";

	ContentObserver contentObserver = null;
	BroadcastReceiver receiver = null;
	IntentFilter filter = null;

	DrawerLayout mDrawerLayout;
	LinearLayout mDrawerLinear;
	TextView mWelcomePerson;
	TextView mWelcome;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	String actionbar_colors, actionbar_colorsString;
	String[] title;
	String[] count;
	int[] icon;
	Fragment fragment1 = new FragmentsSchedule();
	Fragment fragment2 = new FragmentsHomeworkDue();
	Fragment fragment3 = new FragmentSettings();
	ProgressDialog LoginDialog;

	private HttpResponse response;

	private String due_tommorow_shared, due_tommorow_shared_content;

	private int shared;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);

		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#4285f4")));

		getSupportActionBar().setElevation(2);

		new Update().execute();

		if (Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(Color.parseColor("#3367d6"));
		}

		ActionBar bar = getSupportActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

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

				new BasicHttpContext();

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://www.beaconschool.org/~markovic/lincoln.php");

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

				} catch (NullPointerException e) {

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"homework", Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (NoSuchElementException e) {

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"homework", Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				} catch (RuntimeException e) {
					SharedPreferences.Editor localEditor = getSharedPreferences(
							"homework", Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}
			} finally {

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			SharedPreferences download_error = getSharedPreferences("homework",
					Context.MODE_PRIVATE);

			String error = download_error.getString("download_error", "no");

			String download_errors = "Error while downloading homework, there  could be no internet connection or portal's down";

			if (error.equals("yes")) {

				SharedPreferences.Editor localEditor = getSharedPreferences(
						"homework", Context.MODE_PRIVATE).edit();

				Toast.makeText(LoadingLayout.this, download_errors,
						Toast.LENGTH_LONG).show();

				localEditor.putString("download_error", "no");

				localEditor.commit();

			}

			Intent intent = new Intent(LoadingLayout.this, MainActivity.class);

			startActivity(intent);

		}

	}

	public void parse_due_tommorow_string() {

		SharedPreferences Tommorow_Homework = getSharedPreferences("homework",
				Context.MODE_PRIVATE);

		String Due_Tommorow = Tommorow_Homework.getString("homework_content",
				"");

		Due_Tommorow = Due_Tommorow.replaceAll("^\"|\"$", "");

		Due_Tommorow = Due_Tommorow.substring(3);

		Log.d("homework due tommorow", Due_Tommorow);

		InputStream is = new ByteArrayInputStream(Due_Tommorow.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int value = 0;
			int state = 0;
			shared = 0;
			int shared_add = 0;
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

						System.out.println("shared_add= " + shared_add + " "
								+ strb);

						String strrr = strb.toString()
								.replaceAll("^\"|\"$", "");

						if (strrr.length() < 3 && isStringNumeric(strrr)) {
							shared_add = 0;
							shared++;

							Log.d("restart", "yes");

							due_tommorow_shared = "due_tommorow"
									+ Integer.toString(shared);

							SharedPreferences Band = getApplicationContext()
									.getSharedPreferences("last band tommorow",
											Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_tommorow_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_tommorow0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = getSharedPreferences(
									"last band tommorow", Context.MODE_PRIVATE);

							SharedPreferences Description = getSharedPreferences(
									due_tommorow_shared, Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_tommorow7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_tommorow_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_tommorow7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = getSharedPreferences(
								"last band tommorow", Context.MODE_PRIVATE)
								.edit();

						localEditors.putString("last string", strrr);

						localEditors.apply();

						due_tommorow_shared = "due_tommorow"
								+ Integer.toString(shared);

						due_tommorow_shared_content = "due_tommorow"
								+ Integer.toString(shared_add);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = getSharedPreferences(
								due_tommorow_shared, Context.MODE_PRIVATE)
								.edit();

						localEditor
								.putString(due_tommorow_shared_content, strr);

						localEditor.apply();

						System.out.println("shared= " + shared);

						strb.setLength(0);
						state = 0;
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

			due_tommorow_shared_content = "due_tommorow7";

			SharedPreferences.Editor localEditor = getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE).edit();

			localEditor.putString(due_tommorow_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getSharedPreferences(
					"due_tommorow_counter", Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}
	}

	public void parse_due_today_string() {

		SharedPreferences Today_Homework = getSharedPreferences("homework",
				Context.MODE_PRIVATE);

		String Due_Today = Today_Homework.getString("homework_content", "");

		Due_Today = Due_Today.replaceAll("^\"|\"$", "");

		Due_Today = Due_Today.substring(7);

		Log.d("homework due today", Due_Today);

		InputStream is = new ByteArrayInputStream(Due_Today.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int value = 0;
			int state = 0;
			shared = 0;
			int shared_add = 0;
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

						System.out.println("shared_add= " + shared_add + " "
								+ strb);

						String strrr = strb.toString()
								.replaceAll("^\"|\"$", "");

						if (strrr.length() < 3 && isStringNumeric(strrr)) {
							shared_add = 0;
							shared++;

							Log.d("restart", "yes");

							due_today_shared = "due_today"
									+ Integer.toString(shared);

							SharedPreferences Band = getSharedPreferences(
									"last band today", Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_today_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_today0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = getSharedPreferences(
									"last band today", Context.MODE_PRIVATE);

							SharedPreferences Description = getSharedPreferences(
									due_today_shared, Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_today7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_today_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_today7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = getSharedPreferences(
								"last band today", Context.MODE_PRIVATE).edit();

						localEditors.putString("last string", strrr);

						localEditors.apply();

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

			e.printStackTrace();
		}

		finally {

		}
	}

	public static boolean isStringNumeric(String str) {
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols
				.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();

		try {

			if (!Character.isDigit(str.charAt(0))
					&& str.charAt(0) != localeMinusSign)
				return false;

			boolean isDecimalSeparatorFound = false;
			char localeDecimalSeparator = currentLocaleSymbols
					.getDecimalSeparator();

			for (char c : str.substring(1).toCharArray()) {
				if (!Character.isDigit(c)) {
					if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
						isDecimalSeparatorFound = true;
						continue;
					}
					return false;
				}
			}
			return true;

		} catch (StringIndexOutOfBoundsException e) {

			e.printStackTrace();

			return false;

		}
	}

}