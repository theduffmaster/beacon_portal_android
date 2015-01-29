package com.bernard.beaconportal.activities.preferences;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.os.Environment;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.Account.SortType;
import com.bernard.beaconportal.activities.FontSizes;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.MAIL.NotificationHideSubject;
import com.bernard.beaconportal.activities.MAIL.SplitViewMode;
import com.bernard.beaconportal.activities.MAIL.Theme;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.preferences.Settings.BooleanSetting;
import com.bernard.beaconportal.activities.preferences.Settings.ColorSetting;
import com.bernard.beaconportal.activities.preferences.Settings.EnumSetting;
import com.bernard.beaconportal.activities.preferences.Settings.FontSizeSetting;
import com.bernard.beaconportal.activities.preferences.Settings.IntegerRangeSetting;
import com.bernard.beaconportal.activities.preferences.Settings.InvalidSettingValueException;
import com.bernard.beaconportal.activities.preferences.Settings.PseudoEnumSetting;
import com.bernard.beaconportal.activities.preferences.Settings.SettingsDescription;
import com.bernard.beaconportal.activities.preferences.Settings.SettingsUpgrader;
import com.bernard.beaconportal.activities.preferences.Settings.V;
import com.bernard.beaconportal.activities.preferences.Settings.WebFontSizeSetting;

public class GlobalSettings {
	public static final Map<String, TreeMap<Integer, SettingsDescription>> SETTINGS;
	public static final Map<Integer, SettingsUpgrader> UPGRADERS;

