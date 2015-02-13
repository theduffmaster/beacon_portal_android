package com.bernard.beaconportal.activities.activity.setup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.MAIL.NotificationHideSubject;
import com.bernard.beaconportal.activities.MAIL.NotificationQuickDelete;
import com.bernard.beaconportal.activities.MAIL.SplitViewMode;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.ColorPickerDialog;
import com.bernard.beaconportal.activities.activity.MAILPreferenceActivity;
import com.bernard.beaconportal.activities.controller.MessagingController;
import com.bernard.beaconportal.activities.helper.FileBrowserHelper;
import com.bernard.beaconportal.activities.helper.FileBrowserHelper.FileBrowserFailOverCallback;
import com.bernard.beaconportal.activities.preferences.CheckBoxListPreference;
import com.bernard.beaconportal.activities.preferences.TimePickerPreference;
import com.bernard.beaconportal.activities.service.MailService;
import com.bernard.beaconportal.activities.view.MessageWebView;

public class Prefs extends MAILPreferenceActivity {

	/**
	 * Immutable empty {@link CharSequence} array
	 */
	private static final CharSequence[] EMPTY_CHAR_SEQUENCE_ARRAY = new CharSequence[0];

	/*
	 * Keys of the preferences defined in res/xml/global_preferences.xml
	 */
	private static final String PREFERENCE_LANGUAGE = "language";
	private static final String PREFERENCE_THEME = "theme";
	private static final String PREFERENCE_MESSAGE_VIEW_THEME = "messageViewTheme";
	private static final String PREFERENCE_FIXED_MESSAGE_THEME = "fixedMessageViewTheme";
	private static final String PREFERENCE_COMPOSER_THEME = "messageComposeTheme";
	private static final String PREFERENCE_FONT_SIZE = "font_size";
	private static final String PREFERENCE_ANIMATIONS = "animations";
	private static final String PREFERENCE_GESTURES = "gestures";
	private static final String PREFERENCE_VOLUME_NAVIGATION = "volumeNavigation";
	private static final String PREFERENCE_START_INTEGRATED_INBOX = "start_integrated_inbox";
	private static final String PREFERENCE_CONFIRM_ACTIONS = "confirm_actions";
	private static final String PREFERENCE_NOTIFICATION_HIDE_SUBJECT = "notification_hide_subject";
	private static final String PREFERENCE_MEASURE_ACCOUNTS = "measure_accounts";
	private static final String PREFERENCE_COUNT_SEARCH = "count_search";
	private static final String PREFERENCE_HIDE_SPECIAL_ACCOUNTS = "hide_special_accounts";
	private static final String PREFERENCE_MESSAGELIST_CHECKBOXES = "messagelist_checkboxes";
	private static final String PREFERENCE_MESSAGELIST_PREVIEW_LINES = "messagelist_preview_lines";
	private static final String PREFERENCE_MESSAGELIST_SENDER_ABOVE_SUBJECT = "messagelist_sender_above_subject";
	private static final String PREFERENCE_MESSAGELIST_STARS = "messagelist_stars";
	private static final String PREFERENCE_MESSAGELIST_SHOW_CORRESPONDENT_NAMES = "messagelist_show_correspondent_names";
	private static final String PREFERENCE_MESSAGELIST_SHOW_CONTACT_NAME = "messagelist_show_contact_name";
	private static final String PREFERENCE_MESSAGELIST_CONTACT_NAME_COLOR = "messagelist_contact_name_color";
	private static final String PREFERENCE_MESSAGELIST_SHOW_CONTACT_PICTURE = "messagelist_show_contact_picture";
	private static final String PREFERENCE_MESSAGELIST_COLORIZE_MISSING_CONTACT_PICTURES = "messagelist_colorize_missing_contact_pictures";
	private static final String PREFERENCE_MESSAGEVIEW_FIXEDWIDTH = "messageview_fixedwidth_font";
	private static final String PREFERENCE_MESSAGEVIEW_VISIBLE_REFILE_ACTIONS = "messageview_visible_refile_actions";

