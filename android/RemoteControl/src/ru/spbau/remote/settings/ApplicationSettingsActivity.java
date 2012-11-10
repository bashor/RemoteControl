package ru.spbau.remote.settings;

import ru.spbau.remote.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Application settings activity
 * 
 * @version 1.0
 * @author kmu
 */
public class ApplicationSettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