	static {
		Map<String, TreeMap<Integer, SettingsDescription>> s = new LinkedHashMap<String, TreeMap<Integer, SettingsDescription>>();

		/**
		 * When adding new settings here, be sure to increment
		 * {@link Settings.VERSION} and use that for whatever you add here.
		 */

		s.put("animations",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("attachmentdefaultpath", Settings.versions(new V(1,
				new DirectorySetting(Environment.getExternalStorageDirectory()
						.toString()))));
		s.put("backgroundOperations", Settings.versions(new V(1,
				new EnumSetting<MAIL.BACKGROUND_OPS>(MAIL.BACKGROUND_OPS.class,
						MAIL.BACKGROUND_OPS.WHEN_CHECKED))));
		s.put("changeRegisteredNameColor",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("confirmDelete",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("confirmDeleteStarred",
				Settings.versions(new V(2, new BooleanSetting(false))));
		s.put("confirmSpam",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("countSearchMessages",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("enableDebugLogging",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("enableSensitiveLogging",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("fontSizeAccountDescription", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeAccountName", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeFolderName", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeFolderStatus", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageComposeInput", Settings.versions(new V(5,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageListDate", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageListPreview", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageListSender", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageListSubject", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewAdditionalHeaders", Settings.versions(new V(
				1, new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewCC", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewContent", Settings.versions(new V(1,
				new WebFontSizeSetting(3)), new V(31, null)));
		s.put("fontSizeMessageViewDate", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewSender", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewSubject", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewTime", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("fontSizeMessageViewTo", Settings.versions(new V(1,
				new FontSizeSetting(FontSizes.FONT_DEFAULT))));
		s.put("gesturesEnabled", Settings.versions(new V(1, new BooleanSetting(
				true)), new V(4, new BooleanSetting(false))));
		s.put("hideSpecialAccounts",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("keyguardPrivacy", Settings.versions(new V(1, new BooleanSetting(
				false)), new V(12, null)));
		s.put("language", Settings.versions(new V(1, new LanguageSetting())));
		s.put("measureAccounts",
				Settings.versions(new V(1, new BooleanSetting(true))));
		s.put("messageListCheckboxes",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("messageListPreviewLines",
				Settings.versions(new V(1, new IntegerRangeSetting(1, 100, 2))));
		s.put("messageListStars",
				Settings.versions(new V(1, new BooleanSetting(true))));
		s.put("messageViewFixedWidthFont",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("messageViewReturnToList",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("messageViewShowNext",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("mobileOptimizedLayout",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("quietTimeEnabled",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("quietTimeEnds",
				Settings.versions(new V(1, new TimeSetting("7:00"))));
		s.put("quietTimeStarts",
				Settings.versions(new V(1, new TimeSetting("21:00"))));
		s.put("registeredNameColor",
				Settings.versions(new V(1, new ColorSetting(0xFF00008F))));
		s.put("showContactName",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("showCorrespondentNames",
				Settings.versions(new V(1, new BooleanSetting(true))));
		s.put("sortTypeEnum", Settings.versions(new V(10,
				new EnumSetting<SortType>(SortType.class,
						Account.DEFAULT_SORT_TYPE))));
		s.put("sortAscending", Settings.versions(new V(10, new BooleanSetting(
				Account.DEFAULT_SORT_ASCENDING))));
		s.put("startIntegratedInbox",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("theme",
				Settings.versions(new V(1, new ThemeSetting(MAIL.Theme.LIGHT))));
		s.put("messageViewTheme", Settings.versions(new V(16, new ThemeSetting(
				MAIL.Theme.LIGHT)), new V(24, new SubThemeSetting(
				MAIL.Theme.USE_GLOBAL))));
		s.put("useGalleryBugWorkaround",
				Settings.versions(new V(1, new GalleryBugWorkaroundSetting())));
		s.put("useVolumeKeysForListNavigation",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("useVolumeKeysForNavigation",
				Settings.versions(new V(1, new BooleanSetting(false))));
		s.put("wrapFolderNames",
				Settings.versions(new V(22, new BooleanSetting(false))));
		s.put("notificationHideSubject", Settings.versions(new V(12,
				new EnumSetting<NotificationHideSubject>(
						NotificationHideSubject.class,
						NotificationHideSubject.NEVER))));
		s.put("useBackgroundAsUnreadIndicator",
				Settings.versions(new V(19, new BooleanSetting(true))));
		s.put("threadedView",
				Settings.versions(new V(20, new BooleanSetting(true))));
		s.put("splitViewMode", Settings.versions(new V(23,
				new EnumSetting<SplitViewMode>(SplitViewMode.class,
						SplitViewMode.NEVER))));
		s.put("messageComposeTheme", Settings.versions(new V(24,
				new SubThemeSetting(MAIL.Theme.USE_GLOBAL))));
		s.put("fixedMessageViewTheme",
				Settings.versions(new V(24, new BooleanSetting(true))));
		s.put("showContactPicture",
				Settings.versions(new V(25, new BooleanSetting(true))));
		s.put("autofitWidth",
				Settings.versions(new V(28, new BooleanSetting(true))));
		s.put("colorizeMissingContactPictures",
				Settings.versions(new V(29, new BooleanSetting(true))));
		s.put("messageViewDeleteActionVisible",
				Settings.versions(new V(30, new BooleanSetting(true))));
		s.put("messageViewArchiveActionVisible",
				Settings.versions(new V(30, new BooleanSetting(false))));
		s.put("messageViewMoveActionVisible",
				Settings.versions(new V(30, new BooleanSetting(false))));
		s.put("messageViewCopyActionVisible",
				Settings.versions(new V(30, new BooleanSetting(false))));
		s.put("messageViewSpamActionVisible",
				Settings.versions(new V(30, new BooleanSetting(false))));
		s.put("fontSizeMessageViewContentPercent", Settings.versions(new V(31,
				new IntegerRangeSetting(40, 250, 100))));
		s.put("hideUserAgent",
				Settings.versions(new V(32, new BooleanSetting(false))));
		s.put("hideTimeZone",
				Settings.versions(new V(32, new BooleanSetting(false))));

		SETTINGS = Collections.unmodifiableMap(s);

		Map<Integer, SettingsUpgrader> u = new HashMap<Integer, SettingsUpgrader>();
		u.put(12, new SettingsUpgraderV12());
		u.put(24, new SettingsUpgraderV24());
		u.put(31, new SettingsUpgraderV31());

		UPGRADERS = Collections.unmodifiableMap(u);
	}

	public static Map<String, Object> validate(int version,
			Map<String, String> importedSettings) {
		return Settings.validate(version, SETTINGS, importedSettings, false);
	}

	public static Set<String> upgrade(int version,
			Map<String, Object> validatedSettings) {
		return Settings
				.upgrade(version, UPGRADERS, SETTINGS, validatedSettings);
	}

	public static Map<String, String> convert(Map<String, Object> settings) {
		return Settings.convert(settings, SETTINGS);
	}

	public static Map<String, String> getGlobalSettings(
			SharedPreferences storage) {
		Map<String, String> result = new HashMap<String, String>();
		for (String key : SETTINGS.keySet()) {
			String value = storage.getString(key, null);
			if (value != null) {
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * Upgrades the settings from version 11 to 12
	 * 
	 * Map the 'keyguardPrivacy' value to the new NotificationHideSubject enum.
	 */
	public static class SettingsUpgraderV12 implements SettingsUpgrader {

		@Override
		public Set<String> upgrade(Map<String, Object> settings) {
			Boolean keyguardPrivacy = (Boolean) settings.get("keyguardPrivacy");
			if (keyguardPrivacy != null && keyguardPrivacy.booleanValue()) {
				// current setting: only show subject when unlocked
				settings.put("notificationHideSubject",
						NotificationHideSubject.WHEN_LOCKED);
			} else {
				// always show subject [old default]
				settings.put("notificationHideSubject",
						NotificationHideSubject.NEVER);
			}
			return new HashSet<String>(Arrays.asList("keyguardPrivacy"));
		}
	}

	/**
	 * Upgrades the settings from version 23 to 24.
	 * 
	 * <p>
	 * Set <em>messageViewTheme</em> to {@link MAIL.Theme#USE_GLOBAL} if
	 * <em>messageViewTheme</em> has the same value as <em>theme</em>.
	 * </p>
	 */
	public static class SettingsUpgraderV24 implements SettingsUpgrader {

		@Override
		public Set<String> upgrade(Map<String, Object> settings) {
			MAIL.Theme messageViewTheme = (MAIL.Theme) settings
					.get("messageViewTheme");
			MAIL.Theme theme = (MAIL.Theme) settings.get("theme");
			if (theme != null && messageViewTheme != null
					&& theme == messageViewTheme) {
				settings.put("messageViewTheme", MAIL.Theme.USE_GLOBAL);
			}

			return null;
		}
	}

	/**
	 * Upgrades the settings from version 30 to 31.
	 * 
	 * <p>
	 * Convert value from <em>fontSizeMessageViewContent</em> to
	 * <em>fontSizeMessageViewContentPercent</em>.
	 * </p>
	 */
	public static class SettingsUpgraderV31 implements SettingsUpgrader {

		@Override
		public Set<String> upgrade(Map<String, Object> settings) {
			int oldSize = ((Integer) settings.get("fontSizeMessageViewContent"))
					.intValue();

			int newSize = convertFromOldSize(oldSize);

			settings.put("fontSizeMessageViewContentPercent", newSize);

			return new HashSet<String>(
					Arrays.asList("fontSizeMessageViewContent"));
		}

		public static int convertFromOldSize(int oldSize) {
			switch (oldSize) {
			case 1: {
				return 40;
			}
			case 2: {
				return 75;
			}
			case 4: {
				return 175;
			}
			case 5: {
				return 250;
			}
			case 3:
			default: {
				return 100;
			}
			}
		}
	}

	/**
	 * The gallery bug work-around setting.
	 * 
	 * <p>
	 * The default value varies depending on whether you have a version of
	 * Gallery 3D installed that contains the bug we work around.
	 * </p>
	 * 
	 * @see MAIL#isGalleryBuggy()
	 */
	public static class GalleryBugWorkaroundSetting extends BooleanSetting {
		public GalleryBugWorkaroundSetting() {
			super(false);
		}

		@Override
		public Object getDefaultValue() {
			return MAIL.isGalleryBuggy();
		}
	}

	/**
	 * The language setting.
	 * 
	 * <p>
	 * Valid values are read from {@code settings_language_values} in
	 * {@code res/values/arrays.xml}.
	 * </p>
	 */
	public static class LanguageSetting extends PseudoEnumSetting<String> {
		private final Map<String, String> mMapping;

		public LanguageSetting() {
			super("");

			Map<String, String> mapping = new HashMap<String, String>();
			String[] values = MAIL.app.getResources().getStringArray(
					R.array.settings_language_values);
			for (String value : values) {
				if (value.length() == 0) {
					mapping.put("", "default");
				} else {
					mapping.put(value, value);
				}
			}
			mMapping = Collections.unmodifiableMap(mapping);
		}

		@Override
		protected Map<String, String> getMapping() {
			return mMapping;
		}

		@Override
		public Object fromString(String value)
				throws InvalidSettingValueException {
			if (mMapping.containsKey(value)) {
				return value;
			}

			throw new InvalidSettingValueException();
		}
	}

	/**
	 * The theme setting.
	 */
	public static class ThemeSetting extends SettingsDescription {
		private static final String THEME_LIGHT = "light";
		private static final String THEME_DARK = "dark";

		public ThemeSetting(MAIL.Theme defaultValue) {
			super(defaultValue);
		}

		@Override
		public Object fromString(String value)
				throws InvalidSettingValueException {
			try {
				Integer theme = Integer.parseInt(value);
				if (theme == MAIL.Theme.LIGHT.ordinal() ||
				// We used to store the resource ID of the theme in the
				// preference storage,
				// but don't use the database upgrade mechanism to update the
				// values. So
				// we have to deal with the old format here.
						theme == android.R.style.Theme_Light) {
					return MAIL.Theme.LIGHT;
				} else if (theme == MAIL.Theme.DARK.ordinal()
						|| theme == android.R.style.Theme) {
					return MAIL.Theme.DARK;
				}
			} catch (NumberFormatException e) { /* do nothing */
			}

			throw new InvalidSettingValueException();
		}

		@Override
		public Object fromPrettyString(String value)
				throws InvalidSettingValueException {
			if (THEME_LIGHT.equals(value)) {
				return MAIL.Theme.LIGHT;
			} else if (THEME_DARK.equals(value)) {
				return MAIL.Theme.DARK;
			}

			throw new InvalidSettingValueException();
		}

		@Override
		public String toPrettyString(Object value) {
			switch ((MAIL.Theme) value) {
			case DARK: {
				return THEME_DARK;
			}
			default: {
				return THEME_LIGHT;
			}
			}
		}

		@Override
		public String toString(Object value) {
			return Integer.toString(((MAIL.Theme) value).ordinal());
		}
	}

	/**
	 * The message view theme setting.
	 */
	public static class SubThemeSetting extends ThemeSetting {
		private static final String THEME_USE_GLOBAL = "use_global";

		public SubThemeSetting(Theme defaultValue) {
			super(defaultValue);
		}

		@Override
		public Object fromString(String value)
				throws InvalidSettingValueException {
			try {
				Integer theme = Integer.parseInt(value);
				if (theme == MAIL.Theme.USE_GLOBAL.ordinal()) {
					return MAIL.Theme.USE_GLOBAL;
				}

				return super.fromString(value);
			} catch (NumberFormatException e) {
				throw new InvalidSettingValueException();
			}
		}

		@Override
		public Object fromPrettyString(String value)
				throws InvalidSettingValueException {
			if (THEME_USE_GLOBAL.equals(value)) {
				return MAIL.Theme.USE_GLOBAL;
			}

			return super.fromPrettyString(value);
		}

		@Override
		public String toPrettyString(Object value) {
			if (((MAIL.Theme) value) == MAIL.Theme.USE_GLOBAL) {
				return THEME_USE_GLOBAL;
			}

			return super.toPrettyString(value);
		}
	}

	/**
	 * A time setting.
	 */
	public static class TimeSetting extends SettingsDescription {
		public TimeSetting(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public Object fromString(String value)
				throws InvalidSettingValueException {
			if (!value.matches(TimePickerPreference.VALIDATION_EXPRESSION)) {
				throw new InvalidSettingValueException();
			}
			return value;
		}
	}

	/**
	 * A directory on the file system.
	 */
	public static class DirectorySetting extends SettingsDescription {
		public DirectorySetting(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public Object fromString(String value)
				throws InvalidSettingValueException {
			try {
				if (new File(value).isDirectory()) {
					return value;
				}
			} catch (Exception e) { /* do nothing */
			}

			throw new InvalidSettingValueException();
		}
	}
}
