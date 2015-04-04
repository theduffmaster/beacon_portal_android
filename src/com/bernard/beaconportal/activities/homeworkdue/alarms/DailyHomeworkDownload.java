package com.bernard.beaconportal.activities.homeworkdue.alarms;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import com.astuetz.PagerSlidingTabStripMargins;
import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.drawable;
import com.bernard.beaconportal.activities.homeworkdue.FragmentsHomeworkDue;
import com.bernard.beaconportal.activities.schedule.view.FragmentsSchedule;
import com.bernard.beaconportal.activities.settings.FragmentSettings;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatExtras;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DailyHomeworkDownload extends BroadcastReceiver {

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
	public static int homeworkCount;

	Notification n;
	public static ArrayList<String> due_tommorow_list;

	private HttpResponse response;

	private String due_tommorow_shared, due_tommorow_shared_content;

	private int shared;

	private Context context;

	private Activity activityContext;

	private static String date;

	private static String day_due;

	public DailyHomeworkDownload() {
		super();
	}

	@Override
	public void onReceive(Context receive_context, Intent intent) {

		due_tommorow_list = new ArrayList<String>();

		this.activityContext = activityContext;

		Log.d("Beacon Portal", "alarm activated daily");

		context = receive_context;

		new Update().execute();

	}

	public class Update extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... urls) {

			SharedPreferences bDay = context.getSharedPreferences("Login_Info",
					Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = context.getSharedPreferences(
					"Login_Info", Context.MODE_PRIVATE);

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

					SharedPreferences.Editor localEditor = context
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd hh:mm a");
					Calendar cal = Calendar.getInstance();
					String downloaded = dateFormat.format(cal.getTime());

					localEditor.putString("homework_content", homework);

					localEditor.putString("download_date", downloaded);

					localEditor.apply();

					SharedPreferences.Editor log = context
							.getSharedPreferences("AlarmDownload",
									Context.MODE_PRIVATE).edit();

					log.putString("date", downloaded);

					log.apply();

					Log.d("receiver", "information given to shared preferences");

					parse_due_tommorow_string();

					parse_due_today_string();

					parse_due_tommorow_content();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					createNotification();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {

					parse_due_tommorow_string();

					parse_due_today_string();

					parse_due_tommorow_content();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					createNotification();

					SharedPreferences.Editor localEditor = context
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (NoSuchElementException e) {

					parse_due_tommorow_string();

					parse_due_today_string();

					parse_due_tommorow_content();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					createNotification();

					SharedPreferences.Editor localEditor = context
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (RuntimeException e) {
					parse_due_tommorow_string();

					parse_due_today_string();

					parse_due_tommorow_content();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					createNotification();

					SharedPreferences.Editor localEditor = context
							.getSharedPreferences("homework",
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

			Log.d("sender", "Broadcasting message");

			Intent intent = new Intent("up_navigation");

			intent.putExtra("message", "This is my message!");
			LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

			SharedPreferences download_error = context.getSharedPreferences(
					"homework", Context.MODE_PRIVATE);

			String error = download_error.getString("download_error", "no");

			String download_date = "Download error, refreshed homework using homework downloaded at "
					+ download_error.getString("download_date", "");

			if (error.equals("yes")) {

				SharedPreferences.Editor localEditor = context
						.getSharedPreferences("homework", Context.MODE_PRIVATE)
						.edit();

				Toast.makeText(context, download_date, Toast.LENGTH_LONG)
						.show();

				localEditor.putString("download_error", "no");

				localEditor.commit();

			}

			System.out.println("Homework List For Update = "
					+ due_tommorow_list);

			// parse_due_tommorow_content();
			//
			// try{
			//
			// createNotification();
			//
			// }catch (IndexOutOfBoundsException e) {
			//
			// parse_due_tommorow_content();
			//
			// createNotification();
			//
			// }
			//
		}

	}

	public void parse_due_tommorow_string() {

		homeworkCount = 0;

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

		SharedPreferences Tommorow_Homework = context.getSharedPreferences(
				"homework", Context.MODE_PRIVATE);

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

							SharedPreferences Band = context
									.getApplicationContext()
									.getSharedPreferences("last band tommorow",
											Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = context
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_tommorow0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = context
									.getSharedPreferences("last band tommorow",
											Context.MODE_PRIVATE);

							SharedPreferences Description = context
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_tommorow7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = context
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_tommorow7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = context
								.getSharedPreferences("last band tommorow",
										Context.MODE_PRIVATE).edit();

						localEditors.putString("last string", strrr);

						localEditors.apply();

						due_tommorow_shared = "due_tommorow"
								+ Integer.toString(shared);

						due_tommorow_shared_content = "due_tommorow"
								+ Integer.toString(shared_add);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = context
								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

						localEditor
								.putString(due_tommorow_shared_content, strr);

						localEditor.apply();

						if (strr.equals(date)) {

							homeworkCount++;

							System.out.println("Homework Count= "
									+ homeworkCount);

						}

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

			SharedPreferences.Editor localEditor = context
					.getSharedPreferences(due_tommorow_shared,
							Context.MODE_PRIVATE).edit();

			localEditor.putString(due_tommorow_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = context
					.getSharedPreferences("due_tommorow_counter",
							Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

			SharedPreferences.Editor localEditors = context
					.getSharedPreferences("last band tommorow",
							Context.MODE_PRIVATE).edit();

			localEditors.clear();

			localEditors.apply();

			due_tommorow_shared = "due_tommorow" + Integer.toString(shared + 1);

			SharedPreferences.Editor dummy_item = context.getSharedPreferences(
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

			System.out.println("Homework Count= " + homeworkCount);

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}
	}

	public void parse_due_today_string() {

		SharedPreferences Today_Homework = context.getSharedPreferences(
				"homework", Context.MODE_PRIVATE);

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

							SharedPreferences Band = context
									.getSharedPreferences("last band today",
											Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = context
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_today0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = context
									.getSharedPreferences("last band today",
											Context.MODE_PRIVATE);

							SharedPreferences Description = context
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_today7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = context
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_today7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = context
								.getSharedPreferences("last band today",
										Context.MODE_PRIVATE).edit();

						localEditors.putString("last string", strrr);

						localEditors.apply();

						due_today_shared = "due_today"
								+ Integer.toString(shared);

						due_today_shared_content = "due_today"
								+ Integer.toString(shared_add);

						String strr = strb.toString().replaceAll("^\"|\"$", "");

						System.out.println("shared_pref= " + strr);

						SharedPreferences.Editor localEditor = context
								.getSharedPreferences(due_today_shared,
										Context.MODE_PRIVATE).edit();

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

			SharedPreferences.Editor localEditor = context
					.getSharedPreferences(due_today_shared,
							Context.MODE_PRIVATE).edit();

			localEditor.putString(due_today_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = context
					.getSharedPreferences("due_today_counter",
							Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			date = sdf.format(c.getTime());

			due_today_shared = "due_tommorow" + Integer.toString(shared + 1);

			SharedPreferences.Editor dummy_item = context.getSharedPreferences(
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

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
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

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	public static float pxFromDp(final Context context, final float dp) {
	    return dp * context.getResources().getDisplayMetrics().density;
	}

	public void parse_due_tommorow_content() {

		Calendar calendar = Calendar.getInstance();

		int day = calendar.get(Calendar.DAY_OF_WEEK);

		if (day == 6) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 3);

			date = sdf.format(c.getTime());

			day_due = "Monday";

			System.out.println("friday");

		} else if (day == 7) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 2);

			date = sdf.format(c.getTime());

			day_due = "Monday";

			System.out.println("saturday");

		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();

			c.add(Calendar.DATE, 1);

			date = sdf.format(c.getTime());

			day_due = "Tommorow";

		}

		System.out.println("Date = " + date);

		SharedPreferences Tommorow_Homework_Counter = context
				.getSharedPreferences("due_tommorow_counter",
						Context.MODE_PRIVATE);

		int counterssss = Tommorow_Homework_Counter.getInt(
				"last shared preference", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_tommorow_shared = "due_tommorow" + Integer.toString(i);

			System.out.println("due_tommorow = " + due_tommorow_shared);

			SharedPreferences Todays_Homework = context.getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE);

			String Band1 = Todays_Homework.getString("due_tommorow0", "");

			String Number1 = Todays_Homework.getString("due_tommorow1", "");

			String Class1 = Todays_Homework.getString("due_tommorow2", "");

			String Teacher1 = Todays_Homework.getString("due_tommorow3", "");

			String Title1 = Todays_Homework.getString("due_tommorow4", "");

			String Date1 = Todays_Homework.getString("due_tommorow5", "");

			String Type1 = Todays_Homework.getString("due_tommorow6", "");

			String Description1 = Todays_Homework
					.getString("due_tommorow7", "");

			SharedPreferences description_check = context.getSharedPreferences(
					"descriptioncheck", Context.MODE_PRIVATE);

			String descriptionCheck = description_check.getString(
					"description", "");

			System.out.println("DateCheck =" + Date1);

			System.out.println("datecheck =" + date);

			if (Date1.contentEquals(date) && Date1 != null) {

				SharedPreferences.Editor checkeditor = context

				.getSharedPreferences("descriptioncheck", Context.MODE_PRIVATE)
						.edit();

				checkeditor.putString("description", Description1);

				checkeditor.commit();

				if (!"Type".equals(Type1)) {

					due_tommorow_list.add(Title1);

					System.out.println("Homework Title For Notification = "
							+ due_tommorow_list);

				}

			}

		}

	}
	
	public Bitmap getResizedBitmap(Bitmap bm, float f, float g) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) f) / width;
	    float scaleHeight = ((float) g) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}

	public void createNotification() {
		// prepare intent which is triggered if the
		// notification is selected

		System.out
				.println("Homework Count For Notification = " + homeworkCount);

		System.out.println("Homework List For Notification = "
				+ due_tommorow_list);

		SharedPreferences sharedpre = context.getSharedPreferences("show_view",
				Context.MODE_PRIVATE);

		String Show_View = sharedpre.getString("show_view", "");
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (Show_View.equals("Homework Due")) {

			SharedPreferences.Editor localEditor1 = context
					.getSharedPreferences("return_to_main",
							Context.MODE_PRIVATE).edit();

			localEditor1.putString("fragment_to_start", "0");

			localEditor1.commit();

		} else {

			SharedPreferences.Editor localEditor1 = context
					.getSharedPreferences("return_to_main",
							Context.MODE_PRIVATE).edit();

			localEditor1.putString("fragment_to_start", "1");

			localEditor1.commit();

		}

		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT
								| PendingIntent.FLAG_ONE_SHOT);

		SharedPreferences sharedprefer = context.getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

		}

		// build notification

		if (homeworkCount == 0) {

			Bitmap icon0 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon0);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon0.getWidth(),
					icon0.getHeight(), icon0.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon0, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, pxFromDp(context, 65), pxFromDp(context, 65));
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("No Homework Due Tommorow!")
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText("Congratulations")
					.setContentIntent(pIntent).setAutoCancel(true).build();

		} else if (homeworkCount == 1) {

			Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon1);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon1.getWidth(),
					icon1.getHeight(), icon1.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon1, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("1 Assignment Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0)))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 2) {

			Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon2);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon2.getWidth(),
					icon2.getHeight(), icon2.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon2, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("2 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle().addLine(
									due_tommorow_list.get(0)).addLine(
									due_tommorow_list.get(1)))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 3) {

			Bitmap icon3 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon3);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon3.getWidth(),
					icon3.getHeight(), icon3.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon3, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("3 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2)))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 4) {

			Bitmap icon4 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon4);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon4.getWidth(),
					icon4.getHeight(), icon4.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon4, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("4 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3)))
					.setContentIntent(pIntent)

					.setAutoCancel(true).build();

		} else if (homeworkCount == 5) {

			Bitmap icon5 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon5);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon5.getWidth(),
					icon5.getHeight(), icon5.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon5, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("5 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4)))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 6) {

			Bitmap icon6 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon6);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon6.getWidth(),
					icon6.getHeight(), icon6.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon6, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("6 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4))
									.setSummaryText("+1 more"))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 7) {

			Bitmap icon7 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon7);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon7.getWidth(),
					icon7.getHeight(), icon7.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon7, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("7 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4))
									.setSummaryText("+2 more"))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 8) {

			Bitmap icon8 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon8);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon8.getWidth(),
					icon8.getHeight(), icon8.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon8, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("8 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4))
									.setSummaryText("+3 more"))
					.setAutoCancel(true).build();

		} else if (homeworkCount == 9) {

			Bitmap icon9 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon9);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon9.getWidth(),
					icon9.getHeight(), icon9.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon9, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("9 Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4))
									.setSummaryText("+4 more"))
					.setAutoCancel(true).build();

		} else if (homeworkCount > 9) {

			Bitmap icon10 = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.icon10);

			Bitmap coloredBitmap = Bitmap.createBitmap(icon10.getWidth(),
					icon10.getHeight(), icon10.getConfig());
			Canvas canvas = new Canvas(coloredBitmap);
			canvas.drawColor(Color.parseColor(actionbar_colors));
			canvas.drawBitmap(icon10, 0, 0, null);
			
			if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			    coloredBitmap = getCroppedBitmap(coloredBitmap);
			} else{
			    
				coloredBitmap = getResizedBitmap(coloredBitmap, 130, 130);
				
			}

			String howManyMore = "+" + Integer.toString(homeworkCount - 5)
					+ " more";

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor("#607D8B"))
					.setContentTitle("10 or More Assignments Due " + day_due)
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setLargeIcon(coloredBitmap)
					.setContentText(due_tommorow_list.get(0))
					.setContentIntent(pIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(due_tommorow_list.get(0))
									.addLine(due_tommorow_list.get(1))
									.addLine(due_tommorow_list.get(2))
									.addLine(due_tommorow_list.get(3))
									.addLine(due_tommorow_list.get(4))
									.setSummaryText(howManyMore))
					.setAutoCancel(true).build();

		} else {

			n = new NotificationCompat.Builder(context)
					.setColor(Color.parseColor(actionbar_colors))
					.setContentTitle("Homework Due Tommorow")
					.setSmallIcon(R.drawable.ic_action_assignment_light)
					.setContentText("Subject").setContentIntent(pIntent)
					.setAutoCancel(true).build();

		}

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(1, n);

	}
}