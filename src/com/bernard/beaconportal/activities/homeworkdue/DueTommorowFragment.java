package com.bernard.beaconportal.activities.homeworkdue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.AppStatus;
import com.bernard.beaconportal.activities.R;
import com.bumptech.glide.Glide;

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

import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;

public class DueTommorowFragment extends Fragment {

	private Activity mActivity;

	private List<DueTodayList> due_tommorow_list;

	private View swipe;

	private String actionbar_colors;

	private static String date;

	private int shared;

	public static EnhancedListView lView;

	public View footer;

	public TextView footer_text;

	private SharedPreferences Tommorow_Homework_Counter;

	private SharedPreferences Tommorow_Homework;

	private SharedPreferences Add_Homework_Counter;

	private SharedPreferences ThemeColor;

	private TextView addHomework;

	private static ArrayAdapter<DueTodayList> adapter;

	private String Band, Number, Class, Teacher, Title, Date, Type,
			Description, due_tommorow_shared, due_tommorow_shared_content;

	static class ViewHolder {
		public ImageView imageView;
		public TextView HomeworkDueText;
		public TextView DescriptionText;
		public TextView TeacherText;
		public TextView TypeText;
	}

	SwipeRefreshLayout swipeLayout;

	HttpResponse response;

	public static final String KEY_HOMEWORK = "homework";
	public static final String KEY_DESC = "desc";
	public static final String KEY_DATE = "date";
	public static final String KEY_TYPE = "type";
	public static final String KEY_BAND = "band";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Tommorow_Homework_Counter = getActivity().getSharedPreferences(
				"due_tommorow_counter", Context.MODE_PRIVATE);

		Add_Homework_Counter = getActivity().getSharedPreferences(
				"add_homework_counter", Context.MODE_PRIVATE);

		Tommorow_Homework = getActivity().getSharedPreferences("homework",
				Context.MODE_PRIVATE);

		new Download().execute();

		swipe = inflater.inflate(R.layout.activity_main, container, false);

