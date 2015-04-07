package hu.rijkswaterstaat.rvaar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import hu.rijkswaterstaat.rvaar.utils.PreferencesActivity;


public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_rvaar);

    }
    public void buttonOnClick(View v){

        Button button =(Button) v;
        startActivity(new Intent(getApplicationContext(), OverviewMap.class));
/**/
    }
    public void onClick_Aboutus(View v){

        Button button =(Button) v;
        startActivity(new Intent(getApplicationContext(), About_us.class));
/**/
    }


    public void onClick_Preferences(View v) {

        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), PreferencesActivity.class));
/**/
    }

    public void onClick_Checklist(View v) {

        Button button = (Button) v;
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
