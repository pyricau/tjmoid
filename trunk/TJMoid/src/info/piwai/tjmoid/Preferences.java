package info.piwai.tjmoid;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Preference resetPreference = getPreferenceScreen().findPreference("reset");

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
				.putString("tauxPartageIngeEntreprise", "0.6") //
				.putString("nbJoursTravaillesAnnuels", "219") //
				.putString("tauxMargeCommerciale", "0.1") //
				.commit();
		finish();
	}

}
