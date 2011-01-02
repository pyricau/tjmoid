package info.piwai.tjmoid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class CheckMaestroActivity extends TrackingActivity {
	
	private static final String SALAIRE_URL = "https://ecoles.excilys.com/maestro-ref/personne/salairePersonne.htm?action=Valider&idPersonne=";
	
	private class CheckMaestroWebViewClient extends WebViewClient {

		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	    
	    @Override
	    public void onLoadResource(WebView view, String url) {
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    }
	}

	private static final String SECU_NUMBER_PREF = "secuNumberPref";
	private static final String MAESTRO_ID_PREF = "maestroIdPref";
	private static final int SECU_NUMBER_DIALOG = 1;
	private WebView webview;
	private EditText secuNumberEdit;
	private EditText maestroIdEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		webview = new WebView(this);
		setContentView(webview);
		
		webview.setWebViewClient(new CheckMaestroWebViewClient());
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);

		showDialog(SECU_NUMBER_DIALOG);
	}

	private String loadSecuNumber() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String secuNumber = preferences.getString(SECU_NUMBER_PREF, "");
		return secuNumber;
	}

	private String loadMaestroId() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String secuNumber = preferences.getString(MAESTRO_ID_PREF, "");
		return secuNumber;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Paramètres").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showDialog(SECU_NUMBER_DIALOG);
				return true;
			}
		});

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == SECU_NUMBER_DIALOG) {
			LayoutInflater inflater = getLayoutInflater();
			View dialogContent = inflater.inflate(R.layout.check_maestro_dialog, null);
			secuNumberEdit = (EditText) dialogContent.findViewById(R.id.secuNumberEdit);
			maestroIdEdit = (EditText) dialogContent.findViewById(R.id.maestroIdEdit);
			return new AlertDialog.Builder(this) //
					.setTitle("Numéro de securité sociale et identifiant Maestro") //
					.setMessage("Champ 1 : numéro de sécurité sociale, sans espace.\nChamp 2 : identifiant Maestro, visible dans le champ Id de Maestro-Ref.") //
					.setView(dialogContent) //
					.setPositiveButton("Charger", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String secuNumber = secuNumberEdit.getText().toString();
							String maestroId = maestroIdEdit.getText().toString();
							saveSecuNumberAndMaestroId(secuNumber, maestroId);
							showMaestroSalaryPage();
						}
					}) //
					.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String secuNumber = loadSecuNumber();
							if ("".equals(secuNumber)) {
								finish();
							}
						}
					}).create();
		}
		return super.onCreateDialog(id);
	}

	protected void saveSecuNumberAndMaestroId(String secuNumber, String maestroId) {
		getPreferences(MODE_PRIVATE) //
				.edit() //
				.putString(SECU_NUMBER_PREF, secuNumber) //
				.putString(MAESTRO_ID_PREF, maestroId) //
				.commit();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		if (id == SECU_NUMBER_DIALOG) {
			secuNumberEdit.setText(loadSecuNumber());
			maestroIdEdit.setText(loadMaestroId());
		}

		super.onPrepareDialog(id, dialog);
	}

	private void showMaestroSalaryPage() {
		
		String secuNumber = loadSecuNumber();
		
		//Padding secuNumber to avoid exceptions with substring when bad input is given
		String paddedSecuNumber = secuNumber+"000000000000000";
		
		String maestroId = loadMaestroId();
		StringBuffer sb = new StringBuffer();

		sb.append(SALAIRE_URL) //
				.append(maestroId)//
				.append("&numeroSecu=") //
				.append(secuNumber) //
				.append("&numeroSecu1=") //
				.append(paddedSecuNumber.substring(0, 1)) //
				.append("&numeroSecu2=") //
				.append(paddedSecuNumber.substring(1, 3)) //
				.append("&numeroSecu3=") //
				.append(paddedSecuNumber.substring(3, 5)) //
				.append("&numeroSecu4=") //
				.append(paddedSecuNumber.substring(5, 7)) //
				.append("&numeroSecu5=") //
				.append(paddedSecuNumber.substring(7, 10)) //
				.append("&numeroSecu6=") //
				.append(paddedSecuNumber.substring(10, 13)) //
				.append("&numeroSecu7=") //
				.append(paddedSecuNumber.substring(13, 15));
		
		String postUrl = sb.toString();
		
		webview.postUrl(postUrl, new byte[0]);

	}
}
