package com.bernard.beaconportal.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.setup.AccountSetupBasics;
import com.bernard.beaconportal.activities.homeworkdue.FragmentsHomeworkDue;
import com.bernard.beaconportal.activities.homeworkdue.alarms.DailyHomeworkDownload;
import com.bernard.beaconportal.activities.homeworkdue.alarms.MidnightHomeworkDownload;
import com.bernard.beaconportal.activities.schedule.view.FragmentsSchedule;
import com.bernard.beaconportal.activities.settings.FragmentSettings;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@SuppressLint("ResourceAsColor")
public class MainActivity extends ActionBarActivity {
	private String Date, Type, due_today_shared, due_today_shared_content,
			due_schedule_shared, due_schedule_shared_content;

	private static final String TAG = "MAILMailExtension";

	public static final String PREF_NAME = "pref_name";

	static final Uri mailAccountsUri = Uri
			.parse("content://com.bernard.beaconportal.activities.messageprovider/accounts/");
	static final String mailUnreadUri = "content://com.bernard.beaconportal.activities.messageprovider/account_unread/";
	static final String mailMessageProvider = "content://com.bernard.beaconportal.activities.messageprovider/";

	ContentObserver contentObserver = null;
	BroadcastReceiver receiver = null;
	IntentFilter filter = null;

	private AlertDialog alertDialog;

	private int check;

	final static int RQS_1 = 1;

	int selected_item;
	DrawerLayout mDrawerLayout;
	LinearLayout mDrawerLinear;
	TextView mWelcomePerson;
	TextView mWelcome;
	static ListView mDrawerList;
	ImageView mShadow;
	ActionBarDrawerToggle mDrawerToggle;
	private MenuListAdapter mMenuAdapter;
	String actionbar_colors, actionbar_colorsString;
	private String Show_View;
	String[] title;
	String[] count;
	int[] icon;
	public static String counterss;
	Fragment fragment1 = new FragmentsSchedule();
	Fragment fragment2 = new FragmentsHomeworkDue();
	Fragment fragment3 = new FragmentSettings();
	ListFragment fragment4 = new FragmentsResource();
	private CharSequence mDrawerTitle;
	private CharSequence mDrawerTitleCheck;
	private CharSequence mTitle;
	private String KEY_STATE_TITLE;
	private ActionBar mActionBar;

    private Bundle savedInstanceState;

	private HttpResponse response;

	private String date;

	private String due_tommorow_shared, due_tommorow_shared_content;

	private String MAILcount;

