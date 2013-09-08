package project.androidfinal;

import project.androidfinal.R;
import android.os.Bundle;

import android.view.Menu;
import android.preference.PreferenceActivity;

public class SettingsActivity extends  PreferenceActivity{

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flight_preferences);
        this.addPreferencesFromResource(R.xml.preferences);
    }


}
