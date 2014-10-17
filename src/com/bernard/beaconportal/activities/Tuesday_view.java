package com.bernard.beaconportal.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bernard.beaconportal.activities.R;

import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;
import de.timroes.android.listview.EnhancedListView.UndoStyle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tuesday_view extends Fragment {

	private String ABand, BBand, CBand, DBand, EBand, FBand, HBand, GBand;

	private int count0, count1, count2, count3, count4, count5, count6;

	private static String bandString;

	private static int position;
	
	private ListView list;
	
	private View footer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.schedule_list_view, container, false);
	}

	private List<schedule_view> myschedule;

	static class ViewHolder {

		public TextView HomeworkDueText;

		public TextView ClassesText;

		public TextView NotesCountText;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		if (day == 3) {

		}

		SharedPreferences sharedpref = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday", Context.MODE_PRIVATE);
		HBand = sharedpref.getString("h_Band", null);
		GBand = sharedpref.getString("g_Band", null);
		BBand = sharedpref.getString("b_Band", null);
		EBand = sharedpref.getString("e_Band", null);
		DBand = sharedpref.getString("d_Band", null);
		CBand = sharedpref.getString("c_Band", null);
		ABand = sharedpref.getString("a_Band", null);

		SharedPreferences sharedpref0 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday0", Context.MODE_PRIVATE);

		count0 = sharedpref0.getInt("note_count", 1000);

		SharedPreferences sharedpref1 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday1", Context.MODE_PRIVATE);

		count1 = sharedpref1.getInt("note_count", 1000);

		SharedPreferences sharedpref2 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday2", Context.MODE_PRIVATE);

		count2 = sharedpref2.getInt("note_count", 1000);

		SharedPreferences sharedpref3 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday3", Context.MODE_PRIVATE);

		count3 = sharedpref3.getInt("note_count", 1000);

		SharedPreferences sharedpref4 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday4", Context.MODE_PRIVATE);

		count4 = sharedpref4.getInt("note_count", 1000);

		SharedPreferences sharedpref5 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday5", Context.MODE_PRIVATE);

		count5 = sharedpref5.getInt("note_count", 1000);

		SharedPreferences sharedpref6 = getActivity().getApplicationContext()
				.getSharedPreferences("tuesday6", Context.MODE_PRIVATE);

		count6 = sharedpref6.getInt("note_count", 1000);

		super.onResume();
		myschedule = new ArrayList<schedule_view>();
		populatescheduleList();
		populateListView();

	}

	private void populatescheduleList() {
		myschedule.add(new schedule_view("H Band", HBand, count0));
		myschedule.add(new schedule_view("G Band", GBand, count1));
		myschedule.add(new schedule_view("B Band", BBand, count2));
		myschedule.add(new schedule_view("E Band", EBand, count3));
		myschedule.add(new schedule_view("D Band", DBand, count4));
		myschedule.add(new schedule_view("C Band", CBand, count5));
		myschedule.add(new schedule_view("A Band", ABand, count6));
	}

	private void populateListView() {
		ArrayAdapter<schedule_view> adapter = new MyListAdapter();
		list = (ListView) getView().findViewById(R.id.listView2);
		
		footer = getActivity().getLayoutInflater().inflate(R.layout.homeworkday_footer,
	             null);
			 
		TextView footer_text = (TextView) footer.findViewById(R.id.textView1);
		
		footer_text.setText("Homework Due Tuesday");
		
		list.addFooterView(footer); 
		
		list.setAdapter(adapter);
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
				return onLongListItemClick(v, pos, id);
			}
		});
	}

	public class MyListAdapter extends ArrayAdapter<schedule_view> {
		public MyListAdapter() {
			super(getActivity(), R.layout.item_view, myschedule);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.schedule_list_item_view, parent, false);
				holder = new ViewHolder();

				schedule_view currenthomeworkdue = myschedule.get(position);

				holder.HomeworkDueText = (TextView) convertView
						.findViewById(R.id.bandText);

				holder.ClassesText = (TextView) convertView
						.findViewById(R.id.textView1);

				holder.NotesCountText = (TextView) convertView
						.findViewById(R.id.note_count);

				View BackGround = (View) convertView
						.findViewById(R.id.note_background);

				TextView Count = (TextView) convertView
						.findViewById(R.id.note_count);

				if (currenthomeworkdue.Note_Number() == 1000) {

					BackGround.setVisibility(View.GONE);

					Count.setVisibility(View.GONE);

				}
				if (currenthomeworkdue.Note_Number() == 0) {

					BackGround.setVisibility(View.GONE);

					Count.setVisibility(View.GONE);

				}
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			schedule_view currenthomeworkdue = myschedule.get(position);

			holder.HomeworkDueText.setText(currenthomeworkdue.Band());

			holder.ClassesText.setText(currenthomeworkdue.Classes());

			holder.NotesCountText.setText(Integer.toString(currenthomeworkdue
					.Note_Number()));

			return convertView;

		}

	}

	private void showNotesDialog() {
		FragmentManager fm = getFragmentManager();
		NotesDialog notesDialog = new NotesDialog();
		notesDialog.show(fm, bandString);
	}

	protected boolean onLongListItemClick(View v, int pos, long id) {

		TextView band = (TextView) v.findViewById(R.id.bandText);

		bandString = band.getText().toString();

		position = pos;

		showNotesDialog();

		return true;
	}

	public static class NotesDialog extends DialogFragment {

		private EnhancedListView mNotes;
		private static RelativeLayout addNote;
		private int position_mainlist;
		private String note_removed;

		public NotesDialog() {
			// Empty constructor required for DialogFragment
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			View view = getActivity().getLayoutInflater().inflate(
					R.layout.note_dialog, null);
			mNotes = (EnhancedListView) view.findViewById(R.id.listViewNotes);
			addNote = (RelativeLayout) view.findViewById(R.id.notesAdd);

			String band_position = ("tuesday" + (Integer.toString(position)))
					.toString();

			position_mainlist = position;

			SharedPreferences sharedpref = getActivity()
					.getApplicationContext().getSharedPreferences(
							band_position, Context.MODE_PRIVATE);

			int counterssss = sharedpref.getInt("note_count", 0);

			int countersssss = counterssss + 1;

			final ArrayList<String> note_list = new ArrayList<String>();

			for (int i = 0; i < countersssss; i++) {

				String note_item = Integer.toString(i);

				String note = sharedpref.getString(note_item, "");

				note_list.add(note);

			}

			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.note_dialog_item, R.id.textView1,
					note_list);

			mNotes.setAdapter(arrayAdapter);

			mNotes.setDismissCallback(new OnDismissCallback() {

				@Override
				public EnhancedListView.Undoable onDismiss(
						EnhancedListView listView, final int position) {

					// Store the item for later undo

					final String item = (String) arrayAdapter.getItem(position);

					// Remove the item from the adapter
					arrayAdapter.remove(item);

					// return an Undoable
					return new EnhancedListView.Undoable() {
						// Reinsert the item to the adapter
						@Override
						public void undo() {
							arrayAdapter.insert(item, position);
						}

						// Return a string for your item

						// Delete item completely from your persistent storage
						@Override
						public void discard() {

							String band_position = ("tuesday" + (Integer
									.toString(position_mainlist))).toString();

							SharedPreferences.Editor localEditor = getActivity()
									.getSharedPreferences(band_position,
											Context.MODE_PRIVATE).edit();

							SharedPreferences sharedpref = getActivity()
									.getApplicationContext()
									.getSharedPreferences(band_position,
											Context.MODE_PRIVATE);

							int counterssss = sharedpref
									.getInt("note_count", 0);

							int countersssss = counterssss + 1;

							int ii = -1;

							for (int i = 0; i < countersssss; i++) {

								String note_item = Integer.toString(i);

								String note = sharedpref.getString(note_item,
										"");

								if (item.equals(note)) {

									System.out.println(note + " 1 " + item);

									String item_positions = Integer.toString(i);

									localEditor.remove(item_positions);

									localEditor.commit();

								} else {

									System.out.println(note + " 2 " + item);

									ii++;

									System.out.println(ii);

									String item_position = Integer.toString(ii);

									localEditor.putString(item_position, note);

								}

							}

							int note_counts = sharedpref
									.getInt("note_count", 0);

							int note_minus = note_counts - 1;

							localEditor.putInt("note_count", note_minus);

							localEditor.commit();

							getActivity().recreate();

						}
					};

				}

			});

			mNotes.enableSwipeToDismiss();

			mNotes.setUndoStyle(UndoStyle.MULTILEVEL_POPUP);

			mNotes.setRequireTouchBeforeDismiss(false);

			mNotes.setUndoHideDelay(3000);

			arrayAdapter.remove("");

			addNote.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					getDialog().dismiss();

					showNoteDialog();
				}
			});

			if (bandString.equals("H Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("8:00-8:55");

			}
			if (bandString.equals("G Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("9:00-10:00");

			}
			if (bandString.equals("B Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("10:05-11:00");

			}
			if (bandString.equals("E Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("11:05-12:00");

			}
			if (bandString.equals("D Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("12:02-12:57");

			}
			if (bandString.equals("C Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("1:02-1:57");

			}
			if (bandString.equals("A Band")) {

				TextView band_times = (TextView) view
						.findViewById(R.id.infotimeView);

				band_times.setText("2:02-2:57");

			}

			builder.setView(view);

			builder.setTitle(bandString);

			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							getDialog().dismiss();
						}
					});

			return builder.create();

		}

		private void showNoteDialog() {
			getDialog().dismiss();
			FragmentManager fm = getFragmentManager();
			NoteDialog noteDialog = new NoteDialog();
			noteDialog.show(fm, bandString);
		}

		public static class NoteDialog extends DialogFragment {

			private EditText mEditText;

			private int Counts;

			public NoteDialog() {
				// Empty constructor required for DialogFragment
			}

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				View view = getActivity().getLayoutInflater().inflate(
						R.layout.add_note_dialog, null);
				mEditText = (EditText) view.findViewById(R.id.editText1);

				addNote.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						getDialog().dismiss();

						NotesDialog notes = new NotesDialog();

						notes.showNoteDialog();
					}
				});

				builder.setView(view);

				builder.setTitle("Add New Note for " + bandString);

				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								getDialog().dismiss();
							}
						});

				builder.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								String band_position = ("tuesday" + (Integer
										.toString(position))).toString();

								SharedPreferences.Editor localEditor = getActivity()
										.getSharedPreferences(band_position,
												Context.MODE_PRIVATE).edit();

								SharedPreferences sharedpref = getActivity()
										.getApplicationContext()
										.getSharedPreferences(band_position,
												Context.MODE_PRIVATE);

								if (sharedpref.contains("note_count")) {

									int Count = sharedpref.getInt("note_count",
											0);

									Counts = Count + 1;

								} else {

									Counts = 1;

								}

								String Note = mEditText.getText().toString();

								String note_counts = Integer.toString(Counts);

								localEditor.putString(note_counts, Note);

								localEditor.putInt("note_count", Counts);

								localEditor.apply();

								getDialog().dismiss();

								getActivity().recreate();

							}
						});

				return builder.create();

			}

		}

	}

	@Override
	public void onStop(){
		super.onStop();
	
		list.removeFooterView(footer);
		
	}
	
}