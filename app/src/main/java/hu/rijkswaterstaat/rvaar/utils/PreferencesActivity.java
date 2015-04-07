package hu.rijkswaterstaat.rvaar.utils;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hu.rijkswaterstaat.rvaar.R;

/**
 * Created by Haydar on 07-04-15.
 */
public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceFragment()).commit();
    }

    public static class PreferenceFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }


}
