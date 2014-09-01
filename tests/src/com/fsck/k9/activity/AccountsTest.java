package com.bernard.beaconportal.activities.activity;

import android.test.ActivityInstrumentationTestCase2;
import com.bernard.beaconportal.activities.activity.Accounts;
/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.bernard.beaconportal.activities.activity.AccountsTest \
 * com.bernard.beaconportal.activities.tests/android.test.InstrumentationTestRunner
 */
public class AccountsTest extends ActivityInstrumentationTestCase2<Accounts> {

    public AccountsTest() {
        super("com.bernard.beaconportal.activities", Accounts.class);
    }

}
