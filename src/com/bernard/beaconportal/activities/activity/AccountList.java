package com.bernard.beaconportal.activities.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.BaseAccount;
import com.bernard.beaconportal.activities.FontSizes;
import com.bernard.beaconportal.activities.K9;
import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.search.SearchAccount;
import com.bernard.beaconportal.activities.R;

/**
 * Activity displaying the list of accounts.
 * 
 * <p>
 * Classes extending this abstract class have to provide an
 * {@link #onAccountSelected(BaseAccount)} method to perform an action when an
 * account is selected.
 * </p>
 */
public abstract class AccountList extends K9ListActivity implements
		OnItemClickListener {
	private FontSizes mFontSizes = K9.getFontSizes();

	private String actionbar_colors;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setResult(RESULT_CANCELED);

		setContentView(R.layout.account_list);

		ListView listView = getListView();
		listView.setOnItemClickListener(this);
		listView.setItemsCanFocus(false);

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

		bar.setDisplayHomeAsUpEnabled(true);

	}

	/**
	 * Reload list of accounts when this activity is resumed.
	 */
	@Override
	public void onResume() {
		super.onResume();
		new LoadAccounts().execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BaseAccount account = (BaseAccount) parent.getItemAtPosition(position);
		onAccountSelected(account);
	}

	/**
	 * Create a new {@link AccountsAdapter} instance and assign it to the
	 * {@link ListView}.
	 * 
	 * @param realAccounts
	 *            An array of accounts to display.
	 */
	public void populateListView(Account[] realAccounts) {
		List<BaseAccount> accounts = new ArrayList<BaseAccount>();

		if (displaySpecialAccounts() && !K9.isHideSpecialAccounts()) {
			BaseAccount unifiedInboxAccount = SearchAccount
					.createUnifiedInboxAccount(this);
			BaseAccount allMessagesAccount = SearchAccount
					.createAllMessagesAccount(this);

			accounts.add(unifiedInboxAccount);
			accounts.add(allMessagesAccount);
		}

		accounts.addAll(Arrays.asList(realAccounts));
		AccountsAdapter adapter = new AccountsAdapter(accounts);
		ListView listView = getListView();
		listView.setAdapter(adapter);
		listView.invalidate();
	}

	/**
	 * Implementing decide whether or not to display special accounts in the
	 * list.
	 * 
	 * @return {@code true}, if special accounts should be listed. {@code false}
	 *         , otherwise.
	 */
	protected abstract boolean displaySpecialAccounts();

	/**
	 * This method will be called when an account was selected.
	 * 
	 * @param account
	 *            The account the user selected.
	 */
	protected abstract void onAccountSelected(BaseAccount account);

	class AccountsAdapter extends ArrayAdapter<BaseAccount> {
		public AccountsAdapter(List<BaseAccount> accounts) {
			super(AccountList.this, 0, accounts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final BaseAccount account = getItem(position);

			final View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = getLayoutInflater().inflate(R.layout.accounts_item,
						parent, false);
				view.findViewById(R.id.active_icons).setVisibility(View.GONE);
				view.findViewById(R.id.folders).setVisibility(View.GONE);
			}

			AccountViewHolder holder = (AccountViewHolder) view.getTag();
			if (holder == null) {
				holder = new AccountViewHolder();
				holder.description = (TextView) view
						.findViewById(R.id.description);
				holder.email = (TextView) view.findViewById(R.id.email);
				holder.chip = view.findViewById(R.id.chip);

				view.setTag(holder);
			}

			String description = account.getDescription();
			if (account.getEmail().equals(description)) {
				holder.email.setVisibility(View.GONE);
			} else {
				holder.email.setVisibility(View.VISIBLE);
				holder.email.setText(account.getEmail());
			}

			if (description == null || description.isEmpty()) {
				description = account.getEmail();
			}

			holder.description.setText(description);

			if (account instanceof Account) {
				Account realAccount = (Account) account;
				holder.chip.setBackgroundColor(realAccount.getChipColor());
			} else {
				holder.chip.setBackgroundColor(0xff999999);
			}

			holder.chip.getBackground().setAlpha(255);

			mFontSizes.setViewTextSize(holder.description,
					mFontSizes.getAccountName());
			mFontSizes.setViewTextSize(holder.email,
					mFontSizes.getAccountDescription());

			return view;
		}

		class AccountViewHolder {
			public TextView description;
			public TextView email;
			public View chip;
		}
	}

	/**
	 * Load accounts in a background thread
	 */
	class LoadAccounts extends AsyncTask<Void, Void, Account[]> {
		@Override
		protected Account[] doInBackground(Void... params) {
			Account[] accounts = Preferences.getPreferences(
					getApplicationContext()).getAccounts();
			return accounts;
		}

		@Override
		protected void onPostExecute(Account[] accounts) {
			populateListView(accounts);
		}
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home: {

			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);

			return true;
		}

		}
		return true;

	}

}