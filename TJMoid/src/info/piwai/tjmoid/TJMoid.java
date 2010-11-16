package info.piwai.tjmoid;

import info.piwai.tjmoid.CalculateurSalaire.Builder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class TJMoid extends Activity {

	private EditText tjmInput;
	private TextView salaireTextView;

	private CalculateurSalaire calculateurSalaire;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tjmInput = (EditText) findViewById(R.id.tjmInput);
		salaireTextView = (TextView) findViewById(R.id.salaireTextView);

		tjmInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				onTjmChanged();
			}
		});

	}

	private void onTjmChanged() {
		String tjmString = tjmInput.getText().toString();

		long tjm;
		if ("".equals(tjmString)) {
			tjm = 0;
		} else {
			tjm = Long.parseLong(tjmString);
		}

		long salaireBrut = calculateurSalaire.calculerSalaireBrut(tjm);
		salaireTextView.setText(salaireBrut + " euros bruts annuels");
	}

	@Override
	protected void onResume() {
		super.onResume();
		calculateurSalaire = buildCalculateurSalaire();
		onTjmChanged();
	}

	private CalculateurSalaire buildCalculateurSalaire() {

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		Builder builder = new CalculateurSalaire.Builder();
		builder.salaireBrutDeBase(getDouble(preferences, "salaireBrutDeBase", 2584)) //
				.tauxChargesSocialesPatronales(getDouble(preferences, "tauxChargesSocialesPatronales", 1.58)) //
				.tauxPartageIngeEntreprise(getDouble(preferences, "tauxPartageIngeEntreprise", 0.6)) //
				.nbJoursTravaillesAnnuels(getDouble(preferences, "nbJoursTravaillesAnnuels", 219)) //
				.tauxMargeCommerciale(getDouble(preferences, "tauxMargeCommerciale", 0.1));
		return builder.build();
	}

	private double getDouble(SharedPreferences preferences, String key, double defaultValue) {
		String stringValue = preferences.getString(key, "");
		try {
			return Double.parseDouble(stringValue);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem settingsItem = menu.add("Paramètres");
		settingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				startPreferencesActivity();
				return true;
			}
		});
		return true;
	}

	private void startPreferencesActivity() {
		Intent preferencesActivityIntent = new Intent(TJMoid.this, Preferences.class);
		startActivity(preferencesActivityIntent);
	}

}