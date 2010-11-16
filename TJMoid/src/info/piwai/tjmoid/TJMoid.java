package info.piwai.tjmoid;

import info.piwai.tjmoid.CalculateurSalaire.Builder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TJMoid extends Activity {
	
	private class LouarneFactRunnable implements Runnable {
		public void run() {
			showLouarneFact();
		}
	}

	private EditText tjmInput;
	private TextView salaireTextView;

	private final LouarneFactGenerator louarneFactGenerator = new LouarneFactGenerator();
	
	private final Handler handler = new Handler();
	
	private boolean paused = true;
	
	private final LouarneFactRunnable louarneFactRunnable = new LouarneFactRunnable();
	


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

				String tjmString = s.toString();
				long tjm;
				if ("".equals(tjmString)) {
					tjm = 0;
				} else {
					tjm = Long.parseLong(tjmString);
				}
				onTjmChanged(tjm);
			}
		});

	}

	private void onTjmChanged(long tjm) {

		CalculateurSalaire calculateurSalaire = buildCalculateurSalaire();

		long salaireBrut = calculateurSalaire.calculerSalaireBrut(tjm);

		salaireTextView.setText(salaireBrut + " euros bruts annuels");
	}

	private CalculateurSalaire buildCalculateurSalaire() {
		Builder builder = new CalculateurSalaire.Builder();
		builder.salaireBrutDeBase(2584) //
				.tauxChargesSocialesPatronales(1.58) //
				.tauxPartageIngeEntreprise(0.6) //
				.nbJoursTravaillesAnnuels(219) //
				.tauxMargeCommerciale(0.1);
		CalculateurSalaire calculateurSalaire = builder.build();
		return calculateurSalaire;
	}

	@Override
	protected void onResume() {
		super.onResume();
		paused = false;
		showLouarneFact();
	}

	@Override
	protected void onPause() {
		super.onPause();
		paused = true;
	}

	private void showLouarneFact() {
		if (!paused) {
			String randomLouarneFact = louarneFactGenerator.getRandomLouarneFact();
			Toast.makeText(this, randomLouarneFact, Toast.LENGTH_LONG).show();
			planNextLouarneFact();
		}
	}

	private void planNextLouarneFact() {
		handler.postDelayed(louarneFactRunnable, 5000);
	}

}