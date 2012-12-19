package ru.spbau.remote.settings;

import java.net.URI;

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
	private static final String SCHEMA = "ws";
	private static final String PATH = "/websocket";
	
	private static final String SERVER_HOST = "pref_host";
	private static final String SERVER_PORT = "pref_port";
	private static final String SOURCE_ID = "pref_source";
	private static final String TARGET_ID = "pref_target";
	
	private SharedPreferences mySettings;
	
	public ApplicationSettingsSource(Context context) {
		mySettings = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String getHost() {
		return mySettings.getString(SERVER_HOST, "192.168.0.50");
	}
	
	public int getPort() {
		return Integer.parseInt(mySettings.getString(SERVER_PORT, "8080"));
	}
	
	public String getSource() {
		return mySettings.getString(SOURCE_ID, null);
	}
	
	public String getTarget() {
		return mySettings.getString(TARGET_ID, null);
	}
	
	public URI getURI() {
		URI uri = null;

        String query = "";
        String from = getSource();
        String to = getTarget();
        if (from.length() > 0) {
            query = "from=" + from;
        }
        if (to.length() > 0) {
            if (query.length() > 0)
                query += "&";
            query += "to=" + to;
        }

		try {
			uri = new URI(SCHEMA, null, getHost(), getPort(), PATH, query, null);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getLocalizedMessage());
		}
		return uri;
	}
}