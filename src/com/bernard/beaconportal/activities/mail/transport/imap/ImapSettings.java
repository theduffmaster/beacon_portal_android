package com.bernard.beaconportal.activities.mail.transport.imap;

import com.bernard.beaconportal.activities.mail.AuthType;
import com.bernard.beaconportal.activities.mail.ConnectionSecurity;
import com.bernard.beaconportal.activities.mail.store.ImapStore;
import com.bernard.beaconportal.activities.mail.store.ImapStore.ImapConnection;

/**
 * Settings source for IMAP. Implemented in order to remove coupling between
 * {@link ImapStore} and {@link ImapConnection}.
 */
public interface ImapSettings {
	String getHost();

	int getPort();

	ConnectionSecurity getConnectionSecurity();

	AuthType getAuthType();

	String getUsername();

	String getPassword();

	boolean useCompression(int type);

	String getPathPrefix();

	void setPathPrefix(String prefix);

	String getPathDelimeter();

	void setPathDelimeter(String delimeter);

	String getCombinedPrefix();

	void setCombinedPrefix(String prefix);
}
