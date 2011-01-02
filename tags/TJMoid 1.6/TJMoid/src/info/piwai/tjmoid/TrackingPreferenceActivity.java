package info.piwai.tjmoid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public abstract class TrackingPreferenceActivity extends PreferenceActivity {

	private GoogleAnalyticsTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();

		tracker.start(Constants.ANALYTICS_ID, 20, this);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("/") //
		.append(Constants.VERSION) //
		.append("/") //
		.append(Constants.VERSION_NAME) //
		.append("/") //
		.append(getClass().getSimpleName());
				
		
		tracker.trackPageView(sb.toString());
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
