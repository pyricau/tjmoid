package info.piwai.tjmoid;

import info.piwai.tjmoid.domain.JoursOuvres;
import info.piwai.tjmoid.domain.SalaireDao;
import info.piwai.tjmoid.domain.SalaireMensuel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MonthlySalaryActivity extends TrackingActivity {
	private static final String TAG = MonthlySalaryActivity.class.getSimpleName();

	public static final String DEFAULT_TJM_EXTRA = "default_tjm_extra";
	private static final String SELECTED_YEAR_PREF = "selectedYear";
	private static final String SELECTED_MONTH_PREF = "selectedMonth";

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
	private Button nextMonthButton;
	private Button previousMonthButton;

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
		
		bindButtons();
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
	}

	private void bindSpinners() {
		yearSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (selectedYear != allowedYears[position]) {
					updateSelectedMonth(selectedMonth, allowedYears[position]);
					getTracker().trackEvent("Spinner", "Change", "Year", selectedYear.getAnnee());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		monthSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (selectedMonth != position) {
					updateSelectedMonth(position, selectedYear);
					getTracker().trackEvent("Spinner", "Change", "Month", selectedMonth);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	

	private void bindButtons() {
		previousMonthButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPreviousMonth();
			}
		});
		nextMonthButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectNextMonth();
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
		previousMonthButton = (Button) findViewById(R.id.previousMonthButton);
		nextMonthButton = (Button) findViewById(R.id.nextMonthButton);
	}

	private void updateSelectedMonth(int newSelectedMonth, JoursOuvres newSelectedYear) {
		selectedMonth = newSelectedMonth;
		selectedYear = newSelectedYear;
		updateSpinnersFromSelectedMonth();
		updateViewsFromSelectedMonth();
	}

	private void updateSpinnersFromSelectedMonth() {
		monthSelectSpinner.setSelection(selectedMonth);
		int yearSelectionIndex = Arrays.binarySearch(allowedYears, selectedYear);
		yearSelectSpinner.setSelection(yearSelectionIndex);
	}

	private void updateViewsFromSelectedMonth() {
		Log.d(TAG, "Reloading data and views");

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

	@Override
	protected void onResume() {
		super.onResume();
		loadSelectedMonth();
	}

	@Override
	protected void onPause() {
		super.onPause();

		saveSelectedMonth();
	}

	private void saveSelectedMonth() {
		getPreferences(MODE_PRIVATE) //
				.edit() //
				.putInt(SELECTED_YEAR_PREF, selectedYear.getAnnee()) //
				.putInt(SELECTED_MONTH_PREF, selectedMonth) //
				.commit();
	}

	private void loadSelectedMonth() {

		Calendar calendar = Calendar.getInstance();

		int defaultSelectedYear = calendar.get(Calendar.YEAR);
		int defaultSelectedMonth = calendar.get(Calendar.MONTH);

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		JoursOuvres newSelectedYear = JoursOuvres.fromYear(preferences.getInt(SELECTED_YEAR_PREF, defaultSelectedYear));
		int newSelectedMonth = preferences.getInt(SELECTED_MONTH_PREF, defaultSelectedMonth);

		updateSelectedMonth(newSelectedMonth, newSelectedYear);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		salaireDao.close();
	}

	private void selectNextMonth() {
		int newSelectedMonth = selectedMonth + 1;

		if (newSelectedMonth == 12) {

			int currentYearSelectionIndex = Arrays.binarySearch(allowedYears, selectedYear);

			if (currentYearSelectionIndex + 1 < allowedYears.length) {
				newSelectedMonth = 0;
				updateSelectedMonth(newSelectedMonth, allowedYears[currentYearSelectionIndex + 1]);
			} else {
				Toast.makeText(this, "Data not available yet", Toast.LENGTH_SHORT).show();
			}
		} else {
			updateSelectedMonth(newSelectedMonth, selectedYear);
		}
	}

	private void selectPreviousMonth() {
		int newSelectedMonth = selectedMonth - 1;

		if (newSelectedMonth == -1) {

			int currentYearSelectionIndex = Arrays.binarySearch(allowedYears, selectedYear);

			if (currentYearSelectionIndex != 0) {
				newSelectedMonth = 0;
				updateSelectedMonth(newSelectedMonth, allowedYears[currentYearSelectionIndex - 1]);
			} else {
				Toast.makeText(this, "Data not available yet", Toast.LENGTH_SHORT).show();
			}
		} else {
			updateSelectedMonth(newSelectedMonth, selectedYear);
		}
	}

}
