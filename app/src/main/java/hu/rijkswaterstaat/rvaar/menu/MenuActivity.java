package hu.rijkswaterstaat.rvaar.menu;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import hu.rijkswaterstaat.rvaar.AccordianSampleActivity;
import hu.rijkswaterstaat.rvaar.Checklist;
import hu.rijkswaterstaat.rvaar.Home;
import hu.rijkswaterstaat.rvaar.OverviewMap;
import hu.rijkswaterstaat.rvaar.Quiz;
import hu.rijkswaterstaat.rvaar.R;
import hu.rijkswaterstaat.rvaar.TipsActivity;
import hu.rijkswaterstaat.rvaar.utils.PreferencesActivity;


public class MenuActivity extends ActionBarActivity {
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void setMenu(String[] items) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout != null) {
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        if(mDrawerList != null) {
            mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }
        //Toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_closed) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the activity_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        // Highlight the selected item, update the title, and close the drawer
        switch (position) {
            case 0: {
                Intent a = new Intent(this, Home.class);
                startActivity(a);
            }
            break;
            case 1:
                Intent b = new Intent(this, OverviewMap.class);
                startActivity(b);
                break;
            case 2:
                Intent c = new Intent(this, Checklist.class);
                startActivity(c);
                break;
            case 3:
                Intent d = new Intent(this, TipsActivity.class);
                startActivity(d);
                break;
            case 4:
                Intent e = new Intent(this, Quiz.class);
                startActivity(e);
                break;
            case 5:
                Intent f = new Intent(this, PreferencesActivity.class);
                startActivity(f);
                break;
            case 6:
                Intent g = new Intent(this,AccordianSampleActivity.class);
                startActivity(g);
                break;
            default:
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

    }
}



