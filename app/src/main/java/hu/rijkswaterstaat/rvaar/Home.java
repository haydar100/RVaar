package hu.rijkswaterstaat.rvaar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import hu.rijkswaterstaat.rvaar.menu.MenuActivity;
import hu.rijkswaterstaat.rvaar.utils.PreferencesActivity;


public class Home extends MenuActivity {
    private static String BOAT_NAME = "";
    private String valueOf;
//public String BOAT_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_icon_app);
        setMenu();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        showPromptForUsername();
    }

    public void showPromptForUsername() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.titleAlertUser));
        alert.setMessage(getResources().getString(R.string.bootnaaminfo));
        final EditText input = new EditText(this);
        input.setHint("Anoniem");
        //  BOAT_NAME = String.valueOf(input.getText());
        alert.setView(input);

        if (!preferences.contains("showedPromptForUsernameOnStartup")) {


            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String valueOfInput = String.valueOf(input.getText());
                    valueOf = String.valueOf(input.getText());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("boatType", "Sloep");
                    editor.putString("BOAT_NAME", valueOfInput);
                    editor.putBoolean("showedPromptForUsernameOnStartup", true);
                    editor.commit();
                }
            });

            alert.setNegativeButton("Nee, bedankt", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String valueOfInput = String.valueOf(input.getText());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("boatType", "Sloep");
                    editor.putString("BOAT_NAME", valueOfInput);
                    editor.putBoolean("showedPromptForUsernameOnStartup", true);
                    editor.commit();
                }
            });
            alert.show();
        }
    }

    public void buttonOnClick(View v) {
        startActivity(new Intent(getApplicationContext(), OverviewMap.class));
/**/
    }

    public void onClick_Aboutus(View v) {
        startActivity(new Intent(getApplicationContext(), SOS.class));
/**/
    }

    public void onClick_Quiz(View v) {
        startActivity(new Intent(getApplicationContext(), Quiz.class));
/**/
    }


    public void onClick_Preferences(View v) {
        startActivity(new Intent(getApplicationContext(), PreferencesActivity.class));
/**/
    }

    public void onClick_Tips(View v) {
        startActivity(new Intent(getApplicationContext(), TipsActivity.class));
    }

    public void onClick_Checklist(View v) {
        startActivity(new Intent(getApplicationContext(), Checklist.class));
/**/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