	private static final String PREFERENCE_MESSAGEVIEW_RETURN_TO_LIST = "messageview_return_to_list";
	private static final String PREFERENCE_MESSAGEVIEW_SHOW_NEXT = "messageview_show_next";
	private static final String PREFERENCE_QUIET_TIME_ENABLED = "quiet_time_enabled";
	private static final String PREFERENCE_QUIET_TIME_STARTS = "quiet_time_starts";
	private static final String PREFERENCE_QUIET_TIME_ENDS = "quiet_time_ends";
	private static final String PREFERENCE_NOTIF_QUICK_DELETE = "notification_quick_delete";
	private static final String PREFERENCE_HIDE_USERAGENT = "privacy_hide_useragent";
	private static final String PREFERENCE_HIDE_TIMEZONE = "privacy_hide_timezone";

	private static final String PREFERENCE_MESSAGEVIEW_MOBILE_LAYOUT = "messageview_mobile_layout";
	private static final String PREFERENCE_AUTOFIT_WIDTH = "messageview_autofit_width";
	private static final String PREFERENCE_BACKGROUND_OPS = "background_ops";
	private static final String PREFERENCE_GALLERY_BUG_WORKAROUND = "use_gallery_bug_workaround";
	private static final String PREFERENCE_DEBUG_LOGGING = "debug_logging";
	private static final String PREFERENCE_SENSITIVE_LOGGING = "sensitive_logging";

	private static final String PREFERENCE_ATTACHMENT_DEF_PATH = "attachment_default_path";
	private static final String PREFERENCE_BACKGROUND_AS_UNREAD_INDICATOR = "messagelist_background_as_unread_indicator";
	private static final String PREFERENCE_THREADED_VIEW = "threaded_view";
	private static final String PREFERENCE_FOLDERLIST_WRAP_NAME = "folderlist_wrap_folder_name";
	private static final String PREFERENCE_SPLITVIEW_MODE = "splitview_mode";

	private static final int ACTIVITY_CHOOSE_FOLDER = 1;

	// Named indices for the mVisibleRefileActions field
	private static final int VISIBLE_REFILE_ACTIONS_DELETE = 0;
	private static final int VISIBLE_REFILE_ACTIONS_ARCHIVE = 1;
	private static final int VISIBLE_REFILE_ACTIONS_MOVE = 2;
	private static final int VISIBLE_REFILE_ACTIONS_COPY = 3;
	private static final int VISIBLE_REFILE_ACTIONS_SPAM = 4;

	private ListPreference mLanguage;
	private ListPreference mTheme;
	private CheckBoxPreference mFixedMessageTheme;
	private ListPreference mMessageTheme;
	private ListPreference mComposerTheme;
	private CheckBoxPreference mAnimations;
	private CheckBoxPreference mGestures;
	private CheckBoxListPreference mVolumeNavigation;
	private CheckBoxPreference mStartIntegratedInbox;
	private CheckBoxListPreference mConfirmActions;
	private ListPreference mNotificationHideSubject;
	private CheckBoxPreference mMeasureAccounts;
	private CheckBoxPreference mCountSearch;
	private CheckBoxPreference mHideSpecialAccounts;
	private ListPreference mPreviewLines;
	private CheckBoxPreference mSenderAboveSubject;
	private CheckBoxPreference mCheckboxes;
	private CheckBoxPreference mStars;
	private CheckBoxPreference mShowCorrespondentNames;
	private CheckBoxPreference mShowContactName;
	private CheckBoxPreference mChangeContactNameColor;
	private CheckBoxPreference mShowContactPicture;
	private CheckBoxPreference mColorizeMissingContactPictures;
	private CheckBoxPreference mFixedWidth;
	private CheckBoxPreference mReturnToList;
	private CheckBoxPreference mShowNext;
	private CheckBoxPreference mMobileOptimizedLayout;
	private CheckBoxPreference mAutofitWidth;
	private ListPreference mBackgroundOps;
	private CheckBoxPreference mUseGalleryBugWorkaround;
	private CheckBoxPreference mDebugLogging;
	private CheckBoxPreference mSensitiveLogging;
	private CheckBoxPreference mHideUserAgent;
	private CheckBoxPreference mHideTimeZone;
	private CheckBoxPreference mWrapFolderNames;
	private CheckBoxListPreference mVisibleRefileActions;

