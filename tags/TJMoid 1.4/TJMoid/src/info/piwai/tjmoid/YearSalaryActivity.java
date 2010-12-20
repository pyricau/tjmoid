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

import info.piwai.tjmoid.CalculateurSalaire.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.admob.android.ads.AdView;
import com.admob.android.ads.SimpleAdListener;

public class YearSalaryActivity extends TrackingActivity {

	private EditText tjmInput;
	private TextView salaireBrutAnnuelTextView;
	private TextView salaireBrutMensuelTextView;
	private TextView salaireNetMensuelTextView;
	private Button monthlySalaryButton;

	private AdView adView;

	private CalculateurSalaire calculateurSalaire;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_salary);

		tjmInput = (EditText) findViewById(R.id.tjmInput);
		salaireBrutAnnuelTextView = (TextView) findViewById(R.id.salaireBrutAnnuelTextView);
		salaireBrutMensuelTextView = (TextView) findViewById(R.id.salaireBrutMensuelTextView);
		salaireNetMensuelTextView = (TextView) findViewById(R.id.salaireNetMensuelTextView);
		adView = (AdView) findViewById(R.id.ad);

		monthlySalaryButton = (Button) findViewById(R.id.monthlySalaryButton);

		calculateurSalaire = buildCalculateurSalaire();

		adView.setAdListener(new SimpleAdListener() {
			@Override
			public void onFailedToReceiveAd(AdView arg0) {
				Log.d("TEST", "onFailedToReceiveAd");
			}

			@Override
			public void onReceiveAd(AdView arg0) {
				Log.d("TEST", "onReceiveAd");
			}

			@Override
			public void onReceiveRefreshedAd(AdView arg0) {
				Log.d("TEST", "onReceiveRefreshedAd");
			}
			
			@Override
			public void onFailedToReceiveRefreshedAd(AdView arg0) {
				Log.d("TEST", "onFailedToReceiveRefreshedAd");
			}

		});

		tjmInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				onTjmChanged();
			}
		});

		monthlySalaryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(YearSalaryActivity.this, MonthlySalaryActivity.class);
				int defaultTjm = tjmFromInput();
				intent.putExtra(MonthlySalaryActivity.DEFAULT_TJM_EXTRA, defaultTjm);
				startActivity(intent);
			}
		});

	}

	private void onTjmChanged() {
		int tjm = tjmFromInput();

		long salaireBrutAnnuel = calculateurSalaire.calculerSalaireBrut(tjm);
		salaireBrutAnnuelTextView.setText(salaireBrutAnnuel + " euros");

		long salaireBrutMensuel = salaireBrutAnnuel / 12;
		salaireBrutMensuelTextView.setText(salaireBrutMensuel + " euros");

		long salaireNetMensuel = (long) (((double) salaireBrutMensuel) * 0.77);
		salaireNetMensuelTextView.setText(salaireNetMensuel + " euros");
	}

	private int tjmFromInput() {
		int tjm;
		String tjmString = tjmInput.getText().toString();

		if ("".equals(tjmString)) {
			tjm = 0;
		} else {
			tjm = Integer.parseInt(tjmString);
		}
		return tjm;
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
		Intent preferencesActivityIntent = new Intent(YearSalaryActivity.this, Preferences.class);
		startActivity(preferencesActivityIntent);
	}

}