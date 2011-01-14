package info.piwai.tjmoid;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.admob.android.ads.AdManager;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class TJMoidApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		String[] testDevices = { AdManager.TEST_EMULATOR };
		AdManager.setTestDevices(testDevices);
		
		GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
		
		String versionName;
		String versionCode;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = packageInfo.versionName;
			versionCode = String.valueOf(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			versionName = "UNKNOWN";
			versionCode = "0";
		}
		
		tracker.start(Constants.ANALYTICS_ID, this);
		
		tracker.trackEvent("versionName", versionName, null, 0);
		tracker.trackEvent("versionCode", versionCode, null, 0);

		tracker.trackEvent("release", Build.VERSION.RELEASE, null, 0);
		tracker.trackEvent("model", Build.MODEL, null, 0);
		
		tracker.dispatch();
		tracker.stop();
	}

}
