package hu.rijkswaterstaat.rvaar;
//https://guides.codepath.com/android/Basic-Todo-App-Tutorial
//http://www.anwbwatersport.nl/vaarinformatie/varen-in-europa/checklist-voor-vertrekkers.html

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Checklist extends ActionBarActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = getSavedData(sp);

        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                     /*   items.remove(pos);*/
                        items.remove(pos);
                        //item.setBackgroundColor(Color.parseColor("#803a9200"));

                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }
                });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.parseColor("#803a9200"));
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checklist, menu);
        return true;
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.trim().length() > 0) {
            items.add(itemText);
            etNewItem.setText("");
            itemsAdapter.notifyDataSetChanged();
        } else {
            etNewItem.setError("Veld mag niet leeg zijn!");
        }
    }

    void saveData(ArrayList<String> data, SharedPreferences sp) {
        Set<String> dataset = new HashSet<>();
        dataset.addAll(data);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("CHECKLIST", dataset);
        editor.commit();
    }

    ArrayList<String> getSavedData(SharedPreferences sp) {
        ArrayList<String> result;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("CHECKLIST", null);
        if (set != null) {
            result = new ArrayList<>(set);
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

    public void onDestroy() {
        super.onDestroy();
        saveData(items, sp);
    }

    public void onPause() {
        super.onPause();
        saveData(items, sp);
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
