package info.piwai.tjmoid.domain;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SalaireDao {

	private static final String DATABASE_NAME = "salaire.db";
	private static final String SALAIRE_TABLE = "salaire";

	private static final String MONTH_NUMBER_KEY = "monthNumber";
	private static final String YEAR_KEY = "year";
	private static final String NB_JOURS_OUVRES_KEY = "nbJoursOuvres";
	private static final String NB_CONGES_KEY = "nbConges";
	private static final String NB_CONGES_SANS_SOLDE_KEY = "nbCongesSansSolde";
	private static final String NB_JOURS_COMMUNAUTAIRES_KEY = "nbJoursCommunautaires";
	private static final String CA_MANUEL_KEY = "chiffreAffaireManuel";
	private static final String SALAIRE_BRUT_DE_BASE_KEY = "salaireBrutDeBase";
	private static final String TAUX_CHARGES_SOCIALES_PATRONALES = "tauxChargesSocialesPatronales";
	private static final String TAUX_PARTAGE_SALARIE_ENTREPRISE = "tauxPartageSalarieEntreprise";
	private static final String TJM = "tjm";
	private static final String TAUX_MARGE_COMMERCIALE = "tauxMargeCommerciale";

	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = //
	"create table " + SALAIRE_TABLE //
			+ " (" //
			+ MONTH_NUMBER_KEY + " integer not null" //
			+ "," //
			+ YEAR_KEY + " integer not null" //
			+ "," //
			+ NB_JOURS_OUVRES_KEY + " float not null" //
			+ "," //
			+ NB_CONGES_KEY + " float not null" //
			+ "," //
			+ NB_CONGES_SANS_SOLDE_KEY + " float not null" //
			+ "," //
			+ NB_JOURS_COMMUNAUTAIRES_KEY + " float not null" //
			+ "," //
			+ CA_MANUEL_KEY + " float not null" //
			+ "," //
			+ SALAIRE_BRUT_DE_BASE_KEY + " float not null" //
			+ "," //
			+ TAUX_CHARGES_SOCIALES_PATRONALES + " float not null" //
			+ "," //
			+ TAUX_PARTAGE_SALARIE_ENTREPRISE + " float not null" //
			+ "," //
			+ TJM + " integer not null" //
			+ "," //
			+ TAUX_MARGE_COMMERCIALE + " float not null" //
			+ ");";

	private static final String SELECT_ONE = //
	YEAR_KEY + "=?" //
			+ " AND " //
			+ MONTH_NUMBER_KEY + "=?" //
	;
	
	private static final String[] ALL_FIELDS = null;
	private static final String NO_GROUP = null;
	private static final String NO_HAVING = null;
	private static final String NOT_ORDERED = null;
	private static final String NULL_COLUMN_HACK = "NULL_COLUMN_HACK";
	

	public static class MyHelper extends SQLiteOpenHelper {
		
		private final Context context;

		public MyHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SalaireDao.DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
	}

	private static ContentValues salaireToContentValues(SalaireMensuel salaire) {
		ContentValues values = new ContentValues();
		values.put(MONTH_NUMBER_KEY, salaire.monthNumber);
		values.put(YEAR_KEY, salaire.year);
		values.put(NB_JOURS_OUVRES_KEY, salaire.nbJoursOuvres);
		values.put(NB_CONGES_KEY, salaire.nbConges);
		values.put(NB_CONGES_SANS_SOLDE_KEY, salaire.nbCongesSansSolde);
		values.put(NB_JOURS_COMMUNAUTAIRES_KEY, salaire.nbJoursCommunautaires);
		values.put(CA_MANUEL_KEY, salaire.chiffreAffaireManuel);
		values.put(SALAIRE_BRUT_DE_BASE_KEY, salaire.salaireBrutDeBase);
		values.put(TAUX_CHARGES_SOCIALES_PATRONALES, salaire.tauxChargesSocialesPatronales);
		values.put(TAUX_PARTAGE_SALARIE_ENTREPRISE, salaire.tauxPartageSalariéEntreprise);
		values.put(TJM, salaire.tjm);
		values.put(TAUX_MARGE_COMMERCIALE, salaire.tauxMargeCommerciale);

		return values;
	}

	private static SalaireMensuel salaireFromCursor(Cursor cursor) {
		SalaireMensuel salaire = new SalaireMensuel();
		salaire.monthNumber = cursor.getInt(cursor.getColumnIndex(MONTH_NUMBER_KEY));
		salaire.year = cursor.getInt(cursor.getColumnIndex(YEAR_KEY));
		salaire.nbJoursOuvres = cursor.getDouble(cursor.getColumnIndex(NB_JOURS_OUVRES_KEY));
		salaire.nbConges = cursor.getDouble(cursor.getColumnIndex(NB_CONGES_KEY));
		salaire.nbCongesSansSolde = cursor.getDouble(cursor.getColumnIndex(NB_CONGES_SANS_SOLDE_KEY));
		salaire.nbJoursCommunautaires = cursor.getDouble(cursor.getColumnIndex(NB_JOURS_COMMUNAUTAIRES_KEY));
		salaire.chiffreAffaireManuel = cursor.getDouble(cursor.getColumnIndex(CA_MANUEL_KEY));
		salaire.salaireBrutDeBase = cursor.getDouble(cursor.getColumnIndex(SALAIRE_BRUT_DE_BASE_KEY));
		salaire.tauxChargesSocialesPatronales = cursor.getDouble(cursor.getColumnIndex(TAUX_CHARGES_SOCIALES_PATRONALES));
		salaire.tauxPartageSalariéEntreprise = cursor.getDouble(cursor.getColumnIndex(TAUX_PARTAGE_SALARIE_ENTREPRISE));
		salaire.tjm = cursor.getInt(cursor.getColumnIndex(TJM));
		salaire.tauxMargeCommerciale = cursor.getDouble(cursor.getColumnIndex(TAUX_MARGE_COMMERCIALE));
		return salaire;
	}

	private final MyHelper helper;

	public SalaireDao(Context context) {
		helper = new MyHelper(context);
	}

	private SalaireMensuel newEmptySalaire(int year, int monthNumber, int defaultTjm) {
		SalaireMensuel salaire = new SalaireMensuel();
		salaire.year = year;
		salaire.monthNumber = monthNumber;
		salaire.nbJoursOuvres = JoursOuvres.joursOuvres(year, monthNumber);
		salaire.salaireBrutDeBase = 2584;
		salaire.tauxChargesSocialesPatronales = 1.58;
		salaire.tauxPartageSalariéEntreprise = 0.6;
		salaire.tauxMargeCommerciale = 0.1;
		salaire.tjm = defaultTjm;
		return salaire;
	}

	public SalaireMensuel find(int year, int monthNumber, int defaultTjm) {
		SalaireMensuel salaire = findOne(year, monthNumber, defaultTjm);
		
		
		List<SalaireMensuel> salairesSixDerniersMois = new ArrayList<SalaireMensuel>();
		SalaireMensuel previousSalaire = salaire;
		for (int i = 0; i < 6; i++) {
			previousSalaire = findPrevious(previousSalaire);
			salairesSixDerniersMois.add(previousSalaire);
		}
		
		salaire.updatePrimeLissées(salairesSixDerniersMois);
		
		return salaire;
	}
	
	private SalaireMensuel findPrevious(SalaireMensuel salaire) {
		int monthNumber = salaire.monthNumber;
		int year = salaire.year;
		
		monthNumber--;
		
		if (monthNumber<Calendar.JANUARY) {
			monthNumber = Calendar.DECEMBER;
			year--;
		}
		
		return findOne(year, monthNumber, 0);
	}
	
	
	private SalaireMensuel findOne(int year, int monthNumber, int defaultTjm) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] arguments = {""+year, ""+monthNumber};
		Cursor cursor = db.query(SALAIRE_TABLE, ALL_FIELDS, SELECT_ONE, arguments, NO_GROUP, NO_HAVING, NOT_ORDERED);
		
		SalaireMensuel salaire;
		if (cursor.moveToNext()) {
			salaire = salaireFromCursor(cursor);
		} else {
			salaire = newEmptySalaire(year, monthNumber, defaultTjm);
			ContentValues values = salaireToContentValues(salaire);
			db.insert(SALAIRE_TABLE, NULL_COLUMN_HACK, values);
		}
		cursor.close();
		
		return salaire;
	}
	
	public void update(SalaireMensuel salaire) {
		salaire.validateOrThrow();
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] arguments = {""+salaire.year, ""+salaire.monthNumber};
		
		ContentValues values = salaireToContentValues(salaire);
		db.update(SALAIRE_TABLE, values, SELECT_ONE, arguments);
	}
	
	/**
	 * Should be called when your activity does not need database access anymore, for example in its onDestroy method.
	 */
	public void close() {
		helper.close();
	}
	

}