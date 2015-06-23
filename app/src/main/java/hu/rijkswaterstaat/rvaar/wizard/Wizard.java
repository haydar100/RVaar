package hu.rijkswaterstaat.rvaar.wizard;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import hu.rijkswaterstaat.rvaar.R;

public class Wizard extends FragmentActivity {
    private ViewPager mPager;
    private MyPagerAdapter adapter;
    private Button volgende;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        mPager = (ViewPager)findViewById(R.id.pager);
        adapter = new MyPagerAdapter((getSupportFragmentManager()));
        mPager.setAdapter(adapter);
        volgende = (Button)findViewById(R.id.volgende);
        volgende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("curr", Integer.toString(mPager.getCurrentItem()));
                if(mPager.getCurrentItem() < 2) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }else{
                    finish();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wizard, menu);
        return true;
    }

    public void skipWizard(View v){
        this.finish();
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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return WizardFragment.newInstance();
                case 1: return WizardFragment2.newInstance();
                case 2: return WizardFragment3.newInstance();
                default: return WizardFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
