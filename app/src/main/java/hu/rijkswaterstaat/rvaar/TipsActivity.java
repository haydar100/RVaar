package hu.rijkswaterstaat.rvaar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hu.rijkswaterstaat.rvaar.dao.MarkerDAOimpl;
import hu.rijkswaterstaat.rvaar.menu.MenuActivity;
import hu.rijkswaterstaat.rvaar.webservice.WSConnector;


public class TipsActivity extends MenuActivity {
    protected ArrayList list = new ArrayList();
    protected List content = new ArrayList();
    protected ArrayList tipsAndTricks = new ArrayList();
    protected ArrayList markersTest = new ArrayList();

    private String[] drawerItems;
    private ListView listView;
    private String[] tips;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        drawerItems = getResources().getStringArray(R.array.drawerItems);
        setMenu(drawerItems);
        listView = (ListView) findViewById(R.id.tips_category);
        tips = getResources().getStringArray(R.array.tips_categories);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    WSConnector wsc = new WSConnector();
                    MarkerDAOimpl dao = new MarkerDAOimpl();
                    tipsAndTricks = dao.getTipsTricks();
                } else {
                    tipsAndTricks = new ArrayList<String>();
                }


            }
        });
        t1.start();
        try {
            t1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tips.length; i++) {
            String[] seperated = tips[i].split(",");
            list.add(seperated[0]);
            content.add(seperated[1]);
        }


        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TipsContentActivity.class);

                for (int i = 0; i < content.size(); i++) {
                    if (content.get(position) != null) {
                        String cnt = content.get(position).toString();
                        intent.putExtra("content", cnt);
                    }
                }
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tips, menu);
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
