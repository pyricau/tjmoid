package info.piwai.tjmoid;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public abstract class TrackingActivity extends Activity {

	private GoogleAnalyticsTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();

		tracker.start(Constants.ANALYTICS_ID, 20, this);
		
		new Thread() {
			@Override
			public void run() {
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

				tracker.setCustomVar(1, "versionName", versionName, 2);
				tracker.setCustomVar(2, "versionCode", versionCode, 2);

				tracker.setCustomVar(3, "release", Build.VERSION.RELEASE, 2);
				tracker.setCustomVar(4, "model", Build.MODEL, 2);

				tracker.trackPageView("/" + getClass().getSimpleName());
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		tracker.stop();
	}

	public GoogleAnalyticsTracker getTracker() {
		return tracker;
	}

}
