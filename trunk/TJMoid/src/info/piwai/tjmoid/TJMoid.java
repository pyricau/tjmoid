package info.piwai.tjmoid;

import info.piwai.tjmoid.CalculateurSalaire.Builder;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TJMoid extends Activity {

	private EditText tjmInput;
	private TextView salaireTextView;

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

}