	private CheckBoxPreference mQuietTimeEnabled;
	private com.bernard.beaconportal.activities.preferences.TimePickerPreference mQuietTimeStarts;
	private com.bernard.beaconportal.activities.preferences.TimePickerPreference mQuietTimeEnds;
	private ListPreference mNotificationQuickDelete;
	private Preference mAttachmentPathPreference;

	private CheckBoxPreference mBackgroundAsUnreadIndicator;
	private CheckBoxPreference mThreadedView;
	private ListPreference mSplitViewMode;

	public static void actionPrefs(Context context) {
		Intent i = new Intent(context, Prefs.class);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.global_preferences);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));
			
			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor("#3367d6"));
	}

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			getActionBar().setBackgroundDrawable(

			new ColorDrawable(Color.parseColor(actionbar_colors)));

			final int splitBarId = getResources().getIdentifier(
					"split_action_bar", "id", "android");

			final View splitActionBar = findViewById(splitBarId);

			if (splitActionBar != null) {

				splitActionBar.setBackgroundDrawable(

				new ColorDrawable(Color.parseColor(actionbar_colors)));

			}

			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor(actionbar_colors));
	}
			
		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		mLanguage = (ListPreference) findPreference(PREFERENCE_LANGUAGE);
		List<CharSequence> entryVector = new ArrayList<CharSequence>(
				Arrays.asList(mLanguage.getEntries()));
		List<CharSequence> entryValueVector = new ArrayList<CharSequence>(
				Arrays.asList(mLanguage.getEntryValues()));
		String supportedLanguages[] = getResources().getStringArray(
				R.array.supported_languages);
		Set<String> supportedLanguageSet = new HashSet<String>(
				Arrays.asList(supportedLanguages));
		for (int i = entryVector.size() - 1; i > -1; --i) {
			if (!supportedLanguageSet.contains(entryValueVector.get(i))) {
				entryVector.remove(i);
				entryValueVector.remove(i);
			}
		}
		initListPreference(mLanguage, MAIL.getMAILLanguage(),
				entryVector.toArray(EMPTY_CHAR_SEQUENCE_ARRAY),
				entryValueVector.toArray(EMPTY_CHAR_SEQUENCE_ARRAY));

		mTheme = setupListPreference(PREFERENCE_THEME,
				themeIdToName(MAIL.getMAILTheme()));
		mFixedMessageTheme = (CheckBoxPreference) findPreference(PREFERENCE_FIXED_MESSAGE_THEME);
		mFixedMessageTheme.setChecked(MAIL.useFixedMessageViewTheme());
		mMessageTheme = setupListPreference(PREFERENCE_MESSAGE_VIEW_THEME,
				themeIdToName(MAIL.getMAILMessageViewThemeSetting()));
		mComposerTheme = setupListPreference(PREFERENCE_COMPOSER_THEME,
				themeIdToName(MAIL.getMAILComposerThemeSetting()));

		findPreference(PREFERENCE_FONT_SIZE).setOnPreferenceClickListener(
				new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						onFontSizeSettings();
						return true;
					}
				});

		mAnimations = (CheckBoxPreference) findPreference(PREFERENCE_ANIMATIONS);
		mAnimations.setChecked(MAIL.showAnimations());

		mGestures = (CheckBoxPreference) findPreference(PREFERENCE_GESTURES);
		mGestures.setChecked(MAIL.gesturesEnabled());

		mVolumeNavigation = (CheckBoxListPreference) findPreference(PREFERENCE_VOLUME_NAVIGATION);
		mVolumeNavigation.setItems(new CharSequence[] {
				getString(R.string.volume_navigation_message),
				getString(R.string.volume_navigation_list) });
		mVolumeNavigation.setCheckedItems(new boolean[] {
				MAIL.useVolumeKeysForNavigationEnabled(),
				MAIL.useVolumeKeysForListNavigationEnabled() });

		mStartIntegratedInbox = (CheckBoxPreference) findPreference(PREFERENCE_START_INTEGRATED_INBOX);
		mStartIntegratedInbox.setChecked(MAIL.startIntegratedInbox());

		mConfirmActions = (CheckBoxListPreference) findPreference(PREFERENCE_CONFIRM_ACTIONS);

		boolean canDeleteFromNotification = MessagingController
				.platformSupportsExtendedNotifications();
		CharSequence[] confirmActionEntries = new CharSequence[canDeleteFromNotification ? 4
				: 3];
		boolean[] confirmActionValues = new boolean[canDeleteFromNotification ? 4
				: 3];
		int index = 0;

		confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete);
		confirmActionValues[index++] = MAIL.confirmDelete();
		confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete_starred);
		confirmActionValues[index++] = MAIL.confirmDeleteStarred();
		if (canDeleteFromNotification) {
			confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete_notif);
			confirmActionValues[index++] = MAIL.confirmDeleteFromNotification();
		}
		confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_spam);
		confirmActionValues[index++] = MAIL.confirmSpam();

		mConfirmActions.setItems(confirmActionEntries);
		mConfirmActions.setCheckedItems(confirmActionValues);

		mNotificationHideSubject = setupListPreference(
				PREFERENCE_NOTIFICATION_HIDE_SUBJECT, MAIL
						.getNotificationHideSubject().toString());

		mMeasureAccounts = (CheckBoxPreference) findPreference(PREFERENCE_MEASURE_ACCOUNTS);
		mMeasureAccounts.setChecked(MAIL.measureAccounts());

		mCountSearch = (CheckBoxPreference) findPreference(PREFERENCE_COUNT_SEARCH);
		mCountSearch.setChecked(MAIL.countSearchMessages());

		mHideSpecialAccounts = (CheckBoxPreference) findPreference(PREFERENCE_HIDE_SPECIAL_ACCOUNTS);
		mHideSpecialAccounts.setChecked(MAIL.isHideSpecialAccounts());

		mPreviewLines = setupListPreference(
				PREFERENCE_MESSAGELIST_PREVIEW_LINES,
				Integer.toString(MAIL.messageListPreviewLines()));

		mSenderAboveSubject = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_SENDER_ABOVE_SUBJECT);
		mSenderAboveSubject.setChecked(MAIL.messageListSenderAboveSubject());
		mCheckboxes = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_CHECKBOXES);
		mCheckboxes.setChecked(MAIL.messageListCheckboxes());

		mStars = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_STARS);
		mStars.setChecked(MAIL.messageListStars());

		mShowCorrespondentNames = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_SHOW_CORRESPONDENT_NAMES);
		mShowCorrespondentNames.setChecked(MAIL.showCorrespondentNames());

		mShowContactName = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_SHOW_CONTACT_NAME);
		mShowContactName.setChecked(MAIL.showContactName());

		mShowContactPicture = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_SHOW_CONTACT_PICTURE);
		mShowContactPicture.setChecked(MAIL.showContactPicture());

		mColorizeMissingContactPictures = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_COLORIZE_MISSING_CONTACT_PICTURES);
		mColorizeMissingContactPictures.setChecked(MAIL
				.isColorizeMissingContactPictures());

		mBackgroundAsUnreadIndicator = (CheckBoxPreference) findPreference(PREFERENCE_BACKGROUND_AS_UNREAD_INDICATOR);
		mBackgroundAsUnreadIndicator.setChecked(MAIL
				.useBackgroundAsUnreadIndicator());

		mChangeContactNameColor = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGELIST_CONTACT_NAME_COLOR);
		mChangeContactNameColor.setChecked(MAIL.changeContactNameColor());

		mThreadedView = (CheckBoxPreference) findPreference(PREFERENCE_THREADED_VIEW);
		mThreadedView.setChecked(MAIL.isThreadedViewEnabled());

		if (MAIL.changeContactNameColor()) {
			mChangeContactNameColor
					.setSummary(R.string.global_settings_registered_name_color_changed);
		} else {
			mChangeContactNameColor
					.setSummary(R.string.global_settings_registered_name_color_default);
		}
		mChangeContactNameColor
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final Boolean checked = (Boolean) newValue;
						if (checked) {
							onChooseContactNameColor();
							mChangeContactNameColor
									.setSummary(R.string.global_settings_registered_name_color_changed);
						} else {
							mChangeContactNameColor
									.setSummary(R.string.global_settings_registered_name_color_default);
						}
						mChangeContactNameColor.setChecked(checked);
						return false;
					}
				});

		mFixedWidth = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_FIXEDWIDTH);
		mFixedWidth.setChecked(MAIL.messageViewFixedWidthFont());

		mReturnToList = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_RETURN_TO_LIST);
		mReturnToList.setChecked(MAIL.messageViewReturnToList());

		mShowNext = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_SHOW_NEXT);
		mShowNext.setChecked(MAIL.messageViewShowNext());

		mMobileOptimizedLayout = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_MOBILE_LAYOUT);
		if (!MessageWebView.isSingleColumnLayoutSupported()) {
			PreferenceCategory prefs = (PreferenceCategory) findPreference("messageview_preferences");
			prefs.removePreference(mMobileOptimizedLayout);
		} else {
			mMobileOptimizedLayout.setChecked(MAIL.mobileOptimizedLayout());
		}

		mAutofitWidth = (CheckBoxPreference) findPreference(PREFERENCE_AUTOFIT_WIDTH);
		mAutofitWidth.setChecked(MAIL.autofitWidth());

		mQuietTimeEnabled = (CheckBoxPreference) findPreference(PREFERENCE_QUIET_TIME_ENABLED);
		mQuietTimeEnabled.setChecked(MAIL.getQuietTimeEnabled());

		mQuietTimeStarts = (TimePickerPreference) findPreference(PREFERENCE_QUIET_TIME_STARTS);
		mQuietTimeStarts.setDefaultValue(MAIL.getQuietTimeStarts());
		mQuietTimeStarts.setSummary(MAIL.getQuietTimeStarts());
		mQuietTimeStarts
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String time = (String) newValue;
						mQuietTimeStarts.setSummary(time);
						return false;
					}
				});

		mQuietTimeEnds = (TimePickerPreference) findPreference(PREFERENCE_QUIET_TIME_ENDS);
		mQuietTimeEnds.setSummary(MAIL.getQuietTimeEnds());
		mQuietTimeEnds.setDefaultValue(MAIL.getQuietTimeEnds());
		mQuietTimeEnds
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String time = (String) newValue;
						mQuietTimeEnds.setSummary(time);
						return false;
					}
				});

		mNotificationQuickDelete = setupListPreference(
				PREFERENCE_NOTIF_QUICK_DELETE, MAIL
						.getNotificationQuickDeleteBehaviour().toString());
		if (!MessagingController.platformSupportsExtendedNotifications()) {
			PreferenceScreen prefs = (PreferenceScreen) findPreference("notification_preferences");
			prefs.removePreference(mNotificationQuickDelete);
			mNotificationQuickDelete = null;
		}

		mBackgroundOps = setupListPreference(PREFERENCE_BACKGROUND_OPS, MAIL
				.getBackgroundOps().toString());
		// In ICS+ there is no 'background data' setting that apps can chose to
		// ignore anymore. So
		// we hide that option for "Background Sync".
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			CharSequence[] oldEntries = mBackgroundOps.getEntries();
			CharSequence[] newEntries = new CharSequence[3];
			// Use "When 'Auto-sync' is checked" instead of "When 'Background
			// data' & 'Auto-sync'
			// are checked" as description.
			newEntries[0] = getString(R.string.background_ops_auto_sync_only);
			newEntries[1] = oldEntries[2];
			newEntries[2] = oldEntries[3];

			CharSequence[] oldValues = mBackgroundOps.getEntryValues();
			CharSequence[] newValues = new CharSequence[3];
			newValues[0] = oldValues[1];
			newValues[1] = oldValues[2];
			newValues[2] = oldValues[3];

			mBackgroundOps.setEntries(newEntries);
			mBackgroundOps.setEntryValues(newValues);

			// Since ConnectivityManager.getBackgroundDataSetting() always
			// returns 'true' on ICS+
			// we map WHEN_CHECKED to ALWAYS.
			if (MAIL.getBackgroundOps() == MAIL.BACKGROUND_OPS.WHEN_CHECKED) {
				mBackgroundOps.setValue(MAIL.BACKGROUND_OPS.ALWAYS.toString());
				mBackgroundOps.setSummary(mBackgroundOps.getEntry());
			}
		}

		mUseGalleryBugWorkaround = (CheckBoxPreference) findPreference(PREFERENCE_GALLERY_BUG_WORKAROUND);
		mUseGalleryBugWorkaround.setChecked(MAIL.useGalleryBugWorkaround());

		mDebugLogging = (CheckBoxPreference) findPreference(PREFERENCE_DEBUG_LOGGING);
		mSensitiveLogging = (CheckBoxPreference) findPreference(PREFERENCE_SENSITIVE_LOGGING);
		mHideUserAgent = (CheckBoxPreference) findPreference(PREFERENCE_HIDE_USERAGENT);
		mHideTimeZone = (CheckBoxPreference) findPreference(PREFERENCE_HIDE_TIMEZONE);

		mDebugLogging.setChecked(MAIL.DEBUG);
		mSensitiveLogging.setChecked(MAIL.DEBUG_SENSITIVE);
		mHideUserAgent.setChecked(MAIL.hideUserAgent());
		mHideTimeZone.setChecked(MAIL.hideTimeZone());

		mAttachmentPathPreference = findPreference(PREFERENCE_ATTACHMENT_DEF_PATH);
		mAttachmentPathPreference.setSummary(MAIL.getAttachmentDefaultPath());
		mAttachmentPathPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						FileBrowserHelper
								.getInstance()
								.showFileBrowserActivity(
										Prefs.this,
										new File(MAIL.getAttachmentDefaultPath()),
										ACTIVITY_CHOOSE_FOLDER, callback);

						return true;
					}

					FileBrowserFailOverCallback callback = new FileBrowserFailOverCallback() {

						@Override
						public void onPathEntered(String path) {
							mAttachmentPathPreference.setSummary(path);
							MAIL.setAttachmentDefaultPath(path);
						}

						@Override
						public void onCancel() {
							// canceled, do nothing
						}
					};
				});

		mWrapFolderNames = (CheckBoxPreference) findPreference(PREFERENCE_FOLDERLIST_WRAP_NAME);
		mWrapFolderNames.setChecked(MAIL.wrapFolderNames());

		mVisibleRefileActions = (CheckBoxListPreference) findPreference(PREFERENCE_MESSAGEVIEW_VISIBLE_REFILE_ACTIONS);
		CharSequence[] visibleRefileActionsEntries = new CharSequence[5];
		visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_DELETE] = getString(R.string.delete_action);
		visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_ARCHIVE] = getString(R.string.archive_action);
		visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_MOVE] = getString(R.string.move_action);
		visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_COPY] = getString(R.string.copy_action);
		visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_SPAM] = getString(R.string.spam_action);

		boolean[] visibleRefileActionsValues = new boolean[5];
		visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_DELETE] = MAIL
				.isMessageViewDeleteActionVisible();
		visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_ARCHIVE] = MAIL
				.isMessageViewArchiveActionVisible();
		visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_MOVE] = MAIL
				.isMessageViewMoveActionVisible();
		visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_COPY] = MAIL
				.isMessageViewCopyActionVisible();
		visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_SPAM] = MAIL
				.isMessageViewSpamActionVisible();

		mVisibleRefileActions.setItems(visibleRefileActionsEntries);
		mVisibleRefileActions.setCheckedItems(visibleRefileActionsValues);

		mSplitViewMode = (ListPreference) findPreference(PREFERENCE_SPLITVIEW_MODE);
		initListPreference(mSplitViewMode, MAIL.getSplitViewMode().name(),
				mSplitViewMode.getEntries(), mSplitViewMode.getEntryValues());
	}

	private static String themeIdToName(MAIL.Theme theme) {
		switch (theme) {
		case DARK:
			return "dark";
		case USE_GLOBAL:
			return "global";
		default:
			return "light";
		}
	}

	private static MAIL.Theme themeNameToId(String theme) {
		if (TextUtils.equals(theme, "dark")) {
			return MAIL.Theme.DARK;
		} else if (TextUtils.equals(theme, "global")) {
			return MAIL.Theme.USE_GLOBAL;
		} else {
			return MAIL.Theme.LIGHT;
		}
	}

	private void saveSettings() {
		SharedPreferences preferences = Preferences.getPreferences(this)
				.getPreferences();

		MAIL.setMAILLanguage(mLanguage.getValue());

		MAIL.setMAILTheme(themeNameToId(mTheme.getValue()));
		MAIL.setUseFixedMessageViewTheme(mFixedMessageTheme.isChecked());
		MAIL.setMAILMessageViewThemeSetting(themeNameToId(mMessageTheme.getValue()));
		MAIL.setMAILComposerThemeSetting(themeNameToId(mComposerTheme.getValue()));

		MAIL.setAnimations(mAnimations.isChecked());
		MAIL.setGesturesEnabled(mGestures.isChecked());
		MAIL.setUseVolumeKeysForNavigation(mVolumeNavigation.getCheckedItems()[0]);
		MAIL.setUseVolumeKeysForListNavigation(mVolumeNavigation
				.getCheckedItems()[1]);
		MAIL.setStartIntegratedInbox(!mHideSpecialAccounts.isChecked()
				&& mStartIntegratedInbox.isChecked());
		MAIL.setNotificationHideSubject(NotificationHideSubject
				.valueOf(mNotificationHideSubject.getValue()));

		int index = 0;
		MAIL.setConfirmDelete(mConfirmActions.getCheckedItems()[index++]);
		MAIL.setConfirmDeleteStarred(mConfirmActions.getCheckedItems()[index++]);
		if (MessagingController.platformSupportsExtendedNotifications()) {
			MAIL.setConfirmDeleteFromNotification(mConfirmActions
					.getCheckedItems()[index++]);
		}
		MAIL.setConfirmSpam(mConfirmActions.getCheckedItems()[index++]);

		MAIL.setMeasureAccounts(mMeasureAccounts.isChecked());
		MAIL.setCountSearchMessages(mCountSearch.isChecked());
		MAIL.setHideSpecialAccounts(mHideSpecialAccounts.isChecked());
		MAIL.setMessageListPreviewLines(Integer.parseInt(mPreviewLines.getValue()));
		MAIL.setMessageListCheckboxes(mCheckboxes.isChecked());
		MAIL.setMessageListStars(mStars.isChecked());
		MAIL.setShowCorrespondentNames(mShowCorrespondentNames.isChecked());
		MAIL.setMessageListSenderAboveSubject(mSenderAboveSubject.isChecked());
		MAIL.setShowContactName(mShowContactName.isChecked());
		MAIL.setShowContactPicture(mShowContactPicture.isChecked());
		MAIL.setColorizeMissingContactPictures(mColorizeMissingContactPictures
				.isChecked());
		MAIL.setUseBackgroundAsUnreadIndicator(mBackgroundAsUnreadIndicator
				.isChecked());
		MAIL.setThreadedViewEnabled(mThreadedView.isChecked());
		MAIL.setChangeContactNameColor(mChangeContactNameColor.isChecked());
		MAIL.setMessageViewFixedWidthFont(mFixedWidth.isChecked());
		MAIL.setMessageViewReturnToList(mReturnToList.isChecked());
		MAIL.setMessageViewShowNext(mShowNext.isChecked());
		MAIL.setMobileOptimizedLayout(mMobileOptimizedLayout.isChecked());
		MAIL.setAutofitWidth(mAutofitWidth.isChecked());
		MAIL.setQuietTimeEnabled(mQuietTimeEnabled.isChecked());

		boolean[] enabledRefileActions = mVisibleRefileActions
				.getCheckedItems();
		MAIL.setMessageViewDeleteActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_DELETE]);
		MAIL.setMessageViewArchiveActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_ARCHIVE]);
		MAIL.setMessageViewMoveActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_MOVE]);
		MAIL.setMessageViewCopyActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_COPY]);
		MAIL.setMessageViewSpamActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_SPAM]);

		MAIL.setQuietTimeStarts(mQuietTimeStarts.getTime());
		MAIL.setQuietTimeEnds(mQuietTimeEnds.getTime());
		MAIL.setWrapFolderNames(mWrapFolderNames.isChecked());

		if (mNotificationQuickDelete != null) {
			MAIL.setNotificationQuickDeleteBehaviour(NotificationQuickDelete
					.valueOf(mNotificationQuickDelete.getValue()));
		}

		MAIL.setSplitViewMode(SplitViewMode.valueOf(mSplitViewMode.getValue()));
		MAIL.setAttachmentDefaultPath(mAttachmentPathPreference.getSummary()
				.toString());
		boolean needsRefresh = MAIL.setBackgroundOps(mBackgroundOps.getValue());
		MAIL.setUseGalleryBugWorkaround(mUseGalleryBugWorkaround.isChecked());

		if (!MAIL.DEBUG && mDebugLogging.isChecked()) {
			Toast.makeText(this, R.string.debug_logging_enabled,
					Toast.LENGTH_LONG).show();
		}
		MAIL.DEBUG = mDebugLogging.isChecked();
		MAIL.DEBUG_SENSITIVE = mSensitiveLogging.isChecked();
		MAIL.setHideUserAgent(mHideUserAgent.isChecked());
		MAIL.setHideTimeZone(mHideTimeZone.isChecked());

		Editor editor = preferences.edit();
		MAIL.save(editor);
		editor.commit();

		if (needsRefresh) {
			MailService.actionReset(this, null);
		}
	}

	@Override
	protected void onPause() {
		saveSettings();
		super.onPause();
	}

	private void onFontSizeSettings() {
		FontSizeSettings.actionEditSettings(this);
	}

	private void onChooseContactNameColor() {
		new ColorPickerDialog(this,
				new ColorPickerDialog.OnColorChangedListener() {
					@Override
					public void colorChanged(int color) {
						MAIL.setContactNameColor(color);
					}
				}, MAIL.getContactNameColor()).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_CHOOSE_FOLDER:
			if (resultCode == RESULT_OK && data != null) {
				// obtain the filename
				Uri fileUri = data.getData();
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					if (filePath != null) {
						mAttachmentPathPreference.setSummary(filePath
								.toString());
						MAIL.setAttachmentDefaultPath(filePath.toString());
					}
				}
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
