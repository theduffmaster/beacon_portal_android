package com.bernard.beaconportal.activities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.Due_Today_Fragment.Download;
import com.bernard.beaconportal.activities.Due_Today_Fragment.Update;

import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;
import de.timroes.android.listview.EnhancedListView.SwipeDirection;
import de.timroes.android.listview.EnhancedListView.UndoStyle;

public class Due_Tommorow_Fragment extends Fragment {
	private List<homeworkdue> myhomeworkdue;

	private List<Due_Today_List> due_tommorow_list;

	private List<String> read_due_tommorow_list;

	private View swipe;

	private int count;

	private int shared;

	private int countersss;

	public static EnhancedListView lView;

	private ArrayAdapter<Due_Today_List> adapter;

	private String Data, Band, Number, Class, Teacher, Title, Date, Type,
			Description, DescriptionAll, DescriptionCheck, due_tommorow_shared,
			due_tommorow_shared_content;

	private Activity context;

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

		new Download().execute();

		View swipe = inflater.inflate(R.layout.activity_main, container, false);

		swipeLayout = (SwipeRefreshLayout) swipe.findViewById(R.id.swipe);

		swipeLayout.setEnabled(false);

		swipeLayout.setColorSchemeResources(android.R.color.holo_orange_light,
				android.R.color.holo_blue_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_blue_light);

		lView = (EnhancedListView) swipe.findViewById(R.id.listView1);

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

							due_tommorow_list.clear();

							parse_due_tommorow_string();

							parse_due_tommorow_content();

							adapter.notifyDataSetChanged();

							swipeLayout.setRefreshing(false);

							Toast.makeText(getActivity(), downloaded,
									Toast.LENGTH_LONG).show();

