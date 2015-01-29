package com.bernard.beaconportal.activities;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;
import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;
import de.timroes.android.listview.EnhancedListView.SwipeDirection;
import de.timroes.android.listview.EnhancedListView.UndoStyle;

public class Due_Today_Fragment extends Fragment {

	private Activity mActivity;

	private List<Due_Today_List> due_today_list;

	private EnhancedListView list;

	private View swipe;

	private String actionbar_colors;

	private static String date;

	private int shared;

	private SharedPreferences Today_Homework_Counter;

	private SharedPreferences Today_Homework;

	private SharedPreferences Add_Homework_Counter;

	public static EnhancedListView lView;

	private ArrayAdapter<Due_Today_List> adapter;

	private TextView addHomework;

	public View footer;

	public TextView footer_text;

	private String Band, Number, Class, Teacher, Title, Date, Type,
			Description, due_today_shared, due_today_shared_content;

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

		Today_Homework_Counter = getActivity().getSharedPreferences(
				"due_today_counter", Context.MODE_PRIVATE);

		Add_Homework_Counter = getActivity().getSharedPreferences(
				"add_homework_counter", Context.MODE_PRIVATE);

		Today_Homework = getActivity().getSharedPreferences("homework",
				Context.MODE_PRIVATE);

		new Download().execute();

