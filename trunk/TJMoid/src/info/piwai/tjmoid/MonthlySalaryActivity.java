package info.piwai.tjmoid;

import info.piwai.tjmoid.domain.JoursOuvres;
import info.piwai.tjmoid.domain.SalaireDao;
import info.piwai.tjmoid.domain.SalaireMensuel;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MonthlySalaryActivity extends TrackingActivity {

	public static final String DEFAULT_TJM_EXTRA = "default_tjm_extra";
	private int defaultTjm;

	private SalaireDao salaireDao;

	private Spinner monthSelectSpinner;
	private Spinner yearSelectSpinner;
	private EditText tjmInput;
	private EditText congesInput;

	private TextView totalBrutMensuelTextView;
	private TextView errors;

	private JoursOuvres selectedYear;
	private int selectedMonth;
	private JoursOuvres[] allowedYears;
	private SalaireMensuel salaire;
	private TextView fixeBrutMensuelTextView;
	private TextView primesBrutMensuellesTextView;
	private TextView primesNonLisseesTextView;
	private TextView totalNetMensuelTextView;
	private TextView caGenereTextView;
	private EditText cssInput;
	private EditText caManuelInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.monthly_salary);

		defaultTjm = getIntent().getIntExtra(DEFAULT_TJM_EXTRA, 0);

		salaireDao = new SalaireDao(this);

		findViews();

		initSpinners();

		bindSpinners();

		bindInputs();

		updateViewsFromSelectedMonth();
	}

	private void bindInputs() {
		tjmInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

				String tjm = s.toString();
				if (salaire.tjmChanged(tjm)) {
					salaire.setTjmAsString(tjm);
					validateAndUpdate();
				}
			}
		});
		congesInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

				String conges = s.toString();
				if (salaire.congesChanged(conges)) {
					salaire.setCongesAsString(conges);
					validateAndUpdate();
				}
			}
		});
		cssInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String css = s.toString();
				if (salaire.cssChanged(css)) {
					salaire.setCssAsString(css);
					validateAndUpdate();
				}
			}
		});
		caManuelInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String caManuel = s.toString();
				if (salaire.caManuelChanged(caManuel)) {
					salaire.setCaManuelAsString(caManuel);
					validateAndUpdate();
				}
			}
		});
	}

	private void initSpinners() {
		allowedYears = JoursOuvres.allowedValues();
		ArrayAdapter<JoursOuvres> yearAdapter = new ArrayAdapter<JoursOuvres>(this, android.R.layout.simple_spinner_item, allowedYears);
		yearSelectSpinner.setAdapter(yearAdapter);
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
		monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSelectSpinner.setAdapter(monthAdapter);

		selectedYear = allowedYears[0];
		selectedMonth = 0;
	}

	private void bindSpinners() {
		yearSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedYear = allowedYears[position];
				updateViewsFromSelectedMonth();
				getTracker().trackEvent("Spinner", "Change", "Year", selectedYear.getAnnee());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		monthSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedMonth = position;
				updateViewsFromSelectedMonth();
				getTracker().trackEvent("Spinner", "Change", "Month", selectedMonth);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void findViews() {
		yearSelectSpinner = (Spinner) findViewById(R.id.yearSelectSpinner);
		monthSelectSpinner = (Spinner) findViewById(R.id.monthSelectSpinner);
		tjmInput = (EditText) findViewById(R.id.tjmInput);
		congesInput = (EditText) findViewById(R.id.congesInput);
		cssInput = (EditText) findViewById(R.id.cssInput);
		caManuelInput = (EditText) findViewById(R.id.caManuelInput);

		totalBrutMensuelTextView = (TextView) findViewById(R.id.totalBrutMensuelTextView);
		totalNetMensuelTextView = (TextView) findViewById(R.id.totalNetMensuelTextView);
		fixeBrutMensuelTextView = (TextView) findViewById(R.id.fixeBrutMensuelTextView);
		primesBrutMensuellesTextView = (TextView) findViewById(R.id.primesBrutMensuellesTextView);
		primesNonLisseesTextView = (TextView) findViewById(R.id.primesNonLisseesTextView);
		caGenereTextView = (TextView) findViewById(R.id.caGenereTextView);
		errors = (TextView) findViewById(R.id.errors);
	}

	private void updateViewsFromSelectedMonth() {
		salaire = salaireDao.find(selectedYear.getAnnee(), selectedMonth, defaultTjm);

		totalBrutMensuelTextView.setText(salaire.getTotalBrutMensuel());
		totalNetMensuelTextView.setText(salaire.getTotalNetMensuel());
		fixeBrutMensuelTextView.setText(salaire.getFixeBrutMensuel());
		primesBrutMensuellesTextView.setText(salaire.getPrimesBrutMensuelles());
		primesNonLisseesTextView.setText(salaire.getPrimesNonLissées());
		caGenereTextView.setText(salaire.getChiffreAffaireGenere());

		if (salaire.tjmChanged(tjmInput.getText().toString())) {
			tjmInput.setText(salaire.getTjmAsString());
		}

		if (salaire.congesChanged(congesInput.getText().toString())) {
			congesInput.setText(salaire.getCongesAsString());
		}

		if (salaire.cssChanged(cssInput.getText().toString())) {
			cssInput.setText(salaire.getCssAsString());
		}

		if (salaire.caManuelChanged(caManuelInput.getText().toString())) {
			caManuelInput.setText(salaire.getCaManuelAsString());
		}
	}

	private void validateAndUpdate() {
		List<String> validationErrors = new ArrayList<String>();
		salaire.validate(validationErrors);

		if (validationErrors.size() == 0) {
			errors.setText("");
			salaireDao.update(salaire);
			updateViewsFromSelectedMonth();
		} else {
			StringBuilder sb = new StringBuilder();
			for (String validationError : validationErrors) {
				sb.append(validationError).append("\n");
			}
			errors.setText(sb);
		}
	}

}