							Log.d("Home",
									"############################You are not online!!!!");

						}

					}

				});
		
		return swipe;

	}

	@Override
	public void onResume() {

		super.onResume();

		read_due_tommorow_list = new ArrayList<String>();

		due_tommorow_list = new ArrayList<Due_Today_List>();

		parse_due_tommorow_content();

		populateListView();

		registerClickCallback();

		lView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {

			@Override
			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				final Due_Today_List item = (Due_Today_List) adapter
						.getItem(position);
				// Store the item for later undo

				final Due_Today_List currenthomeworkdue = due_tommorow_list
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

						SharedPreferences Tommorow_Homework_Counter = getActivity()
								.getApplicationContext().getSharedPreferences(
										"due_tommorow_counter",
										Context.MODE_PRIVATE);

						int counterssss = Tommorow_Homework_Counter.getInt(
								"last shared preference", 0);

						int countersssss = counterssss + 1;

						for (int i = 0; i < countersssss; i++) {

							due_tommorow_shared = "due_tommorow"
									+ Integer.toString(i);

							SharedPreferences Tommorows_Homework = getActivity()
									.getApplicationContext()
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
										.getApplicationContext()
										.getSharedPreferences(
												due_tommorow_shared,
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

	}

	public void parse_due_tommorow_string() {

		SharedPreferences Tommorow_Homework = getActivity()
				.getApplicationContext().getSharedPreferences("homework",
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

			due_tommorow_shared_content = "due_tommorow7";

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences(due_tommorow_shared,
							Context.MODE_PRIVATE).edit();

			localEditor.putString(due_tommorow_shared_content, strr);

			localEditor.apply();

			SharedPreferences.Editor localEditor1 = getActivity()
					.getSharedPreferences("due_tommorow_counter",
							Context.MODE_PRIVATE).edit();

			localEditor1.putInt("last shared preference", shared);

			localEditor1.apply();

			strb.setLength(0);

		} catch (IOException e) {

			e.printStackTrace();
		}

		finally {

		}
	}

	public class Download extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... urls) {
			SharedPreferences bDay = getActivity().getSharedPreferences(
					"Login_Info", Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = getActivity().getSharedPreferences(
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
					// Html.fromHtml(duetommorow_html).toString();

					SharedPreferences.Editor localEditor = getActivity()
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
			SharedPreferences bDay = getActivity().getSharedPreferences(
					"Login_Info", Context.MODE_PRIVATE);

			String day1 = Integer.toString(bDay.getInt("Day", 0));

			String year1 = Integer.toString(bDay.getInt("Year", 0));

			String month1 = Integer.toString(1 + bDay.getInt("Month", 0));

			SharedPreferences userName = getActivity().getSharedPreferences(
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
					// Html.fromHtml(duetommorow_html).toString();

					SharedPreferences.Editor localEditor = getActivity()
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

					due_tommorow_list.clear();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

				} catch (IllegalStateException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (NullPointerException e) {

					due_tommorow_list.clear();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					SharedPreferences.Editor localEditor = getActivity()
							.getSharedPreferences("homework",
									Context.MODE_PRIVATE).edit();

					localEditor.putString("download_error", "yes");

					localEditor.apply();

					e.printStackTrace();
				}

				catch (NoSuchElementException e) {

					due_tommorow_list.clear();

					parse_due_tommorow_string();

					parse_due_tommorow_content();

					SharedPreferences.Editor localEditor = getActivity()
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

			adapter.notifyDataSetChanged();

			Intent intent = new Intent("up_navigation");

			intent.putExtra("message", "This is my message!");
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
					intent);

			Toast.makeText(getActivity(), "Refresh Finished", 4000).show();

			SharedPreferences download_error = getActivity()
					.getSharedPreferences("homework", Context.MODE_PRIVATE);

			String error = download_error.getString("download_error", "no");

			String download_date = "Download error, refreshed homework using homework downloaded at "
					+ download_error.getString("download_date", "");

			if (error.equals("yes")) {

				SharedPreferences.Editor localEditor = getActivity()
						.getSharedPreferences("homework", Context.MODE_PRIVATE)
						.edit();

				Toast.makeText(getActivity(), download_date, Toast.LENGTH_LONG)
						.show();

				localEditor.putString("download_error", "no");

				localEditor.commit();

			}

			adapter.notifyDataSetChanged();

			swipeLayout.setRefreshing(false);

		}

	}

	public void parse_due_tommorow_content() {

		SharedPreferences Today_Homework_Counter = getActivity()
				.getApplicationContext().getSharedPreferences(
						"due_tommorow_counter", Context.MODE_PRIVATE);

		int counterssss = Today_Homework_Counter.getInt(
				"last shared preference", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_tommorow_shared = "due_tommorow" + Integer.toString(i);

			SharedPreferences Todays_Homework = getActivity()
					.getApplicationContext().getSharedPreferences(
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

			SharedPreferences description_check = getActivity()
					.getApplicationContext().getSharedPreferences(
							"descriptioncheck", Context.MODE_PRIVATE);

			String descriptionCheck = description_check.getString(
					"description", "");

			if (Type != null && !Type.isEmpty()
					&& !Description.equals(descriptionCheck)) {

				SharedPreferences.Editor checkeditor = getActivity()
						.getApplicationContext()
						.getSharedPreferences("descriptioncheck",
								Context.MODE_PRIVATE).edit();

				checkeditor.putString("description", Description);

				checkeditor.commit();
				due_tommorow_list.add(new Due_Today_List(Band, Number, Class,
						Teacher, Title, Date, Type, Description));

			}

		}

	}

	private void registerClickCallback() {
		ListView list = (ListView) getView().findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {

				Due_Today_List clickedhomeworkdue = due_tommorow_list
						.get(position);

				// Description =
				// clickedhomeworkdue.getDescription().substring(5);

				Intent intent = new Intent(getActivity(),
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
		adapter = new due_tommorowAdapter();
		EnhancedListView list = (EnhancedListView) getView().findViewById(
				R.id.listView1);
		list.setAdapter(adapter);

		list.setDismissCallback(new OnDismissCallback() {

			public EnhancedListView.Undoable onDismiss(
					EnhancedListView listView, final int position) {

				Log.d("shared clear1", "yes");

				final Due_Today_List item = (Due_Today_List) adapter
						.getItem(position);
				// Store the item for later undo

				final Due_Today_List currenthomeworkdue = due_tommorow_list
						.get(position);

				// Remove the item from the adapter
				adapter.remove(adapter.getItem(position));

				String Description_Check = currenthomeworkdue.getDescription();

				SharedPreferences Tommorow_Homework_Counter = getActivity()
						.getApplicationContext().getSharedPreferences(
								"due_tommorow_counter", Context.MODE_PRIVATE);

				int counterssss = Tommorow_Homework_Counter.getInt(
						"last shared preference", 0);

				int countersssss = counterssss + 1;

				for (int i = 0; i < countersssss; i++) {

					due_tommorow_shared = "due_tommorow" + Integer.toString(i);

					SharedPreferences Tommorows_Homework = getActivity()
							.getApplicationContext().getSharedPreferences(
									due_tommorow_shared, Context.MODE_PRIVATE);

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
								.getApplicationContext()
								.getSharedPreferences(due_tommorow_shared,
										Context.MODE_PRIVATE).edit();

						localeditor.clear();

						localeditor.commit();

					}

				}

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

					};

				};

			}

		});

	}

	public class due_tommorowAdapter extends ArrayAdapter<Due_Today_List> {
		public due_tommorowAdapter() {
			super(getActivity(), R.layout.item_view, due_tommorow_list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_view, parent, false);
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

			Due_Today_List currenthomeworkdue = due_tommorow_list.get(position);

			Teacher = currenthomeworkdue.getTeacher().substring(0, 1)
					.toUpperCase()
					+ currenthomeworkdue.getTeacher().substring(1)
							.toLowerCase();

			// Description = currenthomeworkdue.getDescription().substring(5);

			Description = currenthomeworkdue.getDescription().toString();

			Description = Description.trim();

			Description = Description.replaceAll("[\\n\\t]", "");

			Description = Html.fromHtml(Description).toString();

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UU")) {

				holder.imageView.setImageResource(R.drawable.uu);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UN")) {

				holder.imageView.setImageResource(R.drawable.un);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UG")) {

				holder.imageView.setImageResource(R.drawable.ug);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TZ")) {

				holder.imageView.setImageResource(R.drawable.tz);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TQ")) {

				holder.imageView.setImageResource(R.drawable.tq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SR")) {

				holder.imageView.setImageResource(R.drawable.sr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SQ")) {

				holder.imageView.setImageResource(R.drawable.sq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SP")) {

				holder.imageView.setImageResource(R.drawable.sp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SK")) {

				holder.imageView.setImageResource(R.drawable.sk);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SF")) {

				holder.imageView.setImageResource(R.drawable.sf);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SC")) {

				holder.imageView.setImageResource(R.drawable.sc);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SB")) {

				holder.imageView.setImageResource(R.drawable.sb);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PQ")) {

				holder.imageView.setImageResource(R.drawable.pq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PP")) {

				holder.imageView.setImageResource(R.drawable.pp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PH")) {

				holder.imageView.setImageResource(R.drawable.ph);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MS")) {

				holder.imageView.setImageResource(R.drawable.ms);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MR")) {

				holder.imageView.setImageResource(R.drawable.mr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MQ")) {

				holder.imageView.setImageResource(R.drawable.mq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MP")) {

				holder.imageView.setImageResource(R.drawable.mp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MG")) {

				holder.imageView.setImageResource(R.drawable.mg);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("ME")) {

				holder.imageView.setImageResource(R.drawable.me);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MC")) {

				holder.imageView.setImageResource(R.drawable.mc);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HU")) {

				holder.imageView.setImageResource(R.drawable.hu);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HG")) {

				holder.imageView.setImageResource(R.drawable.hg);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HF")) {

				holder.imageView.setImageResource(R.drawable.hf);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DM")) {

				holder.imageView.setImageResource(R.drawable.dm);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DW")) {

				holder.imageView.setImageResource(R.drawable.dw);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("EE")) {

				holder.imageView.setImageResource(R.drawable.ee);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DQ")) {

				holder.imageView.setImageResource(R.drawable.dq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DJ")) {

				holder.imageView.setImageResource(R.drawable.dj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CR")) {

				holder.imageView.setImageResource(R.drawable.cr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CQ")) {

				holder.imageView.setImageResource(R.drawable.cq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CJ")) {

				holder.imageView.setImageResource(R.drawable.cj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AQ")) {

				holder.imageView.setImageResource(R.drawable.aq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AJ")) {

				holder.imageView.setImageResource(R.drawable.aj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AN")) {

				holder.imageView.setImageResource(R.drawable.an);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AC")) {

				holder.imageView.setImageResource(R.drawable.ac);

			}

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FS")) {

				holder.imageView.setImageResource(R.drawable.spanish);

			}

			if (currenthomeworkdue.Band.substring(0,
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

		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// TODO Auto-generated method stub

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

}