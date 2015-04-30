package hu.rijkswaterstaat.rvaar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hu.rijkswaterstaat.rvaar.dao.MarkerDAOimpl;
import hu.rijkswaterstaat.rvaar.domain.TipsAndTricks;
import hu.rijkswaterstaat.rvaar.menu.MenuActivity;
import hu.rijkswaterstaat.rvaar.sqlite.SQLiteHelper;
import hu.rijkswaterstaat.rvaar.webservice.WSConnector;


public class TipsActivity extends MenuActivity {
    private static final String DB_NAME = "RvaarDB";
    private static final String TABLE_NAME = "tipsAndTricks";
    private static final String tipsAndTricks_ID = "_id";
    private static final String tipsAndTricks_headerName = "headerName";
    private static final String tipsAndTricks_content = "content";
    public ArrayList<TipsAndTricks> tipsAndTricks = new ArrayList<TipsAndTricks>();
    protected ArrayList<String> headers = new ArrayList<String>();
    protected ArrayList list = new ArrayList();
    protected List content = new ArrayList();
    protected ArrayList markersTest = new ArrayList();
    private SQLiteDatabase database;
    private ListView listView;
    //private String[] tips;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        setMenu();
        listView = (ListView) findViewById(R.id.tips_category);
        SQLiteHelper sqllite = new SQLiteHelper(this, DB_NAME);
        database = sqllite.openDataBase();


        // tips = getResources().getStringArray(R.array.tips_categories);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    WSConnector wsc = new WSConnector();
                    MarkerDAOimpl dao = new MarkerDAOimpl();
                    tipsAndTricks = fillTipsAndTricks();
                    Log.d(tipsAndTricks.size() + "", "");
                } else {
                    //tipsAndTricks = new ArrayList<String>();
                    tipsAndTricks = fillTipsAndTricks();

                }


            }
        });
        t1.start();
        try {
            t1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
        ArrayList<TipsAndTricks> kaas = new ArrayList<TipsAndTricks>();
//        for (int i = 0; i < tips.length; i++) {
//            String[] seperated = tips[i].split(",");
//            list.add(seperated[0]);
//            content.add(seperated[1]);
//        }
        for (TipsAndTricks tt : tipsAndTricks) {
            headers.add(tt.getHeaderName());

        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, headers));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TipsContentActivity.class);

                for (int i = 0; i < tipsAndTricks.size(); i++) {
                    if (tipsAndTricks.get(position) != null) {
                        String cnt = tipsAndTricks.get(position).getContent();
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

    private ArrayList<TipsAndTricks> fillTipsAndTricks() {
        tipsAndTricks = new ArrayList<TipsAndTricks>();
        Cursor tipsAndTricksCursor = database.query(TABLE_NAME, new String[]{tipsAndTricks_ID, tipsAndTricks_headerName, tipsAndTricks_content}, null, null, null, null, tipsAndTricks_headerName);
        tipsAndTricksCursor.moveToFirst();
        if (!tipsAndTricksCursor.isAfterLast()) {
            do {
                String headerName = tipsAndTricksCursor.getString(1);
                String content = tipsAndTricksCursor.getString(2);
                TipsAndTricks tipstricks = new TipsAndTricks(headerName, content);
                tipsAndTricks.add(tipstricks);
                Log.d(tipsAndTricks.size() + "aminakoyim", "");

            } while (tipsAndTricksCursor.moveToNext());
            {

            }


        }
        tipsAndTricksCursor.close();
        return tipsAndTricks;
    }
}



