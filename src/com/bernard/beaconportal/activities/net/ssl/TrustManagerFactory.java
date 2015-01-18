package com.bernard.beaconportal.activities.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

import com.bernard.beaconportal.activities.helper.DomainNameChecker;
import com.bernard.beaconportal.activities.mail.CertificateChainException;
import com.bernard.beaconportal.activities.security.LocalKeyStore;

public final class TrustManagerFactory {
	private static final String LOG_TAG = "TrustManagerFactory";

	private static X509TrustManager defaultTrustManager;

	private static LocalKeyStore keyStore;

	private static class SecureX509TrustManager implements X509TrustManager {
		private static final Map<String, SecureX509TrustManager> mTrustManager = new HashMap<String, SecureX509TrustManager>();

		private final String mHost;
		private final int mPort;

		private SecureX509TrustManager(String host, int port) {
			mHost = host;
			mPort = port;
		}

		public synchronized static X509TrustManager getInstance(String host,
				int port) {
			String key = host + ":" + port;
			SecureX509TrustManager trustManager;
			if (mTrustManager.containsKey(key)) {
				trustManager = mTrustManager.get(key);
			} else {
				trustManager = new SecureX509TrustManager(host, port);
				mTrustManager.put(key, trustManager);
			}

			return trustManager;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			defaultTrustManager.checkClientTrusted(chain, authType);
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			String message = null;
			boolean foundInGlobalKeyStore = false;
			try {
				defaultTrustManager.checkServerTrusted(chain, authType);
				foundInGlobalKeyStore = true;
			} catch (CertificateException e) {
				message = e.getMessage();
			}

			X509Certificate certificate = chain[0];

			// Check the local key store if we couldn't verify the certificate
			// using the global
			// key store or if the host name doesn't match the certificate name
			if (foundInGlobalKeyStore
					&& DomainNameChecker.match(certificate, mHost)
					|| keyStore.isValidCertificate(certificate, mHost, mPort)) {
				return;
			}

			if (message == null) {
				message = (foundInGlobalKeyStore) ? "Certificate domain name does not match "
						+ mHost
						: "Couldn't find certificate in local key store";
			}

			throw new CertificateChainException(message, chain);
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return defaultTrustManager.getAcceptedIssuers();
		}

	}

	static {
		try {
			keyStore = LocalKeyStore.getInstance();

			javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory
					.getInstance("X509");
			tmf.init((KeyStore) null);

			TrustManager[] tms = tmf.getTrustManagers();
			if (tms != null) {
				for (TrustManager tm : tms) {
					if (tm instanceof X509TrustManager) {
						defaultTrustManager = (X509TrustManager) tm;
						break;
					}
				}
			}
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Unable to get X509 Trust Manager ", e);
		} catch (KeyStoreException e) {
			Log.e(LOG_TAG,
					"Key Store exception while initializing TrustManagerFactory ",
					e);
		}
	}

	private TrustManagerFactory() {
	}

	public static X509TrustManager get(String host, int port) {
		return SecureX509TrustManager.getInstance(host, port);
	}
}
