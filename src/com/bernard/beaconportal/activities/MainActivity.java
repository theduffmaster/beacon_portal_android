package com.bernard.beaconportal.activities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.setup.AccountSetupBasics;

@SuppressLint("ResourceAsColor")
public class MainActivity extends SherlockFragmentActivity {
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
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String actionbar_colors, background_colorsString;
	private String Show_View;
	String[] title;
	String[] count;
	int[] icon;
	private String counterss;
	private int counters;
	Fragment fragment1 = new FragmentsView();
	Fragment fragment2 = new FragmentsHomeworkDue();
	Fragment fragment3 = new FragmentSettings();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private int starts = 0;
	
	private BaseAccount mSelectedContextAccount;

	private int shared1;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		Log.d(TAG, "onCreate()");

		String packageName = "com.bernard.beaconportal.activities";

		counterss = "0";
		
		int versionNumber = 0;

		try {
			PackageInfo pi = getApplicationContext().getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionNumber = pi.versionCode;
			String versionName = pi.versionName;

			Log.d(TAG, "K-9 is installed - " + versionNumber + " "
					+ versionName);

		} catch (NameNotFoundException e) {
			Log.d(TAG, "K-9 not found");
		}

		if (versionNumber <= 1) {
			// Register a listener for broadcasts (needed for the older versions
			// of k9)
			Log.d(TAG, "Initialising BroadcastReceiver for old K-9 version");
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
			Log.d(TAG, "Initialising ContentObserver for new K-9 version");
			contentObserver = new ContentObserver(null) {
				@Override
				public void onChange(boolean selfChange) {
					Log.d(TAG, "contentResolver.onChange()");
					doRefresh();
				}
			};
			getContentResolver().registerContentObserver(
					Uri.parse(k9UnreadUri), true, contentObserver);
		}

		
		
		
		
		doRefresh();

		int countssssss = getUnreadK9Count(this);

		K9count = Integer.toString(countssssss);

		System.out.println("k9 Unread Count = " + countssssss);

		
		
		SharedPreferences sharedprefers = getSharedPreferences("first_run",
				Context.MODE_PRIVATE);

		if (sharedprefers.contains("first_run")) {

		} else {

			SharedPreferences.Editor localEditor = getSharedPreferences(
					"first_run", Context.MODE_PRIVATE).edit();

			localEditor.putString("first_run", "ran for the first time");

			localEditor.commit();

			new Update().execute();

		}

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

