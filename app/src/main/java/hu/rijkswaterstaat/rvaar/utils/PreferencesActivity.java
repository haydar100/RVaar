package hu.rijkswaterstaat.rvaar.utils;

import android.os.Bundle;

import hu.rijkswaterstaat.rvaar.R;
import hu.rijkswaterstaat.rvaar.menu.MenuActivity;

/**
 * Created by Haydar on 07-04-15.
 */
public class PreferencesActivity extends MenuActivity {
    static String[] drawerItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        drawerItems = getResources().getStringArray(R.array.drawerItems);
        setMenu(drawerItems);
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
