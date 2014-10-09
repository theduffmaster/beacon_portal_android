package com.bernard.beaconportal.activities.activity.setup;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.BaseAccount;
import com.bernard.beaconportal.activities.Due_Today_List;
import com.bernard.beaconportal.activities.FragmentSettings;
import com.bernard.beaconportal.activities.FragmentsHomeworkDue;
import com.bernard.beaconportal.activities.FragmentsSchedule;
import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.MenuListAdapter;
import com.bernard.beaconportal.activities.R;

public class LoadingLayout extends Activity{
	
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
	Fragment fragment1 = new FragmentsSchedule();
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);
		
		new Update().execute();
		
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
				// "http://www.beaconschool.org/~markovic/lincoln.php");

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
					
					}catch (NullPointerException e) {
						
						SharedPreferences.Editor localEditor =
								getSharedPreferences("homework",
										Context.MODE_PRIVATE).edit();

						localEditor.putString("download_error", "yes");

						localEditor.apply();

						e.printStackTrace();
					}

					catch (NoSuchElementException e) {

						SharedPreferences.Editor localEditor =
								getSharedPreferences("homework",
										Context.MODE_PRIVATE).edit();

						localEditor.putString("download_error", "yes");

						localEditor.apply();

						e.printStackTrace();
					}
				catch (RuntimeException e) {
					SharedPreferences.Editor localEditor =
							getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

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

				SharedPreferences download_error = 
						getSharedPreferences("homework", Context.MODE_PRIVATE);

				String error = download_error.getString("download_error", "no");

				String download_errors = "Error while downloading homework, there  could be no internet connection or portal's down";
					
				if (error.equals("yes")) {

					SharedPreferences.Editor localEditor = 
							getSharedPreferences("homework", Context.MODE_PRIVATE)
							.edit();

					Toast.makeText(LoadingLayout.this, download_errors, Toast.LENGTH_LONG)
							.show();

					localEditor.putString("download_error", "no");

					localEditor.commit();

				}

			Intent intent = new Intent(LoadingLayout.this,
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