	private int shared;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, 1);

		date = sdf.format(c.getTime());

		InputMethodManager im = (InputMethodManager) this
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			counterss = extras.getString("homework_count");
		}

		SharedPreferences rerun = getSharedPreferences("homework_rerun",
				Context.MODE_PRIVATE);

		SharedPreferences sharedprefer = getSharedPreferences(
				"first_run_starts", Context.MODE_PRIVATE);

		SharedPreferences sharedprefererence = getSharedPreferences(
				"Login_info", Context.MODE_PRIVATE);

		if (rerun.contains("first_rerun")) {

		} else {

			SharedPreferences.Editor rerunEditor = getSharedPreferences(
					"homework_rerun", Context.MODE_PRIVATE).edit();

			rerunEditor.putString("first_rerun", "reran");

			rerunEditor.apply();

			// showDatePicker();

		}

		if (sharedprefererence.contains("name")) {

			parse_count();

			new Internet_check_reload().execute();

			SharedPreferences alarm = getSharedPreferences("alarm_check",
					Context.MODE_PRIVATE);

			if (alarm.contains("alarmrerun")) {

			} else {

				SharedPreferences.Editor alarmEditor = getSharedPreferences(
						"alarm_check", Context.MODE_PRIVATE).edit();

				alarmEditor.putString("alarmrerun", "ran");

				alarmEditor.apply();

				setAlarm(this);

				SharedPreferences refresh_time = getSharedPreferences("Alarm",
						Context.MODE_PRIVATE);

				int hour = refresh_time.getInt("Hour", 15);

				int minute = refresh_time.getInt("Minute", 0);

				setAlarmCustom(this, hour, minute);

				Log.d("alarm_first_run_set", "yes");

			}

		} else {

			Intent intent = new Intent(this, AccountSetupBasics.class);
			startActivity(intent);

		}

		if (counterss == null) {

			counterss = "0";

		}

		Log.d(TAG, "onCreate()");

		String packageName = "com.bernard.beaconportal.activities";

		int versionNumber = 0;

		try {
			PackageInfo pi = getApplicationContext().getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionNumber = pi.versionCode;
			String versionName = pi.versionName;

			Log.d(TAG, "Mail is installed - " + versionNumber + " "
					+ versionName);

		} catch (NameNotFoundException e) {
			Log.d(TAG, "Mail not found");
		}

		if (versionNumber <= 1) {
			// Register a listener for broadcasts (needed for the older versions
			// of mail)
			Log.d(TAG, "Initialising BroadcastReceiver for old Mail version");
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					Log.d(TAG, "receiver.onReceive()");
					doRefresh();
				}
			};

			filter = new IntentFilter();
			filter.addAction("com.bernard.beaconportal.activities.intent.action.EMAIL_RECEIVED");
			filter.addAction("com.bernard.beaconportal.activities.intent.action.EMAIL_DELETED");
			filter.addDataScheme("email");
			registerReceiver(receiver, filter);
		} else {
			// Register our own content observer, rather than using
			// addWatchContentUris()
			// since DashClock might not have permission to access the database
			Log.d(TAG, "Initialising ContentObserver for new Mail version");
			contentObserver = new ContentObserver(null) {
				@Override
				public void onChange(boolean selfChange) {
					Log.d(TAG, "contentResolver.onChange()");
					doRefresh();
				}
			};
			getContentResolver().registerContentObserver(
					Uri.parse(mailUnreadUri), true, contentObserver);
		}

		doRefresh();

		int countssssss = getUnreadMAILCount(this);

		MAILcount = Integer.toString(countssssss);

		System.out.println("mail Unread Count = " + countssssss);

		SharedPreferences pref = getSharedPreferences("CheckBox",
				Context.MODE_PRIVATE);

		String checkbox = pref.getString("checked", null);

		if (checkbox != null) {
			if (checkbox.contains("true")) {
				try {
					check_download();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		setContentView(R.layout.drawer_main);

		SharedPreferences sharedpre = getSharedPreferences("show_view",
				Context.MODE_PRIVATE);

		Show_View = sharedpre.getString("show_view", "");

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		SharedPreferences name = getSharedPreferences("Login_info",
				Context.MODE_PRIVATE);

		String person = name.getString("name", "");

		mWelcomePerson = (TextView) findViewById(R.id.Person);

		mWelcomePerson.setText(person);

		mWelcome = (TextView) findViewById(R.id.Welcome);

		mShadow = (ImageView) findViewById(R.id.material_shadow_image_main);

		if (!sharedpref.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";
			
			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

			mWelcomePerson.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			mWelcome.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			if (Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(Color.parseColor("#3367d6"));
			}

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			mWelcomePerson.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			mWelcome.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			mShadow.setBackgroundColor(Color.parseColor(actionbar_colors));

			if (Build.VERSION.SDK_INT >= 21) {
				
				//darken color for status bar
				float[] hsv = new float[3];
				int color = Color.parseColor(actionbar_colors);
				Color.colorToHSV(color, hsv);
				hsv[2] *= 0.85f; // value component
				color = Color.HSVToColor(hsv);
				
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(color);
//				decode(Color.parseColor(actionbar_colors))
			}

		}

        SharedPreferences.Editor localEditor = getSharedPreferences(
                "due_today", Context.MODE_PRIVATE).edit();

        localEditor.putString("inbox", MAILcount);
        localEditor.putString("homeworkdue", counterss);
        localEditor.apply();

        if (Show_View.equals("Homework Due")) {

            title = new String[] { "Homework Due", "Schedule", "Mail",
                    "Resources", "Options", "Logout" };

            icon = new int[] { R.drawable.ic_action_duehomework,
                    R.drawable.ic_action_go_to_today,
                    R.drawable.ic_action_email, R.drawable.ic_action_resources,
                    R.drawable.ic_action_settings, R.drawable.ic_action_logout };

            if (counterss == null && counterss.isEmpty()) {

                count = new String[] { "", "", MAILcount, "", "", "" };

            } else {

                count = new String[] { counterss, "", MAILcount, "", "", "", "" };

            }

        } else {

            if (counterss == null && counterss.isEmpty()) {

                count = new String[] { "", "", MAILcount, "", "", "" };

            } else {

                count = new String[] { "", counterss, MAILcount, "", "", "" };

            }

            title = new String[] { "Schedule", "Homework Due", "Mail",
                    "Resources", "Options", "Logout" };

            icon = new int[] { R.drawable.ic_action_go_to_today,
                    R.drawable.ic_action_duehomework,
                    R.drawable.ic_action_email, R.drawable.ic_action_resources,
                    R.drawable.ic_action_settings, R.drawable.ic_action_logout };

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon,
                count);

        mDrawerList = (ListView) findViewById(R.id.listview_drawer);

        mDrawerList.setAdapter(mMenuAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mActionBar = getSupportActionBar();

		mActionBar.setIcon(new ColorDrawable(getResources().getColor(
                android.R.color.transparent)));

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open) {

			@Override
			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

				if (mDrawerTitle != null
						&& !mDrawerTitle.equals("Beacon Portal")) {
					getSupportActionBar().setTitle(mDrawerTitle);
				}
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		SharedPreferences counts = getSharedPreferences("return_to_main",
				Context.MODE_PRIVATE);

		if (counts.contains("fragment_to_start")) {
			String count = counts.getString("fragment_to_start", null);

			if (count != null) {
				int countss = Integer.parseInt(count);

				if (savedInstanceState == null) {

					selectItem(countss);

					CharSequence cs = mDrawerList.getItemAtPosition(countss).toString();

					mActionBar.setTitle(cs);

					mDrawerTitle = cs;

				}

				counts.edit().remove("fragment_to_start").commit();

			} else {

				if (savedInstanceState == null) {

					selectItem(0);

					selected_item = 0;

					CharSequence cs = mDrawerList.getItemAtPosition(0).toString();

					mActionBar.setTitle(cs);

					mDrawerTitle = cs;

				}

			}

		} else {

			if (savedInstanceState == null) {

				selectItem(0);

				CharSequence cs = mDrawerList.getItemAtPosition(0).toString();

				mActionBar.setTitle(cs);

				mDrawerTitle = cs;

			}

		}

		if (savedInstanceState != null) {
			setTitle(savedInstanceState.getCharSequence(KEY_STATE_TITLE));
		}

		if (!sharedprefer.contains("help_check")) {

			alert_help();

			setProgressBarIndeterminateVisibility(Boolean.TRUE);

		}

		SharedPreferences.Editor localEditor1 = getSharedPreferences(
				"return_to_main", Context.MODE_PRIVATE).edit();

		localEditor1.clear();

		localEditor1.commit();

    }

	@Override
	public void onResume() {
		super.onResume();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open) {

            @Override
            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub

                if (mDrawerTitle != null
                        && !mDrawerTitle.equals("Beacon Portal")) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                }
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, 1);

		date = sdf.format(c.getTime());

		InputMethodManager im = (InputMethodManager) this
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			counterss = extras.getString("homework_count");
		}

		SharedPreferences rerun = getSharedPreferences("homework_rerun",
				Context.MODE_PRIVATE);

		getSharedPreferences("first_run_starts", Context.MODE_PRIVATE);

		SharedPreferences sharedprefererence = getSharedPreferences(
				"Login_info", Context.MODE_PRIVATE);

		if (rerun.contains("first_rerun")) {

		} else {

			SharedPreferences.Editor rerunEditor = getSharedPreferences(
					"homework_rerun", Context.MODE_PRIVATE).edit();

			rerunEditor.putString("first_rerun", "reran");

			rerunEditor.apply();

			// showDatePicker();

		}

		if (sharedprefererence.contains("name")) {

			parse_count();

			new Internet_check_reload().execute();

		} else {

			Intent intent = new Intent(this, AccountSetupBasics.class);
			startActivity(intent);

		}

		if (counterss == null) {

			counterss = "0";

		}

		SharedPreferences pref = getSharedPreferences("CheckBox",
				Context.MODE_PRIVATE);

		String checkbox = pref.getString("checked", null);

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		SharedPreferences name = getSharedPreferences("Login_info",
				Context.MODE_PRIVATE);

		String person = name.getString("name", "");

		mWelcomePerson = (TextView) findViewById(R.id.Person);

		mWelcomePerson.setText(person);

		mWelcome = (TextView) findViewById(R.id.Welcome);

		mShadow = (ImageView) findViewById(R.id.material_shadow_image_main);

		if (!sharedpref.contains("actionbar_color")) {

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));

			mWelcomePerson.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#4285f4")));

			mWelcome.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#4285f4")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			mWelcomePerson.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			mWelcome.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(actionbar_colors)));

			mShadow.setBackgroundColor(Color.parseColor(actionbar_colors));
			
			if (Build.VERSION.SDK_INT >= 21) {
				
				//darken color for status bar
				float[] hsv = new float[3];
				int color = Color.parseColor(actionbar_colors);
				Color.colorToHSV(color, hsv);
				hsv[2] *= 0.85f; // value component
				color = Color.HSVToColor(hsv);
				
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(color);
//				decode(Color.parseColor(actionbar_colors))
			}

		}

		mActionBar = getSupportActionBar();

		mActionBar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		if (checkbox != null) {
			if (checkbox.contains("true")) {
				try {
					check_download();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		String packageName = "com.bernard.beaconportal.activities";

		int versionNumber = 0;

		try {
			PackageInfo pi = getApplicationContext().getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionNumber = pi.versionCode;
			String versionName = pi.versionName;

			Log.d(TAG, "Mail is installed - " + versionNumber + " "
					+ versionName);

		} catch (NameNotFoundException e) {
			Log.d(TAG, "Mail not found");
		}

		if (versionNumber <= 16024) {
			// Register a listener for broadcasts (needed for the older versions
			// of mail)
			Log.d(TAG, "Initialising BroadcastReceiver for old Mail version");
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					Log.d(TAG, "receiver.onReceive()");
					doRefresh();
				}
			};

			filter = new IntentFilter();
			filter.addAction("com.bernard.beaconportal.activities.intent.action.EMAIL_RECEIVED");
			filter.addAction("com.bernard.beaconportal.activities.intent.action.EMAIL_DELETED");
			filter.addDataScheme("email");
			registerReceiver(receiver, filter);
		} else {
			// Register our own content observer, rather than using
			// addWatchContentUris()
			// since DashClock might not have permission to access the database
			Log.d(TAG, "Initialising ContentObserver for new Mail version");
			contentObserver = new ContentObserver(null) {
				@Override
				public void onChange(boolean selfChange) {
					Log.d(TAG, "contentResolver.onChange()");
					doRefresh();
				}
			};
			getContentResolver().registerContentObserver(
					Uri.parse(mailUnreadUri), true, contentObserver);
		}

		doRefresh();

		int countssssss = getUnreadMAILCount(this);

		MAILcount = Integer.toString(countssssss);

		System.out.println("mail Unread Count = " + countssssss);

//        CharSequence cs = mDrawerList.getSelectedItem().toString();
//
//        mActionBar.setTitle(cs);

	}

	public void inbox() {

		System.out.println("inbox");

		Intent intent = new Intent(MainActivity.this, Accounts.class);

		startActivity(intent);

		SharedPreferences sharedprefers = getSharedPreferences("first_inbox",
				Context.MODE_PRIVATE);

		if (sharedprefers.contains("first_inbox")) {

		} else {

			SharedPreferences.Editor localEditor = getSharedPreferences(
					"first_inbox", Context.MODE_PRIVATE).edit();

			localEditor.putString("first_inbox", "ran for the first time");

			Intent intent1 = new Intent(MainActivity.this, Accounts.class);

			startActivity(intent1);

			localEditor.commit();

		}

	}

	public class Internet_check_reload extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {

			check = 0;

			while (check != 1) {

				if (AppStatus.getInstance(getApplicationContext()).isOnline(
						getApplicationContext())) {

					new Update().execute();

					System.out.println("INTERNET WORKED!, UPDATING CONTENT");

					check = 1;

					break;
				}
			}
			return null;
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

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd hh:mm a");
					Calendar cal = Calendar.getInstance();
					String downloaded = dateFormat.format(cal.getTime());

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"homework", Context.MODE_PRIVATE).edit();

					localEditor.putString("homework_content", homework);

					localEditor.putString("download_date", downloaded);

					localEditor.apply();

					parse_shedule_homework();

					Log.d("receiver", "information given to shared preferences");

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {

					e.printStackTrace();
				} catch (NoSuchElementException e) {

					e.printStackTrace();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}

			} finally {

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

		}

	}

	public void parse_count() {

		Calendar calendar = Calendar.getInstance();

		int day = calendar.get(Calendar.DAY_OF_WEEK);

		if (day == 6) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 3);

			date = sdf.format(c.getTime());

			System.out.println("friday");

		} else if (day == 7) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 2);

			date = sdf.format(c.getTime());

			System.out.println("saturday");

		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 1);

			date = sdf.format(c.getTime());

		}

		SharedPreferences Tommorow_Homework = getApplicationContext()
				.getSharedPreferences("homework", Context.MODE_PRIVATE);

		String Due_Tommorow = Html.fromHtml(
				Tommorow_Homework.getString("homework_content", "")).toString();

		new ArrayList<String>();

		new StringBuilder();

		InputStream is = new ByteArrayInputStream(Due_Tommorow.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int i = 0;

			while ((Due_Tommorow = reader.readLine()) != null) {
				String[] part = Due_Tommorow.split("\",\"", -1);
				int noOfItems = part.length;
				int counter = 0;

				counter++;
				counter++;
				counter++;
				counter++;
				counter++;
				counter++;
				Date = counter < noOfItems ? part[counter] : "";
				counter++;
				Type = counter < noOfItems ? part[counter] : "";
				counter++;
				counter++;

				if (Type != null && !Type.isEmpty() && Date.equals(date)) {

					++i;

				}

				counterss = Integer.toString(i);

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}

	}

	public boolean onOptionsItemSelected(android.view.MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
				mDrawerLayout.closeDrawer(mDrawerLinear);
			} else {
				mDrawerLayout.openDrawer(mDrawerLinear);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {


            mDrawerLayout.closeDrawer(mDrawerLinear);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectItem(position);
                }
            }, 225);

        }
	}

	void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (Show_View.equals("Homework Due")) {

			switch (position) {
			case 0:
				ft.replace(R.id.content_frame, fragment2);
				mActionBar.setTitle("Homework Due");
				mDrawerTitle = "Homework Due";
				break;
			case 1:
				ft.replace(R.id.content_frame, fragment1);
				mActionBar.setTitle("Schedule");
				mDrawerTitle = "Schedule";
				break;
			case 2:
				
				inbox();
				mActionBar.setTitle("Mail");
				break;
			case 3:
				ft.replace(R.id.content_frame, fragment4);
				mActionBar.setTitle("Resource");
				mDrawerTitle = "Resource";
				break;
			case 4:
				ft.replace(R.id.content_frame, fragment3);
				mActionBar.setTitle("Options");
				mDrawerTitle = "Options";
				break;

			case 5:
				alert_logout();
				mActionBar.setTitle("Logout");
				break;
			}

		} else {

			switch (position) {
			case 0:
				ft.replace(R.id.content_frame, fragment1);
				mActionBar.setTitle("Schedule");
				mDrawerTitle = "Schedule";
				break;
			case 1:
				ft.replace(R.id.content_frame, fragment2);
				mActionBar.setTitle("Homework Due");
				mDrawerTitle = "Homework Due";
				break;

			case 2:
				inbox();
				mActionBar.setTitle("Mail");
				break;
			case 3:
				ft.replace(R.id.content_frame, fragment4);
				mActionBar.setTitle("Resources");
				mDrawerTitle = "Resources";
				break;
			case 4:
				ft.replace(R.id.content_frame, fragment3);
				mActionBar.setTitle("Options");
				mDrawerTitle = "Options";
				break;

			case 5:
				alert_logout();
				mActionBar.setTitle("Logout");
				break;
			}
		}

		ft.commit();

		mDrawerList.setItemChecked(position, true);

		mDrawerLayout.closeDrawer(mDrawerLinear);
	}

	private void alert_logout() {

		SharedPreferences name = getSharedPreferences("Login_info",
				Context.MODE_PRIVATE);

		String person = name.getString("name", "");

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Are You Sure You Want to Logout " + person
							+ "? This Will Delete All Your Data.").setTitle(
					"Logout");

			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							clearApplicationData();

							android.os.Process.killProcess(android.os.Process
									.myPid());

						}
					});

			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							dialog.dismiss();
							
							getSupportActionBar().setTitle(mDrawerTitle);

						}
					});

			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}

	}

	public void clearApplicationData() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG",
							"**************** File /data/data/com.bernard.beaconportal/"
									+ s + " DELETED *******************");
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
		
		mActionBar.setTitle(mDrawerTitle);

	}

	@Override
	public void setTitle(CharSequence title) {

		getSupportActionBar().setTitle(title);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		CharSequence title = mDrawerLayout.isDrawerOpen(mDrawerLinear) ? mDrawerTitle
				: mTitle;
		outState.putCharSequence(KEY_STATE_TITLE, title);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (alertDialog != null) {

			alertDialog.dismiss();

		}

		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);

	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Log.d("receiver", "Got");

			mDrawerList.performItemClick(
					mDrawerList.getAdapter().getView(1, null, null), 1,
					mDrawerList.getAdapter().getItemId(1));

			Log.d("receiver", "Got message");

		}
	};

	@Override
	public void onBackPressed() {

		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {

			manager.popBackStack();

		} else {

			super.onBackPressed();
		}
	}

	private void check_download() throws ClientProtocolException, IOException {

		if (AppStatus.getInstance(this).isOnline(this)) {

			new Update().execute();

		} else {

			Toast.makeText(this, "No Internet Connection", 8000).show();
			Log.d("Home", "############################You are not online!!!!");

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

			SharedPreferences.Editor localEditors = getSharedPreferences(
					"last band tommorow", Context.MODE_PRIVATE).edit();

			localEditors.clear();

			localEditors.apply();

			Calendar calendar = Calendar.getInstance();

			int day = calendar.get(Calendar.DAY_OF_WEEK);

			if (day == 6) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar c = Calendar.getInstance();

				c.add(Calendar.DATE, 3);

				date = sdf.format(c.getTime());

				System.out.println("friday");

			} else if (day == 7) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar c = Calendar.getInstance();

				c.add(Calendar.DATE, 2);

				date = sdf.format(c.getTime());

				System.out.println("saturday");

			} else {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar c = Calendar.getInstance();

				c.add(Calendar.DATE, 1);

				date = sdf.format(c.getTime());

			}

			due_tommorow_shared = "due_tommorow" + Integer.toString(shared + 1);

			SharedPreferences.Editor dummy_item = getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE).edit();

			dummy_item.putString("due_tommorow0", "ZZZZZ");

			dummy_item.putString("due_tommorow1", "2");

			dummy_item.putString("due_tommorow2", "Test");

			dummy_item.putString("due_tommorow3", "Teacher");

			dummy_item.putString("due_tommorow4", "Title");

			dummy_item.putString("due_tommorow5", date);

			dummy_item.putString("due_tommorow6", "Type");

			dummy_item.putString("due_tommorow7", "Description");

			dummy_item.apply();

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}
	}

	public void parse_shedule_homework() {

		SharedPreferences schedule_Homework = getSharedPreferences("homework",
				Context.MODE_PRIVATE);

		String Due_schedule = schedule_Homework.getString("homework_content",
				"");

		Due_schedule = Due_schedule.replaceAll("^\"|\"$", "");

		Due_schedule = Due_schedule.substring(7);

		Log.d("homework due schedule", Due_schedule);

		InputStream is = new ByteArrayInputStream(Due_schedule.getBytes());

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

							due_schedule_shared = "due_schedule"
									+ Integer.toString(shared);

							SharedPreferences Band = getSharedPreferences(
									"last band schedule", Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_schedule_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_schedule0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = getSharedPreferences(
									"last band schedule", Context.MODE_PRIVATE);

							SharedPreferences Description = getSharedPreferences(
									due_schedule_shared, Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_schedule7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = getSharedPreferences(
									due_schedule_shared, Context.MODE_PRIVATE)
									.edit();

							localEditor.putString("due_schedule7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = getSharedPreferences(
								"last band schedule", Context.MODE_PRIVATE)
								.edit();

						localEditors.putString("last string", strrr);

						localEditors.apply();

						due_schedule_shared = "due_schedule"
								+ Integer.toString(shared);

						due_schedule_shared_content = "due_schedule"
								+ Integer.toString(shared_add);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = getSharedPreferences(
								due_schedule_shared, Context.MODE_PRIVATE)
								.edit();

						localEditor
								.putString(due_schedule_shared_content, strr);

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

			due_schedule_shared_content = "due_schedule7";

			SharedPreferences.Editor localEditor = getSharedPreferences(
					due_schedule_shared, Context.MODE_PRIVATE).edit();

			localEditor.putString(due_schedule_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getSharedPreferences(
					"due_schedule_counter", Context.MODE_PRIVATE).edit();

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

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			date = sdf.format(c.getTime());

			due_today_shared = "due_tommorow" + Integer.toString(shared + 1);

			SharedPreferences.Editor dummy_item = getSharedPreferences(
					due_today_shared, Context.MODE_PRIVATE).edit();

			dummy_item.putString("due_today0", "ZZZZZ");

			dummy_item.putString("due_today1", "2");

			dummy_item.putString("due_today2", "Test");

			dummy_item.putString("due_today3", "Teacher");

			dummy_item.putString("due_today4", "Title");

			dummy_item.putString("due_today5", date);

			dummy_item.putString("due_today6", "Type");

			dummy_item.putString("due_today7", "Description");

			dummy_item.apply();

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

	protected void onUpdateData(int reason) {
		Log.d(TAG, "onUpdateData(" + reason + ")");
		doRefresh();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		if (contentObserver != null) {
			getContentResolver().unregisterContentObserver(contentObserver);
			contentObserver = null;
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

	}

	protected void doRefresh() {
		Log.d(TAG, "doRefresh()");

		int countssssss = getUnreadMAILCount(this);

		Log.d(TAG, "" + countssssss + " unread emails");

	}

	public static class CursorHandler {
		private List<Cursor> cursors = new ArrayList<Cursor>();

		public Cursor add(Cursor c) {
			if (c != null)
				cursors.add(c);
			return c;
		}

		public void closeAll() {
			for (Cursor c : cursors) {
				if (!c.isClosed())
					c.close();
			}
		}
	}

	private static int mailUnreadCount = 0;

	public static int getUnreadMAILCount(Context context) {
		refreshUnreadMAILCount(context);

		return mailUnreadCount;
	}

	private static int getUnreadMAILCount(Context context, int accountNumber) {
		CursorHandler ch = new CursorHandler();
		try {
			Cursor cur = ch.add(context.getContentResolver().query(
					Uri.parse(mailUnreadUri + "/" + accountNumber + "/"), null,
					null, null, null));
			if (cur != null) {
				Log.d(TAG, "mail: " + cur.getCount() + " unread rows returned");

				if (cur.getCount() > 0) {
					cur.moveToFirst();
					int unread = 0;
					int nameIndex = cur.getColumnIndex("accountName");
					int unreadIndex = cur.getColumnIndex("unread");
					do {
						String acct = cur.getString(nameIndex);
						int unreadForAcct = cur.getInt(unreadIndex);
						Log.d(TAG, "mail: " + acct + " - " + unreadForAcct
								+ " unread");
						unread += unreadForAcct;
					} while (cur.moveToNext());
					cur.close();
					return unread;
				}
			} else {
				Log.d(TAG, "Failed to query mail unread contentprovider.");
			}
		} catch (IllegalStateException e) {
			Log.d(TAG, "Mail unread uri unknown.");
		}
		return 0;
	}

	public static void refreshUnreadMAILCount(Context context) {
		int accounts = getMAILAccountCount(context);
		if (accounts > 0) {
			int countssssss = 0;
			for (int acct = 0; acct < accounts; ++acct) {
				countssssss += getUnreadMAILCount(context, acct);
			}
			mailUnreadCount = countssssss;
		}
	}

	public static int getMAILAccountCount(Context context) {
		CursorHandler ch = new CursorHandler();
		try {
			Cursor cur = ch.add(context.getContentResolver().query(
					mailAccountsUri, null, null, null, null));
			if (cur != null) {
				// if (Preferences.logging) Log.d(MetaWatch.TAG,
				// "mail: "+cur.getCount()+ " account rows returned");

				int count = cur.getCount();

				return count;
			} else {
				// if (Preferences.logging) Log.d(MetaWatch.TAG,
				// "Failed to query mail unread contentprovider.");
			}
		} catch (IllegalStateException e) {
			// if (Preferences.logging) Log.d(MetaWatch.TAG,
			// "Mail accounts uri unknown.");
		} catch (java.lang.SecurityException e) {
			// if (Preferences.logging) Log.d(MetaWatch.TAG,
			// "Permissions failure accessing Mail databases");
		} finally {
			ch.closeAll();
		}
		return 0;

	}

	private void alert_help() {

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.help).setTitle("About");

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							SharedPreferences sharedprefer = getSharedPreferences(
									"first_run_starts", Context.MODE_PRIVATE);

							if (!sharedprefer.contains("help_check")) {

								SharedPreferences.Editor localEditors = getSharedPreferences(
										"first_run_starts",
										Context.MODE_PRIVATE).edit();

								localEditors.putString("help_check", "checked");

								localEditors.commit();

								MainActivity.this.recreate();

							}

						}
					});

			alertDialog = builder.create();

			alertDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					setProgressBarIndeterminateVisibility(Boolean.FALSE);
				}
			});

			alertDialog.show();

		}

	}

	// public void showDatePicker() {
	// // Initializiation
	// LayoutInflater inflater = getLayoutInflater();
	// final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	// View customView = inflater.inflate(R.layout.bdaypicker, null);
	// dialogBuilder.setView(customView);
	// Calendar now = Calendar.getInstance();
	// final DatePicker datePicker = (DatePicker) customView
	// .findViewById(R.id.dialog_datepicker);
	// final TextView dateTextView = (TextView) customView
	// .findViewById(R.id.dialog_dateview);
	// final SimpleDateFormat dateViewFormatter = new SimpleDateFormat(
	// "MM/dd/yyyy");
	//
	// // View settings
	// dialogBuilder.setTitle("Please Enter Your Date of Birth");
	// Calendar choosenDate = Calendar.getInstance();
	// int year = choosenDate.get(Calendar.YEAR);
	// int month = choosenDate.get(Calendar.MONTH);
	// int day = choosenDate.get(Calendar.DAY_OF_MONTH);
	// try {
	//
	// year = 2000;
	// month = 0;
	// day = 1;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Calendar dateToDisplay = Calendar.getInstance();
	// dateToDisplay.set(year, month, day);
	// dateTextView.setText(dateViewFormatter.format(dateToDisplay.getTime()));
	// // Buttons
	// dialogBuilder.setNegativeButton("Go Back",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// dialog.dismiss();
	// }
	// });
	// dialogBuilder.setPositiveButton("Login",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// int year = datePicker.getYear();
	// int month = datePicker.getMonth();
	// int day = datePicker.getDayOfMonth();
	//
	// SharedPreferences.Editor localEditor = getSharedPreferences(
	// "Login_Info", Context.MODE_PRIVATE).edit();
	//
	// localEditor.putInt("Day", day);
	//
	// localEditor.putInt("Year", year);
	//
	// localEditor.putInt("Month", month);
	//
	// localEditor.apply();
	//
	// dialog.dismiss();
	//
	// bday_check_dialog();
	// }
	// });
	// final AlertDialog dialog = dialogBuilder.create();
	// // Initialize datepicker in dialog atepicker
	// datePicker.init(year, month, day,
	// new DatePicker.OnDateChangedListener() {
	// @Override
	// public void onDateChanged(DatePicker view, int year,
	// int monthOfYear, int dayOfMonth) {
	// Calendar choosenDate = Calendar.getInstance();
	// choosenDate.set(year, monthOfYear, dayOfMonth);
	// dateTextView.setText(dateViewFormatter
	// .format(choosenDate.getTime()));
	//
	// dateTextView.setTextColor(Color.parseColor("#58585b"));
	// dialog.getButton(DialogInterface.BUTTON_POSITIVE)
	// .setEnabled(true);
	// }
	//
	// });
	//
	// dialog.show();
	// }
	//
	// private void bday_check_dialog() {
	// {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//
	// SharedPreferences bDay = getSharedPreferences("Login_Info",
	// Context.MODE_PRIVATE);
	//
	// String day1 = Integer.toString(bDay.getInt("Day", 0));
	//
	// String year1 = Integer.toString(bDay.getInt("Year", 0));
	//
	// String month1 = Integer.toString(1 + bDay.getInt("Month", 0));
	//
	// SharedPreferences userName = getSharedPreferences("Login_Info",
	// Context.MODE_PRIVATE);
	//
	// String day = day1.replaceFirst("^0+(?!$)", "");
	//
	// String month = month1.replaceFirst("^0+(?!$)", "");
	//
	// String year = year1.replaceFirst("^0+(?!$)", "");
	//
	// String birthday = month + "/" + day + "/" + year;
	//
	// builder.setTitle("Are You Sure " + birthday
	// + " Is Your Actual Birthday?");
	//
	// builder.setMessage("If this isn't your real birthday, you won't be able to receive homework through the app. We need your birthday so we can tell if you are really you. So are you sure "
	// + birthday + " is your actual birthday?");
	//
	// builder.setPositiveButton("No",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int id) {
	//
	// showDatePicker();
	//
	// }
	// });
	// builder.setNegativeButton("Yes",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int id) {
	//
	// }
	// });
	//
	// AlertDialog alertDialog = builder.create();
	//
	// alertDialog.show();
	//
	// }
	// }

	private void setAlarm(Context context) {

		Intent intent = new Intent(context, MidnightHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				RQS_1, intent, 0);

		Calendar calNow = Calendar.getInstance();
		Calendar targetCal = (Calendar) calNow.clone();

		targetCal.set(Calendar.HOUR_OF_DAY, 0);
		targetCal.set(Calendar.MINUTE, 0);
		targetCal.set(Calendar.SECOND, 0);
		targetCal.set(Calendar.MILLISECOND, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
				pendingIntent);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);

		System.out.println("midnight");

	}

	private void setAlarmCustom(Context context, int hour, int minute) {

		Calendar calNow = Calendar.getInstance();
		Calendar targetCal = (Calendar) calNow.clone();

		targetCal.set(Calendar.HOUR_OF_DAY, hour);
		targetCal.set(Calendar.MINUTE, minute);
		targetCal.set(Calendar.SECOND, 0);
		targetCal.set(Calendar.MILLISECOND, 0);

		Intent intent = new Intent(context, DailyHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				RQS_1, intent, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);

		System.out.println(targetCal);

		System.out.println("alarm reset three");

	}

}