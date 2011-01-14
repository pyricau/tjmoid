/*
 * Copyright 2010 Pierre-Yves Ricau (py.ricau+tjmoid@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.piwai.tjmoid;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class Preferences extends TrackingPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		
		OnPreferenceChangeListener listener = new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				int intValue;
				try {
					intValue = Integer.parseInt(newValue.toString());
				} catch (NumberFormatException e) {
					intValue = 0;
				}
				
				getTracker().trackEvent("Preference", "Change", preference.getKey(), intValue);
				return true;
			}
		};
		
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		
		for (int i = 0; i<preferenceScreen.getPreferenceCount(); i++) {
			preferenceScreen.getPreference(i).setOnPreferenceChangeListener(listener);
		}

		Preference resetPreference = preferenceScreen.findPreference("reset");

		resetPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				resetToDefaultValues();
				return true;
			}
		});
	}

	private void resetToDefaultValues() {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		Editor edit = preferences.edit();

		edit.putString("salaireBrutDeBase", "2584") //
				.putString("tauxChargesSocialesPatronales", "1.58") //
				.putString("tauxPartageSalarieEntreprise", "0.6") //
				.putString("nbJoursTravaillesAnnuels", "219") //
				.putString("tauxMargeCommerciale", "0.1") //
				.commit();
		finish();
	}
	
	

}