		if (!sharedpref.contains("actionbar_color")) {

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#03a9f4")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		ActionBar bar = getSupportActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		mTitle = mDrawerTitle = getTitle();

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("due_today", Context.MODE_PRIVATE);

		if (Today_Homework.contains("duetoday_content")) {

			parse_due_today_string();

		} else {

			counterss = "0";

		}

		SharedPreferences.Editor localEditor = getSharedPreferences(
				"due_today", Context.MODE_PRIVATE).edit();

		localEditor.putString("inbox", K9count);
		localEditor.putString("homeworkdue", counterss);

		localEditor.apply();

		if (Show_View.equals("Homework Due")) {

			title = new String[] { 
					//"Homework Due",
					"Schedule", "Unread Mail",
					"Options", "Logout" };

			icon = new int[] { 
					//R.drawable.ic_action_duehomework,
					R.drawable.ic_action_go_to_today,
					R.drawable.ic_action_email, R.drawable.ic_action_settings,
					R.drawable.ic_action_logout };

			if (counterss == null && counterss.isEmpty()) {

				count = new String[] { 
						//"",
						"", K9count, "", "" };

			} else {

				count = new String[] { 
						//counterss, 
						"", K9count, "", "", "" };

			}

		} else {
			
			if (counterss == null && counterss.isEmpty()) {

				count = new String[] { 
						//"", 
						"", K9count, "", "" };

			} else {

				count = new String[] { "", 
						//counterss, 
						K9count, "", "" };

			}

			title = new String[] { "Schedule", 
					//"Homework Due", 
					"Unread Mail",
					"Options", "Logout" };

			icon = new int[] { R.drawable.ic_action_go_to_today,
					//R.drawable.ic_action_duehomework,
					R.drawable.ic_action_email, R.drawable.ic_action_settings,
					R.drawable.ic_action_logout };

		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		SharedPreferences name = getSharedPreferences("Login_info",
				Context.MODE_PRIVATE);

		String person = name.getString("name", "");

		mWelcomePerson = (TextView) findViewById(R.id.Person);

		mWelcomePerson.setText(person);

		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon,
				count);

		mDrawerList.setAdapter(mMenuAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

				getSupportActionBar().setTitle(mDrawerTitle);
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

				}

				counts.edit().remove("fragment_to_start").commit();

			} else {

				if (savedInstanceState == null) {
					selectItem(0);
				}

			}

		} else {

			if (savedInstanceState == null) {
				selectItem(0);
			}

		}

		
		
		SharedPreferences sharedprefer = getSharedPreferences("first_run_starts",
				Context.MODE_PRIVATE);

			
		
			
			
		if(sharedprefer.contains("first_run_starts")){
			
			
			
			
		}else{
		
			SharedPreferences.Editor localEditors = getSharedPreferences(
					"first_run_starts", Context.MODE_PRIVATE).edit();

			localEditors.putString("first_run_starts", "yes");

			localEditors.commit();
			
		Intent intent = new Intent(this, AccountSetupBasics.class);
		startActivity(intent);
		
		}
		
		SharedPreferences sharedpreferences = getSharedPreferences("first_run_starter",
				Context.MODE_PRIVATE);

			
		SharedPreferences sharedprefererence = getSharedPreferences("Login_info",
				Context.MODE_PRIVATE);
			
			
		if(sharedpreferences.contains("first_run_starts")){
			
		}else{
		
		
		
		if (sharedprefererence.contains("name")) {

			
			SharedPreferences.Editor localEditors = getSharedPreferences(
					"first_run_starter", Context.MODE_PRIVATE).edit();

			localEditors.putString("first_run_starts", "yes");

			localEditors.commit();
			
			alert_help();
			
		} 
		
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d("onResume", "yes");

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

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("due_today", Context.MODE_PRIVATE);

		if (Today_Homework.contains("duetoday_content")) {

			parse_due_today_string();

		} else {

			counterss = "";

		}

		String packageName = "com.bernard.beaconportal.activities";

		int versionNumber = 0;

		try {
			PackageInfo pi = getApplicationContext().getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionNumber = pi.versionCode;
			String versionName = pi.versionName;

			Log.d(TAG, "K-9 is installed - " + versionNumber + " "
					+ versionName);

		} catch (NameNotFoundException e) {
			Log.d(TAG, "K-9 not found");
		}

		if (versionNumber <= 16024) {
			// Register a listener for broadcasts (needed for the older versions
			// of k9)
			Log.d(TAG, "Initialising BroadcastReceiver for old K-9 version");
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
			Log.d(TAG, "Initialising ContentObserver for new K-9 version");
			contentObserver = new ContentObserver(null) {
				@Override
				public void onChange(boolean selfChange) {
					Log.d(TAG, "contentResolver.onChange()");
					doRefresh();
				}
			};
			getContentResolver().registerContentObserver(
					Uri.parse(k9UnreadUri), true, contentObserver);
		}

		doRefresh();

		int countssssss = getUnreadK9Count(this);

		K9count = Integer.toString(countssssss);

		System.out.println("k9 Unread Count = " + countssssss);

	}

	public void inbox() {

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

			SharedPreferences sharedpreferss = getSharedPreferences(
					"due_tommorow_counter", Context.MODE_PRIVATE);

			while (!sharedpreferss.contains("last shared preference")) {

				System.out.println("Internet is not working, still looping");

				if (AppStatus.getInstance(getApplicationContext()).isOnline(
						getApplicationContext())) {

					new Update_due_today().execute();

					System.out.println("INTERNET WORKED!");

					break;

				}

			}
			return null;
		}

	}

	public class Update extends AsyncTask<String, Void, Void> {

		private final HttpClient Client = new DefaultHttpClient();

		@Override
		protected Void doInBackground(String... urls) {
			try {

				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet(
						"http://www.beaconschool.org/~markovic/lincoln.php");
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				String result = "";

				try {
					Log.d("receiver", "animation stopped and downloaded file");

					String duetoday_html = new Scanner(response.getEntity()
							.getContent(), "UTF-8").useDelimiter("\\A").next();

					String duetoday = Html.fromHtml(duetoday_html).toString();

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"due_today", Context.MODE_PRIVATE).edit();

					localEditor.putString("duetoday_content", duetoday);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} finally {

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				check_download();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent intent = getIntent();
			overridePendingTransition(0, 0);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			finish();

			overridePendingTransition(0, 0);
			startActivity(intent);
		}

	}

	public void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void parse_due_today_string() {

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("due_today", Context.MODE_PRIVATE);

		String Due_Today = Today_Homework.getString("duetoday_content", "");

		ArrayList<String> description = new ArrayList<String>();

		StringBuilder DescriptionAll = new StringBuilder();

		InputStream is = new ByteArrayInputStream(Due_Today.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {

			int i = 0;

			while ((Due_Today = reader.readLine()) != null) {
				String[] part = Due_Today.split("\",\"", -1);
				int noOfItems = part.length;
				int counter = 0;

				DescriptionCheck = counter < noOfItems ? part[counter] : "";
				counter++;
				Band = counter < noOfItems ? part[counter] : "";
				counter++;
				Number = counter < noOfItems ? part[counter] : "";
				counter++;
				Class = counter < noOfItems ? part[counter] : "";
				counter++;
				Teacher = counter < noOfItems ? part[counter] : "";
				counter++;
				Title = counter < noOfItems ? part[counter] : "";
				counter++;
				Date = counter < noOfItems ? part[counter] : "";
				counter++;
				Type = counter < noOfItems ? part[counter] : "";
				counter++;
				Data = counter < noOfItems ? part[counter] : "";
				counter++;

				if (Type != null && !Type.isEmpty()) {

					counters = ++i;

					counterss = Integer.toString(counters);

					Log.i("Counter", "Number of children: " + counters);

				}

				Log.d("Type", Type);

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (Show_View.equals("Homework Due")) {

			switch (position) {
//			case 0:
//				ft.replace(R.id.content_frame, fragment2);
//				break;
			case 0:
				ft.replace(R.id.content_frame, fragment1);
				break;
			case 1:

				inbox();

				break;
			case 2:
				ft.replace(R.id.content_frame, fragment3);
				break;

			case 3:

				alert_logout();

				break;
			}

		} else {

			switch (position) {
			case 0:
				ft.replace(R.id.content_frame, fragment1);
				break;
//			case 1:
//
//				SharedPreferences sharedprefers = getSharedPreferences(
//						"due_tommorow_counter", Context.MODE_PRIVATE);
//
//				if (!sharedprefers.contains("last shared preference")) {
//
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//				if (!sharedprefers.contains("last shared preference")) {
//
//					Toast.makeText(this, "Please Connect to the Internet!",
//							8000).show();
//					Log.d("Home",
//							"############################You are not online!!!!");
//
//				} else {
//
//					ft.replace(R.id.content_frame, fragment2);
//
//				}
//
//				break;

			case 1:

				inbox();

				break;
			case 2:
				ft.replace(R.id.content_frame, fragment3);
				break;

			case 3:

				alert_logout();

				break;
			}
		}

		ft.commit();

		mDrawerList.setItemChecked(position, true);

		setTitle(title[position]);

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
							"**************** File /data/data/APP_PACKAGE/" + s
									+ " DELETED *******************");
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
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);

	}

	@Override
	protected void onPause() {
		super.onPause();

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

			new Update_due_today().execute();

		} else {

			Toast.makeText(this, "No Internet Connection", 8000).show();
			Log.d("Home", "############################You are not online!!!!");

		}

	}

	public void parse_due_today_string_content() {

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("due_today", Context.MODE_PRIVATE);

		String Due_Today = Today_Homework.getString("duetoday_content", "");

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

	public class Update_due_today extends AsyncTask<String, Void, Void> {

		private final HttpClient Client = new DefaultHttpClient();

		@Override
		protected Void doInBackground(String... urls) {
			try {

				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet(
						"http://www.beaconschool.org/~markovic/lincoln.php");
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				String result = "";

				try {
					Log.d("receiver", "animation stopped and downloaded file");

					String duetoday_html = new Scanner(response.getEntity()
							.getContent(), "UTF-8").useDelimiter("\\A").next();

					String duetoday = Html.fromHtml(duetoday_html).toString();

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"due_today", Context.MODE_PRIVATE).edit();

					localEditor.putString("duetoday_content", duetoday);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

					parse_due_today_string_content();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} finally {

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			new Update_due_tommorow().execute();

		}

	}

	public void parse_due_tommorow_string() {

		SharedPreferences Today_Homework = getApplicationContext()
				.getSharedPreferences("due_tommorow", Context.MODE_PRIVATE);

		String due_tommorow = Today_Homework.getString("duetoday_content", "");

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

			System.out.println("shared_pref_final= " + strr);

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

	public class Update_due_tommorow extends AsyncTask<String, Void, Void> {

		private final HttpClient Client = new DefaultHttpClient();

		@Override
		protected Void doInBackground(String... urls) {
			try {

				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet(
						"http://www.beaconschool.org/~markovic/lincoln.php");
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				String result = "";

				try {
					Log.d("receiver", "animation stopped and downloaded file");

					String duetoday_html = new Scanner(response.getEntity()
							.getContent(), "UTF-8").useDelimiter("\\A").next();

					String duetoday = Html.fromHtml(duetoday_html).toString();

					SharedPreferences.Editor localEditor = getSharedPreferences(
							"due_tommorow", Context.MODE_PRIVATE).edit();

					localEditor.putString("duetoday_content", duetoday);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

					parse_due_tommorow_string();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} finally {

			}
			return null;
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

		int countssssss = getUnreadK9Count(this);

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

	private static int k9UnreadCount = 0;

	public static int getUnreadK9Count(Context context) {
		refreshUnreadK9Count(context);

		return k9UnreadCount;
	}

	private static int getUnreadK9Count(Context context, int accountNumber) {
		CursorHandler ch = new CursorHandler();
		try {
			Cursor cur = ch.add(context.getContentResolver().query(
					Uri.parse(k9UnreadUri + "/" + accountNumber + "/"), null,
					null, null, null));
			if (cur != null) {
				Log.d(TAG, "k9: " + cur.getCount() + " unread rows returned");

				if (cur.getCount() > 0) {
					cur.moveToFirst();
					int unread = 0;
					int nameIndex = cur.getColumnIndex("accountName");
					int unreadIndex = cur.getColumnIndex("unread");
					do {
						String acct = cur.getString(nameIndex);
						int unreadForAcct = cur.getInt(unreadIndex);
						Log.d(TAG, "k9: " + acct + " - " + unreadForAcct
								+ " unread");
						unread += unreadForAcct;
					} while (cur.moveToNext());
					cur.close();
					return unread;
				}
			} else {
				Log.d(TAG, "Failed to query k9 unread contentprovider.");
			}
		} catch (IllegalStateException e) {
			Log.d(TAG, "k-9 unread uri unknown.");
		}
		return 0;
	}

	public static void refreshUnreadK9Count(Context context) {
		int accounts = getK9AccountCount(context);
		if (accounts > 0) {
			int countssssss = 0;
			for (int acct = 0; acct < accounts; ++acct) {
				countssssss += getUnreadK9Count(context, acct);
			}
			k9UnreadCount = countssssss;
		}
	}

	public static int getK9AccountCount(Context context) {
		CursorHandler ch = new CursorHandler();
		try {
			Cursor cur = ch.add(context.getContentResolver().query(
					k9AccountsUri, null, null, null, null));
			if (cur != null) {
				// if (Preferences.logging) Log.d(MetaWatch.TAG,
				// "k9: "+cur.getCount()+ " account rows returned");

				int count = cur.getCount();

				return count;
			} else {
				// if (Preferences.logging) Log.d(MetaWatch.TAG,
				// "Failed to query k9 unread contentprovider.");
			}
		} catch (IllegalStateException e) {
			// if (Preferences.logging) Log.d(MetaWatch.TAG,
			// "k-9 accounts uri unknown.");
		} catch (java.lang.SecurityException e) {
			// if (Preferences.logging) Log.d(MetaWatch.TAG,
			// "Permissions failure accessing k-9 databases");
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

						}
					});

			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}

	}
	
}
