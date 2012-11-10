package ru.spbau.remote.settings;

import java.net.URI;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Helper class to access application settings
 * 
 * @version 1.0
 * @author kmu
 */
public class ApplicationSettingsSource {
	private static final String SCHEMA = "http";
	private static final String PATH = "websocket";
	
	private static final String SERVER_HOST = "pref_host";
	private static final String SERVER_PORT = "pref_port";
	private static final String SOURCE_ID = "pref_source";
	private static final String TARGET_ID = "pref_target";
	
	private SharedPreferences mySettings;
	
	public ApplicationSettingsSource(Context context) {
		mySettings = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String getHost() {
		return mySettings.getString(SERVER_HOST, "192.168.1.1");
	}
	
	public int getPort() {
		return mySettings.getInt(SERVER_PORT, 8080);
	}
	
	public String getSource() {
		return mySettings.getString(SOURCE_ID, "");
	}
	
	public String getTarget() {
		return mySettings.getString(TARGET_ID, "");
	}
	
	public URL getURL() {
		URL url = null;
		String query = "from=" + getSource() + "&" + "to=" + getTarget();
		try {
			url = new URI(SCHEMA, null, getHost(), getPort(), PATH, query, null).toURL();
		} catch (Exception e) {
			Log.w(this.getClass().getName(), e.getLocalizedMessage());
		}
		return url;
	}
}