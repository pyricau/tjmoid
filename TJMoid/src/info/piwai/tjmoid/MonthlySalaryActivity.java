package info.piwai.tjmoid;

import info.piwai.tjmoid.domain.JoursOuvres;
import info.piwai.tjmoid.domain.SalaireDao;
import info.piwai.tjmoid.domain.SalaireMensuel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.ItemSelect;
import com.googlecode.androidannotations.annotations.Layout;
import com.googlecode.androidannotations.annotations.ViewById;

@Layout(R.layout.monthly_salary)
public class MonthlySalaryActivity extends TrackingActivity {
	private static final String TAG = MonthlySalaryActivity.class.getSimpleName();

	public static final String DEFAULT_TJM_EXTRA = "default_tjm_extra";
	private static final String SELECTED_YEAR_PREF = "selectedYear";
	private static final String SELECTED_MONTH_PREF = "selectedMonth";

	@ViewById
	Spinner monthSelectSpinner;

	@ViewById
	Spinner yearSelectSpinner;

	@ViewById
	EditText tjmInput;

	@ViewById
	EditText congesInput;

	@ViewById
	TextView totalBrutMensuelTextView;

	@ViewById
	TextView errors;

	@ViewById
	TextView fixeBrutMensuelTextView;

	@ViewById
	TextView primesBrutMensuellesTextView;

	@ViewById
	TextView primesNonLisseesTextView;

	@ViewById
	TextView totalNetMensuelTextView;

	@ViewById
	TextView caGenereTextView;

	@ViewById
	EditText cssInput;
	
	@ViewById
	EditText joursComInput;

	@ViewById
	EditText caManuelInput;

	int defaultTjm;

	SalaireDao salaireDao;

	private JoursOuvres selectedYear;
	private int selectedMonth;
	private JoursOuvres[] allowedYears;
	private SalaireMensuel salaire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		defaultTjm = getIntent().getIntExtra(DEFAULT_TJM_EXTRA, 0);

		salaireDao = new SalaireDao(this);

		initSpinners();

		bindSpinners();

		bindInputs();
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

	@ItemSelect
	void yearSelectSpinner(boolean somethingSelected, JoursOuvres selected) {
		if (somethingSelected) {
			if (selectedYear != selected) {
				updateSelectedMonth(selectedMonth, selected);
				getTracker().trackEvent("Spinner", "Change", "Year", selectedYear.getAnnee());
			}
		}
	}

	private void bindSpinners() {
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
		
		if (salaire.joursComChanged(joursComInput.getText().toString())) {
			joursComInput.setText(salaire.getJoursComAsString());
		}

		if (salaire.caManuelChanged(caManuelInput.getText().toString())) {
			caManuelInput.setText(salaire.getCaManuelAsString());
		}
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
		joursComInput.addTextChangedListener(new AbstractTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String joursCom = s.toString();
				if (salaire.joursComChanged(joursCom)) {
					salaire.setJoursComAsString(joursCom);
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

	@Click
	void nextMonthButton() {
		int newSelectedMonth = selectedMonth + 1;

		if (newSelectedMonth > Calendar.DECEMBER) {

			int currentYearSelectionIndex = Arrays.binarySearch(allowedYears, selectedYear);

			if (currentYearSelectionIndex + 1 < allowedYears.length) {
				newSelectedMonth = Calendar.JANUARY;
				updateSelectedMonth(newSelectedMonth, allowedYears[currentYearSelectionIndex + 1]);
			} else {
				Toast.makeText(this, "Data not available yet", Toast.LENGTH_SHORT).show();
			}
		} else {
			updateSelectedMonth(newSelectedMonth, selectedYear);
		}
	}

	@Click
	void previousMonthButton() {
		int newSelectedMonth = selectedMonth - 1;

		if (newSelectedMonth < Calendar.JANUARY) {

			int currentYearSelectionIndex = Arrays.binarySearch(allowedYears, selectedYear);

			if (currentYearSelectionIndex != 0) {
				newSelectedMonth = Calendar.DECEMBER;
				updateSelectedMonth(newSelectedMonth, allowedYears[currentYearSelectionIndex - 1]);
			} else {
				Toast.makeText(this, "Data not available yet", Toast.LENGTH_SHORT).show();
			}
		} else {
			updateSelectedMonth(newSelectedMonth, selectedYear);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadSelectedMonth();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		salaireDao.close();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		loadSelectedMonth();
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("Vérifier sur Maestro").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MonthlySalaryActivity.this, CheckMaestroActivity.class);
				startActivity(intent);
				return true;
			}
		});
		return true;
	}

}
