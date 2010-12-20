package info.piwai.tjmoid;

import android.app.Application;

import com.admob.android.ads.AdManager;

public class TJMoidApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		String[] testDevices = { AdManager.TEST_EMULATOR };
		AdManager.setTestDevices(testDevices);
	}

}
