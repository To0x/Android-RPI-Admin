package de.htw_berlin.Fernsteuerung;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
		
		SharedPreferences settings = getPreferenceScreen().getSharedPreferences();
		
		// Setzt für alle Text-Einstellungen den Summary-Text auf den Value
		Map<String,?> keys = settings.getAll();
		for (Map.Entry<String,?> entry : keys.entrySet()) {
			
			Preference pref = findPreference(entry.getKey());
			if (pref instanceof EditTextPreference)
				pref.setSummary(((EditTextPreference) pref).getText());
			
		}
		
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		// Setzt für alle Text-Einstellungen den Summary-Text auf den Value
		Preference pref = findPreference(key);
		if (pref instanceof EditTextPreference) {
			pref.setSummary(((EditTextPreference) pref).getText());
		}
	}
	
}