		swipeLayout = (SwipeRefreshLayout) swipe.findViewById(R.id.swipe);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

		}

		swipeLayout.setEnabled(false);

		swipeLayout.setColorSchemeResources(android.R.color.holo_orange_light,
				android.R.color.holo_blue_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_blue_light);

		lView = (EnhancedListView) swipe.findViewById(R.id.listView1);

		footer = getActivity().getLayoutInflater().inflate(
				R.layout.homeworkadd_footer, null);

		footer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showNoteDialog();

			}
		});

		footer_text = (TextView) footer.findViewById(R.id.textView1);

		ThemeColor = getActivity().getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!ThemeColor.contains("actionbar_color")) {

			footer_text.setTextColor(Color.parseColor("#4285f4"));

		} else {

			actionbar_colors = ThemeColor.getString("actionbar_color", null);

			footer_text.setTextColor(Color.parseColor(actionbar_colors));
		}

		lView.addFooterView(footer);

		lView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int i) {

			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0)
					swipeLayout.setEnabled(true);
				else
					swipeLayout.setEnabled(false);
			}
		});

		swipeLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						swipeLayout.setRefreshing(true);

						try {

							check_download();

						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						(new Handler()).postDelayed(new Runnable() {

							@Override
							public void run() {
								swipeLayout.setRefreshing(false);

							}

						},

						1000000);

					}

					private void check_download()
							throws ClientProtocolException, IOException {

						if (AppStatus.getInstance(getActivity()).isOnline(
								getActivity())) {

							new Update().execute();

						} else {

							SharedPreferences downloaded_date = getActivity()
									.getSharedPreferences("homework",
											Context.MODE_PRIVATE);

							String download_date = "No internet connection, refreshing homework using homework downloaded at "
									+ downloaded_date.getString(
											"download_date", "");

							String downloaded = "refreshed homework using homework downloaded at "
									+ downloaded_date.getString(
											"download_date", "");

							Toast.makeText(getActivity(), download_date,
									Toast.LENGTH_LONG).show();

							due_tommorow_list = new ArrayList<DueTodayList>();

							parse_due_tommorow_string();

							parse_due_tommorow_content();

							parse_add_content();

							adapter.notifyDataSetChanged();

							swipeLayout.setRefreshing(false);

							Toast.makeText(getActivity(), downloaded,
									Toast.LENGTH_LONG).show();

							Log.d("Home",
									"############################You are not online!!!!");

						}

					}

				});

		lView.setEmptyView(swipe.findViewById(R.id.emptyView));

		addHomework = (TextView) swipe.findViewById(R.id.textView2);

		addHomework.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showNoteDialog();

				Log.d("dialog shown?", "");

			}
		});

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				this.mClickedReceiver,
				new IntentFilter("refreshListViewTommorow"));

		due_tommorow_list = new ArrayList<DueTodayList>();

		return swipe;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onResume() {

		super.onResume();

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

		}

		due_tommorow_list = new ArrayList<DueTodayList>();

		parse_due_tommorow_content();

		parse_add_content();

		populateListView();

		registerClickCallback();

		lView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {

			@Override
			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				final DueTodayList item = adapter.getItem(position);
				// Store the item for later undo

				final DueTodayList currenthomeworkdue = due_tommorow_list
						.get(position);

				// Remove the item from the adapter
				adapter.remove(adapter.getItem(position));

				return new EnhancedListView.Undoable() {
					@Override
					public void undo() {

						Log.d("undo", "yes");

						adapter.insert(item, position);

					}

					@Override
					public void discard() {

						String Description_Check = currenthomeworkdue
								.getDescription();

						int counterssss = Tommorow_Homework_Counter.getInt(
								"last shared preference", 0);

						int countersssss = counterssss + 1;

						for (int i = 0; i < countersssss; i++) {

							due_tommorow_shared = "due_tommorow"
									+ Integer.toString(i);

							SharedPreferences Tommorows_Homework = getActivity()

							.getSharedPreferences(due_tommorow_shared,
									Context.MODE_PRIVATE);

							String Band1 = Tommorows_Homework.getString(
									"due_tommorow0", null);

							String Number1 = Tommorows_Homework.getString(
									"due_tommorow1", null);

							String Class1 = Tommorows_Homework.getString(
									"due_tommorow2", null);

							String Teacher1 = Tommorows_Homework.getString(
									"due_tommorow3", null);

							String Title1 = Tommorows_Homework.getString(
									"due_tommorow4", null);

							String Date1 = Tommorows_Homework.getString(
									"due_tommorow5", null);

							String Type1 = Tommorows_Homework.getString(
									"due_tommorow6", null);

							String Description1 = Tommorows_Homework.getString(
									"due_tommorow7", null);

							if (Band1 != null) {

								Band = Band1.trim();

							}

							if (Number1 != null) {

								Number = Number1.trim();

							}

							if (Class1 != null) {

								Class = Class1.trim();

							}

							if (Teacher1 != null) {

								Teacher = Teacher1.trim();

							}

							if (Title1 != null) {

								Title = Title1.trim();

							}

							if (Date1 != null) {

								Date = Date1.trim();

							}

							if (Type1 != null) {

								Type = Type1.trim();

							}

							if (Description1 != null) {

								Description = Description1.trim();

							}

							Log.d("shared clear", "no");

							if (Description_Check.equals(Description)) {

								Log.d("shared clear", due_tommorow_shared);

								SharedPreferences.Editor localeditor = getActivity()

								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

						int add_counterssss = Add_Homework_Counter.getInt(
								"add_homework_counter", 0);

						int add_countersssss = add_counterssss + 1;

						System.out.println("Counter for Add Homework1= "
								+ add_countersssss);

						for (int i = 0; i < add_countersssss; i++) {

							due_tommorow_shared = "add_homework"
									+ Integer.toString(i);

							SharedPreferences Add_Homework = getActivity()

							.getSharedPreferences(due_tommorow_shared,
									Context.MODE_PRIVATE);

							String Band1 = Add_Homework.getString("add_band",
									null);

							String Number1 = Add_Homework.getString(
									"add_number", null);

							String Class1 = Add_Homework.getString("add_class",
									null);

							String Teacher1 = Add_Homework.getString(
									"add_teacher", null);

							String Title1 = Add_Homework.getString("add_title",
									null);

							String Date1 = Add_Homework.getString("add_date",
									null);

							String Type1 = Add_Homework.getString("add_type",
									null);

							String Description1 = Add_Homework.getString(
									"add_description", null);

							if (Band1 != null) {

								Band = Band1.trim();

							}

							if (Number1 != null) {

								Number = Number1.trim();

							}

							if (Class1 != null) {

								Class = Class1.trim();

							}

							if (Teacher1 != null) {

								Teacher = Teacher1.trim();

							}

							if (Title1 != null) {

								Title = Title1.trim();

							}

							if (Date1 != null) {

								Date = Date1.trim();

							}

							if (Type1 != null) {

								Type = Type1.trim();

							}

							if (Description1 != null) {

								Description = Description1.trim();

							}

							Log.d("shared clear add", "no");

							if (Description_Check.equals(Description)) {

								Log.d("shared clear add", "yes");

								SharedPreferences.Editor localeditor = getActivity()

								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

					}

				};
			}
		});

		EnhancedListView.UndoStyle style = EnhancedListView.UndoStyle.MULTILEVEL_POPUP;
		lView.setUndoStyle(style);
		lView.enableSwipeToDismiss();
		EnhancedListView.SwipeDirection direction = EnhancedListView.SwipeDirection.END;
		lView.setSwipeDirection(direction);
		lView.setRequireTouchBeforeDismiss(false);
		lView.setSwipingLayout(R.id.homework_item);

	}

	public void parse_due_tommorow_string() {

		String Due_Tommorow = Tommorow_Homework.getString("homework_content",
				"");

		Due_Tommorow = Due_Tommorow.replaceAll("^\"|\"$", "");

		Due_Tommorow = Due_Tommorow.substring(Due_Tommorow.indexOf("\",") + 2);

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

							SharedPreferences Band = getActivity()

							.getSharedPreferences("last band tommorow",
									Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = getActivity()
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_tommorow0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = getActivity()

							.getSharedPreferences("last band tommorow",
									Context.MODE_PRIVATE);

							SharedPreferences Description = getActivity()
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_tommorow7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = getActivity()
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_tommorow7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = getActivity()
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

						SharedPreferences.Editor localEditor = getActivity()
								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

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

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences(due_tommorow_shared,
							Context.MODE_PRIVATE).edit();

			localEditor.putString(due_tommorow_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getActivity()
					.getSharedPreferences("due_tommorow_counter",
							Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared + 1);

			localEditor1.apply();

			strb.setLength(0);

			SharedPreferences.Editor localEditors = getActivity()
					.getSharedPreferences("last band tommorow",
							Context.MODE_PRIVATE).edit();

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

			SharedPreferences.Editor dummy_item = getActivity()
					.getSharedPreferences(due_tommorow_shared,
							Context.MODE_PRIVATE).edit();

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

	public class Download extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... urls) {
			SharedPreferences bDay = mActivity.getSharedPreferences(
					"Login_Info", Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = mActivity.getSharedPreferences(
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
					// Html.fromHtml(duetommorow_html).toString();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd hh:mm a");
					Calendar cal = Calendar.getInstance();
					String downloaded = dateFormat.format(cal.getTime());

					localEditor.putString("homework_content", homework);

					localEditor.putString("download_date", downloaded);

					localEditor.apply();

				} catch (IllegalStateException e) {

					e.printStackTrace();
				} catch (IOException e) {

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

	}

	public class Update extends AsyncTask<String, Void, Void> {

		private final HttpClient Client = new DefaultHttpClient();

		@Override
		protected Void doInBackground(String... urls) {
			SharedPreferences bDay = mActivity.getSharedPreferences(
					"Login_Info", Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = mActivity.getSharedPreferences(
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
					// Html.fromHtml(duetommorow_html).toString();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd hh:mm a");
					Calendar cal = Calendar.getInstance();
					String downloaded = dateFormat.format(cal.getTime());

					localEditor.putString("homework_content", homework);

					localEditor.putString("download_date", downloaded);

					localEditor.apply();

					localEditor.putString("homework_content", homework);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

					due_tommorow_list = new ArrayList<DueTodayList>();

					if (adapter.isEmpty()) {

						Log.d("adapter empty", "yes");

					} else {

						Log.d("adapter empty", "no");

					}

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					parse_add_content();

				} catch (IllegalStateException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (NullPointerException e) {

					due_tommorow_list = new ArrayList<DueTodayList>();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					parse_add_content();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (NoSuchElementException e) {

					due_tommorow_list = new ArrayList<DueTodayList>();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					parse_add_content();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (RuntimeException e) {

					due_tommorow_list = new ArrayList<DueTodayList>();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					parse_add_content();

					SharedPreferences.Editor localEditor = mActivity
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
			LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

			Toast.makeText(mActivity, "Refresh Finished", Toast.LENGTH_LONG).show();

			SharedPreferences download_error = mActivity.getSharedPreferences(
					"homework", Context.MODE_PRIVATE);

			String error = download_error.getString("download_error", "no");

			String download_date = "Download error, refreshed homework using homework downloaded at "
					+ download_error.getString("download_date", "");

			if (error.equals("yes")) {

				SharedPreferences.Editor localEditor = mActivity
						.getSharedPreferences("homework", Context.MODE_PRIVATE)
						.edit();

				Toast.makeText(mActivity, download_date, Toast.LENGTH_LONG)
						.show();

				localEditor.putString("download_error", "no");

				localEditor.commit();

			}

			// adapter.notifyDataSetChanged();

			populateListView();

			swipeLayout.setRefreshing(false);

		}

	}

	public void parse_due_tommorow_content() {

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

		System.out.println(date);

		int counterssss = Tommorow_Homework_Counter.getInt(
				"last shared preference", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_tommorow_shared = "due_tommorow" + Integer.toString(i);

			System.out.println("due_tommorow = " + due_tommorow_shared);

			SharedPreferences Todays_Homework = mActivity.getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE);

			String Band1 = Todays_Homework.getString("due_tommorow0", null);

			String Number1 = Todays_Homework.getString("due_tommorow1", null);

			String Class1 = Todays_Homework.getString("due_tommorow2", null);

			String Teacher1 = Todays_Homework.getString("due_tommorow3", null);

			String Title1 = Todays_Homework.getString("due_tommorow4", null);

			String Date1 = Todays_Homework.getString("due_tommorow5", null);

			String Type1 = Todays_Homework.getString("due_tommorow6", null);

			String Description1 = Todays_Homework.getString("due_tommorow7",
					null);

			if (Band1 != null) {

				Band = Band1.trim();

				Log.d("Band" + i, Band);

			}

			if (Number1 != null) {

				Number = Number1.trim();

				Log.d("Number" + i, Number);

			}

			if (Class1 != null) {

				Class = Class1.trim();

				Log.d("Class" + i, Class);

			}

			if (Teacher1 != null) {

				Teacher = Teacher1.trim();

				Log.d("Teacher" + i, Teacher);

			}

			if (Title1 != null) {

				Title = Title1.trim();

				Log.d("Title" + i, Title);

			}

			if (Date1 != null) {

				Date = Date1.trim();

				Log.d("Date" + i, Date);

			}

			if (Type1 != null) {

				Type = Type1.trim();

				Log.d("Type" + i, Type);

			}

			if (Description1 != null) {

				Description = Description1.trim();

				Log.d("Description" + i, Description);

			}

			SharedPreferences description_check = mActivity
					.getSharedPreferences("descriptioncheck",
							Context.MODE_PRIVATE);

			String descriptionCheck = description_check.getString(
					"description", "");

			if (Type != null && Description != null
					&& !Description.equals(descriptionCheck)
					&& Date.contentEquals(date)) {

				if (Band1 != null) {

					Band = Band1.trim();

					Log.d("Band Passed" + i, Band);

				}

				if (Number1 != null) {

					Number = Number1.trim();

					Log.d("Number Passed" + i, Number);

				}

				if (Class1 != null) {

					Class = Class1.trim();

					Log.d("Class Passed" + i, Class);

				}

				if (Teacher1 != null) {

					Teacher = Teacher1.trim();

					Log.d("Teacher Passed" + i, Teacher);

				}

				if (Title1 != null) {

					Title = Title1.trim();

					Log.d("Title Passed" + i, Title);

				}

				if (Date1 != null) {

					Date = Date1.trim();

					Log.d("Date Passed" + i, Date);

				}

				if (Type1 != null) {

					Type = Type1.trim();

					Log.d("Type Passed" + i, Type);

				}

				if (Description1 != null) {

					Description = Description1.trim();

					Log.d("Description Passed" + i, Description);

				}

				SharedPreferences.Editor checkeditor = mActivity

				.getSharedPreferences("descriptioncheck", Context.MODE_PRIVATE)
						.edit();

				checkeditor.putString("description", Description);

				checkeditor.commit();

				if (!"Type".equals(Type)) {
					due_tommorow_list.add(new DueTodayList(Band, Number, Class,
							Teacher, Title, Date, Type, Description));
				}

			}

		}

	}

	private void registerClickCallback() {
		EnhancedListView list = (EnhancedListView) swipe
				.findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {

				DueTodayList clickedhomeworkdue = due_tommorow_list
						.get(position);

				// Description =
				// clickedhomeworkdue.getDescription().substring(5);

				String transitionName = getString(R.string.transition_homework_icon);

				Intent intent = new Intent(getActivity(),
						HomeworkDueDetailsActivity.class);
				intent.putExtra(KEY_HOMEWORK, clickedhomeworkdue.getTitle());
				intent.putExtra(KEY_DESC, clickedhomeworkdue.getDescription());
				intent.putExtra(KEY_DATE, clickedhomeworkdue.getDate());
				intent.putExtra(KEY_TYPE, clickedhomeworkdue.getType());
				intent.putExtra(KEY_BAND, clickedhomeworkdue.getBand());

				startActivity(intent);

			}
		});
	}

	private void populateListView() {

		adapter = new due_tommorowAdapter();
		EnhancedListView list = (EnhancedListView) swipe
				.findViewById(R.id.listView1);
		list.setAdapter(adapter);

		list.setDismissCallback(new OnDismissCallback() {

			@Override
			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				Log.d("shared clear1", "yes");

				final DueTodayList item = adapter.getItem(position);
				// Store the item for later undo

				final DueTodayList currenthomeworkdue = due_tommorow_list
						.get(position);

				// Remove the item from the adapter
				adapter.remove(adapter.getItem(position));

				// return an Undoable

				return new EnhancedListView.Undoable() {

					// Reinsert the item to the adapter

					@Override
					public void undo() {

						System.out.println("undid");

						adapter.insert(item, position);

					}

					// Delete item completely from your persistent storage
					@Override
					public void discard() {

						String Description_Check = currenthomeworkdue
								.getDescription();

						int counterssss = Tommorow_Homework_Counter.getInt(
								"last shared preference", 0);

						int countersssss = counterssss + 1;

						for (int i = 0; i < countersssss; i++) {

							due_tommorow_shared = "due_tommorow"
									+ Integer.toString(i);

							SharedPreferences Tommorows_Homework = mActivity
									.getSharedPreferences(due_tommorow_shared,
											Context.MODE_PRIVATE);

							String Band1 = Tommorows_Homework.getString(
									"due_tommorow0", null);

							String Number1 = Tommorows_Homework.getString(
									"due_tommorow1", null);

							String Class1 = Tommorows_Homework.getString(
									"due_tommorow2", null);

							String Teacher1 = Tommorows_Homework.getString(
									"due_tommorow3", null);

							String Title1 = Tommorows_Homework.getString(
									"due_tommorow4", null);

							String Date1 = Tommorows_Homework.getString(
									"due_tommorow5", null);

							String Type1 = Tommorows_Homework.getString(
									"due_tommorow6", null);

							String Description1 = Tommorows_Homework.getString(
									"due_tommorow7", null);

							if (Band1 != null) {

								Band = Band1.trim();

							}

							if (Number1 != null) {

								Number = Number1.trim();

							}

							if (Class1 != null) {

								Class = Class1.trim();

							}

							if (Teacher1 != null) {

								Teacher = Teacher1.trim();

							}

							if (Title1 != null) {

								Title = Title1.trim();

							}

							if (Date1 != null) {

								Date = Date1.trim();

							}

							if (Type1 != null) {

								Type = Type1.trim();

							}

							if (Description1 != null) {

								Description = Description1.trim();

							}

							Log.d("shared clear", "no");

							if (Description_Check.equals(Description)) {

								Log.d("shared clear", due_tommorow_shared);

								SharedPreferences.Editor localeditor = mActivity

								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

						int add_counterssss = Add_Homework_Counter.getInt(
								"add_homework_counter", 0);

						int add_countersssss = add_counterssss + 1;

						System.out.println("Counter for Add Homework2= "
								+ add_countersssss);

						for (int i = 0; i < add_countersssss; i++) {

							String add_shared = "add_homework"
									+ Integer.toString(i);

							SharedPreferences Add_Homework = mActivity
									.getSharedPreferences(add_shared,
											Context.MODE_PRIVATE);

							String Band1 = Add_Homework.getString("add_band",
									null);

							String Number1 = Add_Homework.getString(
									"add_number", null);

							String Class1 = Add_Homework.getString("add_class",
									null);

							String Teacher1 = Add_Homework.getString(
									"add_teacher", null);

							String Title1 = Add_Homework.getString("add_title",
									null);

							String Date1 = Add_Homework.getString("add_date",
									null);

							String Type1 = Add_Homework.getString("add_type",
									null);

							String Description1 = Add_Homework.getString(
									"add_description", null);

							if (Band1 != null) {

								Band = Band1.trim();

							}

							if (Number1 != null) {

								Number = Number1.trim();

							}

							if (Class1 != null) {

								Class = Class1.trim();

							}

							if (Teacher1 != null) {

								Teacher = Teacher1.trim();

							}

							if (Title1 != null) {

								Title = Title1.trim();

							}

							if (Date1 != null) {

								Date = Date1.trim();

							}

							if (Type1 != null) {

								Type = Type1.trim();

							}

							if (Description1 != null) {

								Description = Description1.trim();

							}

							Log.d("shared clear add", "no");

							if (Description_Check.equals(Description)) {

								Log.d("shared clear add", "yes");

								SharedPreferences.Editor localeditor = mActivity

								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

					};

				};

			}

		});

	}

	public static int getImageId(Context context, String imageName) {
		return context.getResources().getIdentifier("drawable/" + imageName,
				null, context.getPackageName());
	}

	public class due_tommorowAdapter extends ArrayAdapter<DueTodayList> {
		public due_tommorowAdapter() {
			super(mActivity, R.layout.tommorow_item_view, due_tommorow_list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.tommorow_item_view, parent, false);
				holder = new ViewHolder();

				holder.imageView = (ImageView) convertView
						.findViewById(R.id.item_iconclass);

				holder.HomeworkDueText = (TextView) convertView
						.findViewById(R.id.item_texthomeworkdue);

				holder.DescriptionText = (TextView) convertView
						.findViewById(R.id.item_textdescription);

				holder.TeacherText = (TextView) convertView
						.findViewById(R.id.item_textteacher);

				holder.TypeText = (TextView) convertView
						.findViewById(R.id.item_texttype);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			RelativeLayout swipedBackground = (RelativeLayout) convertView
					.findViewById(R.id.swiped_background);

			if (!ThemeColor.contains("actionbar_color")) {

				swipedBackground
						.setBackgroundColor(Color.parseColor("#4285f4"));

			} else {

				actionbar_colors = ThemeColor
						.getString("actionbar_color", null);

				swipedBackground.setBackgroundColor(Color
						.parseColor(actionbar_colors));
			}

			DueTodayList currenthomeworkdue = due_tommorow_list.get(position);

			Teacher = currenthomeworkdue.getTeacher().substring(0, 1)
					.toUpperCase()
					+ currenthomeworkdue.getTeacher().substring(1)
							.toLowerCase();

			// Description = currenthomeworkdue.getDescription().substring(5);

			Description = currenthomeworkdue.getDescription().toString();

			Description = Description.trim();

			Description = Description.replaceAll("[\\n\\t]", "");

			Description = Html.fromHtml(Description).toString();

            String name = currenthomeworkdue.Band.substring(0,
                   Math.min(currenthomeworkdue.Band.length(), 2));

            Log.d("Drawable name= ", name);

            StringBuilder sb = new StringBuilder(name);
            for (int index = 0; index < sb.length(); index++) {
                char c = sb.charAt(index);
                if (Character.isLowerCase(c)) {
                    sb.setCharAt(index, Character.toUpperCase(c));
                } else {
                    sb.setCharAt(index, Character.toLowerCase(c));
                }
            }

            Log.d("Drawable name lowercase= ", sb.toString());

            int resId = getResources().getIdentifier(sb.toString(), "drawable", getActivity().getPackageName());

            Drawable d;

            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    d = getActivity().getResources().getDrawable(resId, getActivity().getTheme());
                } else {
                    d = getActivity().getResources().getDrawable(resId);
                }

                System.out.println("Drawable id = " + d);

                Glide.with(getActivity()).load(resId).into(holder.imageView);

            }catch (Resources.NotFoundException e){

                System.out.println("Uknown Class Identifier");

                Glide.with(getActivity()).load(R.drawable.z).into(holder.imageView);

            }

			// String Image = currenthomeworkdue.Band.substring(0,
			// Math.min(currenthomeworkdue.Band.length(), 2));
			//
			// int picId = getResources().getIdentifier(Image , "drawable",
			// getActivity().getPackageName());
			//
			// System.out.println(getImageId(getActivity(), Image));
			//
			// holder.imageView.setImageResource(getImageId(getActivity(),
			// Image));

//			if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UU")) {
//
//				holder.imageView.setImageResource(R.drawable.uu);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UN")) {
//
//				holder.imageView.setImageResource(R.drawable.un);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UG")) {
//
//				holder.imageView.setImageResource(R.drawable.ug);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TZ")) {
//
//				holder.imageView.setImageResource(R.drawable.tz);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TQ")) {
//
//				holder.imageView.setImageResource(R.drawable.tq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SR")) {
//
//				holder.imageView.setImageResource(R.drawable.sr);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SQ")) {
//
//				holder.imageView.setImageResource(R.drawable.sq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SP")) {
//
//				holder.imageView.setImageResource(R.drawable.sp);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SK")) {
//
//				holder.imageView.setImageResource(R.drawable.sk);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SF")) {
//
//				holder.imageView.setImageResource(R.drawable.sf);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SC")) {
//
//				holder.imageView.setImageResource(R.drawable.sc);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SB")) {
//
//				holder.imageView.setImageResource(R.drawable.sb);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PQ")) {
//
//				holder.imageView.setImageResource(R.drawable.pq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PP")) {
//
//				holder.imageView.setImageResource(R.drawable.pp);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PH")) {
//
//				holder.imageView.setImageResource(R.drawable.ph);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MS")) {
//
//				holder.imageView.setImageResource(R.drawable.ms);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MR")) {
//
//				holder.imageView.setImageResource(R.drawable.mr);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MQ")) {
//
//				holder.imageView.setImageResource(R.drawable.mq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MP")) {
//
//				holder.imageView.setImageResource(R.drawable.mp);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MG")) {
//
//				holder.imageView.setImageResource(R.drawable.mg);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("ME")) {
//
//				holder.imageView.setImageResource(R.drawable.me);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MC")) {
//
//				holder.imageView.setImageResource(R.drawable.mc);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HU")) {
//
//				holder.imageView.setImageResource(R.drawable.hu);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HG")) {
//
//				holder.imageView.setImageResource(R.drawable.hg);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HF")) {
//
//				holder.imageView.setImageResource(R.drawable.hf);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DM")) {
//
//				holder.imageView.setImageResource(R.drawable.dm);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DW")) {
//
//				holder.imageView.setImageResource(R.drawable.dw);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("EE")) {
//
//				holder.imageView.setImageResource(R.drawable.ee);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DQ")) {
//
//				holder.imageView.setImageResource(R.drawable.dq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DJ")) {
//
//				holder.imageView.setImageResource(R.drawable.dj);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CR")) {
//
//				holder.imageView.setImageResource(R.drawable.cr);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CQ")) {
//
//				holder.imageView.setImageResource(R.drawable.cq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CJ")) {
//
//				holder.imageView.setImageResource(R.drawable.cj);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AQ")) {
//
//				holder.imageView.setImageResource(R.drawable.aq);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AJ")) {
//
//				holder.imageView.setImageResource(R.drawable.aj);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AN")) {
//
//				holder.imageView.setImageResource(R.drawable.an);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			} else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AC")) {
//
//				holder.imageView.setImageResource(R.drawable.ac);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			}
//
//			else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FS")) {
//
//				holder.imageView.setImageResource(R.drawable.spanish);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			}
//
//			else if (currenthomeworkdue.Band.substring(0,
//					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FF")) {
//
//				holder.imageView.setImageResource(R.drawable.french);
//
//                Glide.with(context).load(mIcon[position]).into(imgIcon);
//
//			}

			holder.HomeworkDueText
					.setText(currenthomeworkdue.getTitle().trim());

			holder.DescriptionText.setText(Description);

			holder.DescriptionText.setEllipsize(TruncateAt.END);

			holder.TeacherText.setText(Teacher.trim());

			holder.TypeText.setText(currenthomeworkdue.getType().trim());

			return convertView;

		}

	}

	public void parse_add_content() {

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

		System.out.println(date);

		int counterssss = Add_Homework_Counter
				.getInt("add_homework_counter", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_tommorow_shared = "add_homework" + Integer.toString(i);

			Log.d("homework", due_tommorow_shared);

			SharedPreferences Add_Homework = mActivity.getSharedPreferences(
					due_tommorow_shared, Context.MODE_PRIVATE);

			String Band1 = Add_Homework.getString("add_band", null);

			String Number1 = Add_Homework.getString("add_number", null);

			String Class1 = Add_Homework.getString("add_class", null);

			String Teacher1 = Add_Homework.getString("add_teacher", null);

			String Title1 = Add_Homework.getString("add_title", null);

			String Date1 = Add_Homework.getString("add_date", null);

			String Type1 = Add_Homework.getString("add_type", null);

			String Description1 = Add_Homework.getString("add_description",
					null);

			if (Band1 != null) {

				Band = Band1.trim();

				Log.d("Band" + i, Band);

			}

			if (Number1 != null) {

				Number = Number1.trim();

				Log.d("Number" + i, Number);

			}

			if (Class1 != null) {

				Class = Class1.trim();

				Log.d("Class" + i, Class);

			}

			if (Teacher1 != null) {

				Teacher = Teacher1.trim();

				Log.d("Teacher" + i, Teacher);

			}

			if (Title1 != null) {

				Title = Title1.trim();

				Log.d("Title" + i, Title);

			}

			if (Date1 != null) {

				Date = Date1.trim();

				Log.d("Date" + i, Date);

			}

			if (Type1 != null) {

				Type = Type1.trim();

				Log.d("Type" + i, Type);

			}

			if (Description1 != null) {

				Description = Description1.trim();

				Log.d("Description" + i, Description);

			}

			System.out.println("date= " + date);

			System.out.println("Date= " + Date);

			if (Date != null && Date.contentEquals(date)) {

				if (Band1 != null) {

					Band = Band1.trim();

					Log.d("Band Passed" + i, Band);

				}

				if (Number1 != null) {

					Number = Number1.trim();

					Log.d("Number Passed" + i, Number);

				}

				if (Class1 != null) {

					Class = Class1.trim();

					Log.d("Class Passed" + i, Class);

				}

				if (Teacher1 != null) {

					Teacher = Teacher1.trim();

					Log.d("Teacher Passed" + i, Teacher);

				}

				if (Title1 != null) {

					Title = Title1.trim();

					Log.d("Title Passed" + i, Title);

				}

				if (Date1 != null) {

					Date = Date1.trim();

					Log.d("Date Passed" + i, Date);

				}

				if (Type1 != null) {

					Type = Type1.trim();

					Log.d("Type Passed" + i, Type);

				}

				if (Description1 != null) {

					Description = Description1.trim();

					Log.d("Description Passed" + i, Description);

				}

				if (!"Type".equals(Type)) {
					due_tommorow_list.add(new DueTodayList(Band, Number, Class,
							Teacher, Title, Date, Type, Description));
				}

			}

		}

	}

	private void showNoteDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		NoteDialog noteDialog = new NoteDialog();
		noteDialog.show(ft, null);

	}

	public static void hide_keyboard_from(Context context, View view) {

		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static class NoteDialog extends DialogFragment {

		private EditText mTitleText;

		private EditText mDescriptionText;

		private int Counts;

		public NoteDialog() {

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			final View view = getActivity().getLayoutInflater().inflate(
					R.layout.add_homework, null);

			mTitleText = (EditText) view.findViewById(R.id.editText1);

			mDescriptionText = (EditText) view.findViewById(R.id.editText2);

			builder.setView(view);

			builder.setTitle("Add Homework Assignment");

			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							getDialog().dismiss();
						}
					});

			builder.setPositiveButton("Add",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {

							hide_keyboard_from(getActivity(), view);

							SharedPreferences Homework_Counter = getActivity()
									.getSharedPreferences(
											"add_homework_counter",
											Context.MODE_PRIVATE);

							if (Homework_Counter
									.contains("add_homework_counter")) {

								int Count = Homework_Counter.getInt(
										"add_homework_counter", 0);

								Counts = Count + 1;

								System.out.println("Counts1 " + Counts);

							} else {

								Counts = 0;

								System.out.println("Counts2 " + Counts);

							}

							System.out.println("Counts3 " + Counts);

							String HomeworkNumber = "add_homework"
									+ Integer.toString(Counts);

							SharedPreferences.Editor localEditorCount = getActivity()
									.getSharedPreferences(
											"add_homework_counter",
											Context.MODE_PRIVATE).edit();

							localEditorCount.putInt("add_homework_counter",
									Counts);

							localEditorCount.apply();

							SharedPreferences.Editor localEditor = getActivity()
									.getSharedPreferences(HomeworkNumber,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("add_band", "X");

							localEditor.putString("add_number", "0");

							localEditor.putString("add_class",
									"Manually Added Homework");

							localEditor.putString("add_teacher", "You");

							localEditor.putString("add_title", mTitleText
									.getText().toString());

							localEditor.putString("add_date", date);

							localEditor.putString("add_type", "Homework");

							localEditor.putString("add_description",
									mDescriptionText.getText().toString());

							localEditor.apply();

							getDialog().dismiss();

							Intent intent = new Intent(
									"refreshListViewTommorow");

							intent.putExtra("refresh", "refresh listview");
							LocalBroadcastManager.getInstance(getActivity())
									.sendBroadcast(intent);

						}
					});

			return builder.create();

		}

	}

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			due_tommorow_list = new ArrayList<DueTodayList>();

			parse_due_tommorow_content();

			parse_add_content();

			populateListView();

			Log.d("homework_add", "refresh");
			//
			// new UpdateAdd().execute();

		}
	};

	public class UpdateAdd extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... add) {

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			due_tommorow_list = new ArrayList<DueTodayList>();

			parse_due_tommorow_content();

			parse_add_content();

			Log.d("homework_add", "refresh");

			return null;

		}

		@Override
		protected void onPostExecute(Void updateUI) {

			populateListView();

		}

	}

    public Bitmap getPic (String pic)
    {
        return
                BitmapFactory.decodeResource
                        (
                                getResources(), getResourceID(pic, "drawable", getActivity().getApplicationContext())
                        );
    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

}
