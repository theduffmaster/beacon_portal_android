package com.bernard.beaconportal.activities.remotecontrol;

/**
 * 
 * @author Daniel I. Applebaum The interface to implement in order to accept the
 *         arrays containing the UUIDs and descriptions of the accounts
 *         configured in Mail Mail. Should be passed to fetchAccounts(Context,
 *         MAILAccountReceptor)
 */
public interface MAILAccountReceptor {
	public void accounts(String[] uuids, String[] descriptions);
}