package hu.rijkswaterstaat.rvaar.utils;

import android.os.Bundle;

import hu.rijkswaterstaat.rvaar.R;
import hu.rijkswaterstaat.rvaar.menu.MenuActivity;

/**
 * Created by Haydar on 07-04-15.
 */
public class PreferencesActivity extends MenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        setMenu();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new PreferenceFragment()).commit();


    }

    public static class PreferenceFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }


}