		swipe = inflater.inflate(R.layout.activity_main, container, false);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

		}

		swipeLayout = (SwipeRefreshLayout) swipe.findViewById(R.id.swipe);

		swipeLayout.setEnabled(false);

		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_blue_light,
				android.R.color.holo_orange_light);

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

		SharedPreferences sharedprefers = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefers.contains("actionbar_color")) {

			footer_text.setTextColor(Color.parseColor("#4285f4"));

		} else {

			actionbar_colors = sharedprefers.getString("actionbar_color", null);

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

							e.printStackTrace();
						} catch (IOException e) {

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

							due_today_list = new ArrayList<Due_Today_List>();

							parse_due_today_string();

							parse_due_today_content();

							parse_add_content();

							populateListView();

							swipeLayout.setRefreshing(false);

							Toast.makeText(mActivity, downloaded,
									Toast.LENGTH_LONG).show();

							Log.d("Home",
									"############################You are not online!!!!");

						}

					}

				});

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		date = sdf.format(c.getTime());

		System.out.println(date);

		lView.setEmptyView(swipe.findViewById(R.id.emptyView));

		addHomework = (TextView) swipe.findViewById(R.id.textView2);

		addHomework.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showNoteDialog();

				Log.d("dialog shown?", "");

			}
		});

		LocalBroadcastManager.getInstance(getActivity())
				.registerReceiver(this.mClickedReceiver,
						new IntentFilter("refreshListViewToday"));

		due_today_list = new ArrayList<Due_Today_List>();

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

		SharedPreferences sharedprefer = mActivity.getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

		}

		due_today_list = new ArrayList<Due_Today_List>();

		parse_due_today_content();

		parse_add_content();

		populateListView();

		registerClickCallback();

		lView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {

			@Override
			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				Log.d("shared clear1", "yes");

				final Due_Today_List item = adapter.getItem(position);
				// Store the item for later undo

				final Due_Today_List currenthomeworkdue = due_today_list
						.get(position);

				// Remove the item from the adapter
				adapter.remove(adapter.getItem(position));

				// return an Undoable
				return new EnhancedListView.Undoable() {
					// Reinsert the item to the adapter
					@Override
					public void undo() {

						adapter.insert(item, position);

					}

					// Return a string for your item

					// Delete item completely from your persistent storage
					@Override
					public void discard() {

						String Description_Check = currenthomeworkdue
								.getDescription();

						int counterssss = Today_Homework_Counter.getInt(
								"last shared preference", 0);

						int countersssss = counterssss + 1;

						for (int i = 0; i < countersssss; i++) {

							due_today_shared = "due_today"
									+ Integer.toString(i);

							SharedPreferences Todays_Homework = mActivity

							.getSharedPreferences(due_today_shared,
									Context.MODE_PRIVATE);

							String Band1 = Todays_Homework.getString(
									"due_today0", null);

							String Number1 = Todays_Homework.getString(
									"due_today1", null);

							String Class1 = Todays_Homework.getString(
									"due_today2", null);

							String Teacher1 = Todays_Homework.getString(
									"due_today3", null);

							String Title1 = Todays_Homework.getString(
									"due_today4", null);

							String Date1 = Todays_Homework.getString(
									"due_today5", null);

							String Type1 = Todays_Homework.getString(
									"due_today6", null);

							String Description1 = Todays_Homework.getString(
									"due_today7", null);

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

								Log.d("shared clear", "yes");

								SharedPreferences.Editor localeditor = mActivity

								.getSharedPreferences(due_today_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

						int add_counterssss = Add_Homework_Counter.getInt(
								"add_homework_counter", 0);

						int add_countersssss = add_counterssss + 1;

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

								.getSharedPreferences(due_today_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

					};

				};

			}

		});

		EnhancedListView.UndoStyle style = EnhancedListView.UndoStyle.MULTILEVEL_POPUP;
		lView.setUndoStyle(style);
		lView.enableSwipeToDismiss();
		lView.setSwipeDirection(SwipeDirection.START);
		lView.setRequireTouchBeforeDismiss(false);
		lView.setSwipingLayout(R.id.homework_item);


	}

	public void parse_due_today_string() {

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

							SharedPreferences Band = mActivity

							.getSharedPreferences("last band today",
									Context.MODE_PRIVATE);

							String band = Band.getString("last string",
									"ZZZZZZ");

							SharedPreferences.Editor localEditor = mActivity
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_today0", band);

							localEditor.apply();

							shared_add++;
						}

						if (shared_add > 8) {

							SharedPreferences Band = mActivity

							.getSharedPreferences("last band today",
									Context.MODE_PRIVATE);

							SharedPreferences Description = mActivity
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE);

							String last = Band.getString("last string",
									"ZZZZZZ");

							String description = Description.getString(
									"due_today7", "");

							String fixed = description + last;

							Log.d("fixed", fixed);

							SharedPreferences.Editor localEditor = mActivity
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE).edit();

							localEditor.putString("due_today7", fixed);

							localEditor.apply();

						}

						SharedPreferences.Editor localEditors = mActivity
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

						SharedPreferences.Editor localEditor = mActivity
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

			SharedPreferences.Editor localEditor = mActivity
					.getSharedPreferences(due_today_shared,
							Context.MODE_PRIVATE).edit();

			localEditor.putString(due_today_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = mActivity
					.getSharedPreferences("due_today_counter",
							Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

			due_today_shared = "due_today" + Integer.toString(shared + 1);

			SharedPreferences.Editor dummy_item = mActivity
					.getSharedPreferences(due_today_shared,
							Context.MODE_PRIVATE).edit();

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
					// Html.fromHtml(homework_html).toString();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("due_today",
									Context.MODE_PRIVATE).edit();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd hh:mm a");
					Calendar cal = Calendar.getInstance();
					String downloaded = dateFormat.format(cal.getTime());

					localEditor.putString("homework_content", homework);

					localEditor.putString("download_date", downloaded);

					localEditor.apply();

					Log.d("receiver", "information given to shared preferences");

					due_today_list = new ArrayList<Due_Today_List>();

					parse_due_today_string();

					parse_due_today_content();

					parse_add_content();

				} catch (IllegalStateException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (NullPointerException e) {

					due_today_list = new ArrayList<Due_Today_List>();

					parse_due_today_string();

					parse_due_today_content();

					parse_add_content();

					adapter.notifyDataSetChanged();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();

				} catch (NoSuchElementException e) {

					due_today_list = new ArrayList<Due_Today_List>();

					parse_due_today_string();

					parse_due_today_content();

					parse_add_content();

					adapter.notifyDataSetChanged();

					SharedPreferences.Editor localEditor = mActivity
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();

				} catch (RuntimeException e) {
					due_today_list = new ArrayList<Due_Today_List>();

					parse_due_today_string();

					parse_due_today_content();

					parse_add_content();

					adapter.notifyDataSetChanged();

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

			swipeLayout.setRefreshing(false);

			Log.d("sender", "Broadcasting message");

			Toast.makeText(mActivity, "Refresh Finished", 4000).show();

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

			adapter.notifyDataSetChanged();

			swipeLayout.setRefreshing(false);

		}

	}

	public void parse_due_today_content() {

		int counterssss = Today_Homework_Counter.getInt(
				"last shared preference", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_today_shared = "due_today" + Integer.toString(i);

			SharedPreferences Todays_Homework = mActivity.getSharedPreferences(
					due_today_shared, Context.MODE_PRIVATE);

			String Band1 = Todays_Homework.getString("due_today0", null);

			String Number1 = Todays_Homework.getString("due_today1", null);

			String Class1 = Todays_Homework.getString("due_today2", null);

			String Teacher1 = Todays_Homework.getString("due_today3", null);

			String Title1 = Todays_Homework.getString("due_today4", null);

			String Date1 = Todays_Homework.getString("due_today5", null);

			String Type1 = Todays_Homework.getString("due_today6", null);

			String Description1 = Todays_Homework.getString("due_today7", null);

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

			SharedPreferences description_check = mActivity
					.getSharedPreferences("descriptioncheck",
							Context.MODE_PRIVATE);

			String descriptionCheck = description_check.getString(
					"description", "");

			if (Type != null && Description != null && !Type.isEmpty()
					&& !Description.equals(descriptionCheck)
					&& Description.length() > 5 && Date.equals(date)) {

				SharedPreferences.Editor checkeditor = mActivity

				.getSharedPreferences("descriptioncheck", Context.MODE_PRIVATE)
						.edit();

				checkeditor.putString("description", Description);

				checkeditor.commit();

				if (!"Type".equals(Type)) {
					due_today_list.add(new Due_Today_List(Band, Number, Class,
							Teacher, Title, Date, Type, Description));
				}
			}

		}

	}

	private void registerClickCallback() {
		EnhancedListView list = (EnhancedListView) getView().findViewById(
				R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {

				Due_Today_List clickedhomeworkdue = due_today_list
						.get(position);

				Intent intent = new Intent(mActivity,
						homeworkdueDetailsActivity.class);
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
		adapter = new Due_TodayAdapter();
		EnhancedListView list = (EnhancedListView) swipe
				.findViewById(R.id.listView1);
		list.setAdapter(adapter);

		list.setDismissCallback(new OnDismissCallback() {

			@Override
			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				Log.d("shared clear1", "yes");

				final Due_Today_List item = adapter.getItem(position);
				// Store the item for later undo

				final Due_Today_List currenthomeworkdue = due_today_list
						.get(position);

				// Remove the item from the adapter
				adapter.remove(adapter.getItem(position));

				// return an Undoable
				return new EnhancedListView.Undoable() {
					// Reinsert the item to the adapter
					@Override
					public void undo() {

						adapter.insert(item, position);

					}

					// Return a string for your item

					// Delete item completely from your persistent storage
					@Override
					public void discard() {

						String Description_Check = currenthomeworkdue
								.getDescription();

						int counterssss = Today_Homework_Counter.getInt(
								"last shared preference", 0);

						int countersssss = counterssss + 1;

						for (int i = 0; i < countersssss; i++) {

							due_today_shared = "due_today"
									+ Integer.toString(i);

							SharedPreferences Todays_Homework = mActivity

							.getSharedPreferences(due_today_shared,
									Context.MODE_PRIVATE);

							String Band1 = Todays_Homework.getString(
									"due_today0", null);

							String Number1 = Todays_Homework.getString(
									"due_today1", null);

							String Class1 = Todays_Homework.getString(
									"due_today2", null);

							String Teacher1 = Todays_Homework.getString(
									"due_today3", null);

							String Title1 = Todays_Homework.getString(
									"due_today4", null);

							String Date1 = Todays_Homework.getString(
									"due_today5", null);

							String Type1 = Todays_Homework.getString(
									"due_today6", null);

							String Description1 = Todays_Homework.getString(
									"due_today7", null);

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

								Log.d("shared clear", "yes");

								SharedPreferences.Editor localeditor = mActivity

								.getSharedPreferences(due_today_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

						int add_counterssss = Add_Homework_Counter.getInt(
								"add_homework_counter", 0);

						int add_countersssss = add_counterssss + 1;

						for (int i = 0; i < add_countersssss; i++) {

							due_today_shared = "add_homework"
									+ Integer.toString(i);

							SharedPreferences Add_Homework = mActivity
									.getSharedPreferences(due_today_shared,
											Context.MODE_PRIVATE);

							String Band1 = Add_Homework_Counter.getString(
									"add_band", null);

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

								.getSharedPreferences(due_today_shared,
										Context.MODE_PRIVATE).edit();

								localeditor.clear();

								localeditor.commit();

							}

						}

					};

				};

			}

		});

		list.setUndoStyle(UndoStyle.MULTILEVEL_POPUP);

		list.setRequireTouchBeforeDismiss(false);

		list.enableSwipeToDismiss();

		list.setSwipeDirection(SwipeDirection.START);

	}

	public class Due_TodayAdapter extends ArrayAdapter<Due_Today_List> {
		public Due_TodayAdapter() {
			super(mActivity, R.layout.today_item_view, due_today_list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.today_item_view, parent, false);
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

			Due_Today_List currenthomeworkdue = due_today_list.get(position);

			Teacher = currenthomeworkdue.getTeacher().substring(0, 1)
					.toUpperCase()
					+ currenthomeworkdue.getTeacher().substring(1)
							.toLowerCase();

			Description = currenthomeworkdue.getDescription().toString();

			Description = Description.replaceAll("[\\n\\t]", "");

			Description = Html.fromHtml(Description).toString();

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UU")) {

				holder.imageView.setImageResource(R.drawable.uu);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UN")) {

				holder.imageView.setImageResource(R.drawable.un);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UG")) {

				holder.imageView.setImageResource(R.drawable.ug);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TZ")) {

				holder.imageView.setImageResource(R.drawable.tz);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TQ")) {

				holder.imageView.setImageResource(R.drawable.tq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SR")) {

				holder.imageView.setImageResource(R.drawable.sr);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SQ")) {

				holder.imageView.setImageResource(R.drawable.sq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SP")) {

				holder.imageView.setImageResource(R.drawable.sp);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SK")) {

				holder.imageView.setImageResource(R.drawable.sk);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SF")) {

				holder.imageView.setImageResource(R.drawable.sf);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SC")) {

				holder.imageView.setImageResource(R.drawable.sc);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SB")) {

				holder.imageView.setImageResource(R.drawable.sb);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PQ")) {

				holder.imageView.setImageResource(R.drawable.pq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PP")) {

				holder.imageView.setImageResource(R.drawable.pp);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PH")) {

				holder.imageView.setImageResource(R.drawable.ph);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MS")) {

				holder.imageView.setImageResource(R.drawable.ms);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MR")) {

				holder.imageView.setImageResource(R.drawable.mr);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MQ")) {

				holder.imageView.setImageResource(R.drawable.mq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MP")) {

				holder.imageView.setImageResource(R.drawable.mp);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MG")) {

				holder.imageView.setImageResource(R.drawable.mg);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("ME")) {

				holder.imageView.setImageResource(R.drawable.me);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MC")) {

				holder.imageView.setImageResource(R.drawable.mc);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HU")) {

				holder.imageView.setImageResource(R.drawable.hu);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HG")) {

				holder.imageView.setImageResource(R.drawable.hg);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HF")) {

				holder.imageView.setImageResource(R.drawable.hf);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DM")) {

				holder.imageView.setImageResource(R.drawable.dm);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DW")) {

				holder.imageView.setImageResource(R.drawable.dw);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("EE")) {

				holder.imageView.setImageResource(R.drawable.ee);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DQ")) {

				holder.imageView.setImageResource(R.drawable.dq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DJ")) {

				holder.imageView.setImageResource(R.drawable.dj);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CR")) {

				holder.imageView.setImageResource(R.drawable.cr);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CQ")) {

				holder.imageView.setImageResource(R.drawable.cq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CJ")) {

				holder.imageView.setImageResource(R.drawable.cj);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AQ")) {

				holder.imageView.setImageResource(R.drawable.aq);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AJ")) {

				holder.imageView.setImageResource(R.drawable.aj);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AN")) {

				holder.imageView.setImageResource(R.drawable.an);

			} else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AC")) {

				holder.imageView.setImageResource(R.drawable.ac);

			}

			else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FS")) {

				holder.imageView.setImageResource(R.drawable.spanish);

			}

			else if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FF")) {

				holder.imageView.setImageResource(R.drawable.french);

			}

			holder.HomeworkDueText
					.setText(currenthomeworkdue.getTitle().trim());

			holder.DescriptionText.setText(Description);

			holder.DescriptionText.setEllipsize(TruncateAt.END);

			holder.TeacherText.setText(Teacher.trim());

			holder.TypeText.setText(currenthomeworkdue.getType().trim());

			return convertView;

		}

		public void onCreateOptionsMenu(android.view.Menu menu,
				android.view.MenuInflater inflater) {

			inflater.inflate(R.menu.android_refresh, menu);
		}

		public boolean onOptionsItemSelected(android.view.MenuItem paramMenuItem) {
			switch (paramMenuItem.getItemId()) {
			case R.id.apply:

				return true;

			}
			return true;

		}

	}

	public void parse_add_content() {

		Calendar calendar = Calendar.getInstance();

		calendar.get(Calendar.DAY_OF_WEEK);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		date = sdf.format(c.getTime());

		System.out.println(date);

		int counterssss = Add_Homework_Counter
				.getInt("add_homework_counter", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_today_shared = "add_homework" + Integer.toString(i);

			Log.d("homework", due_today_shared);

			SharedPreferences Todays_Homework = mActivity.getSharedPreferences(
					due_today_shared, Context.MODE_PRIVATE);

			String Band1 = Todays_Homework.getString("add_band", null);

			String Number1 = Todays_Homework.getString("add_number", null);

			String Class1 = Todays_Homework.getString("add_class", null);

			String Teacher1 = Todays_Homework.getString("add_teacher", null);

			String Title1 = Todays_Homework.getString("add_title", null);

			String Date1 = Todays_Homework.getString("add_date", null);

			String Type1 = Todays_Homework.getString("add_type", null);

			String Description1 = Todays_Homework.getString("add_description",
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

			if (Date.contentEquals(date)) {

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
					due_today_list.add(new Due_Today_List(Band, Number, Class,
							Teacher, Title, Date, Type, Description));
				}

			}

		}

	}

	public static void hide_keyboard_from(Context context, View view) {

		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private void showNoteDialog() {
		FragmentManager fm = getFragmentManager();
		NoteDialog noteDialog = new NoteDialog();
		noteDialog.show(fm, null);

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

							Intent intent = new Intent("refreshListViewToday");

							intent.putExtra("refresh", "refresh listview");
							LocalBroadcastManager.getInstance(getActivity())
									.sendBroadcast(intent);

							getDialog().dismiss();

						}
					});

			return builder.create();

		}

	}

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			due_today_list = new ArrayList<Due_Today_List>();

			parse_due_today_content();

			parse_add_content();

			populateListView();

			Log.d("homework_add", "refresh");

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

			due_today_list = new ArrayList<Due_Today_List>();

			parse_due_today_content();

			parse_add_content();

			Log.d("homework_add", "refresh");

			return null;

		}

		@Override
		protected void onPostExecute(Void updateUI) {

			populateListView();

		}

	}

}
