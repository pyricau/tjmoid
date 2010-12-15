package info.piwai.tjmoid;

import info.piwai.tjmoid.domain.JoursOuvres;
import info.piwai.tjmoid.domain.SalaireDao;
import info.piwai.tjmoid.domain.SalaireMensuel;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MonthlySalaryActivity extends Activity{
	
	public static final String DEFAULT_TJM_EXTRA = "default_tjm_extra";
	private int defaultTjm;
	
	private SalaireDao salaireDao;
	
	private Spinner monthSelectSpinner;
	private Spinner yearSelectSpinner;
	private EditText tjmInput;
	private EditText congesInput;
	
	private TextView totalBrutMensuelTextView;
	
	private JoursOuvres selectedYear;
	private int selectedMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.monthly_salary);
		
		defaultTjm = getIntent().getIntExtra(DEFAULT_TJM_EXTRA, 0);
		
		salaireDao = new SalaireDao(this);
		
		yearSelectSpinner = (Spinner) findViewById(R.id.yearSelectSpinner);
		monthSelectSpinner = (Spinner) findViewById(R.id.monthSelectSpinner);
		tjmInput = (EditText) findViewById(R.id.tjmInput);
		congesInput = (EditText) findViewById(R.id.congesInput);
		totalBrutMensuelTextView = (TextView)findViewById(R.id.totalBrutMensuelTextView);
		
		
		ArrayAdapter<JoursOuvres> yearAdapter = new ArrayAdapter<JoursOuvres>(this, android.R.layout.simple_spinner_dropdown_item, JoursOuvres.values());
		yearSelectSpinner.setAdapter(yearAdapter);
		
		ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_dropdown_item);
		monthSelectSpinner.setAdapter(monthAdapter);
		
		selectedYear = JoursOuvres.values()[0];
		selectedMonth = 0;
		
		yearSelectSpinner.setSelection(0);
		monthSelectSpinner.setSelection(0);
		
		yearSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedYear = JoursOuvres.values()[position];
				updateViewsFromSelectedMonth();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		monthSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedMonth = position;
				updateViewsFromSelectedMonth();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		updateViewsFromSelectedMonth();
	}
	
	
	private void updateViewsFromSelectedMonth() {
		SalaireMensuel salaire = salaireDao.find(selectedYear.getAnnee(), selectedMonth, defaultTjm);
		
		double totalBrutMensuel = salaire.getTotalBrutMensuel();
		
		totalBrutMensuelTextView.setText(totalBrutMensuel+"");
	}
	
}
