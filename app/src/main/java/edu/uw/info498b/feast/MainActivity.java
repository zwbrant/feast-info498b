package edu.uw.info498b.feast;

import android.animation.FloatEvaluator;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static FeastArrayAdapter feastsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "add new stuf3wqejrfnd safkjaf");

                Intent intent = new Intent(MainActivity.this, NewFeastActivity.class);
                startActivity(intent);
            }
        });

    }

    void initListView() {
        ListView listView = (ListView)findViewById(R.id.listView);

        ArrayList<Feast> feasts = new ArrayList<Feast>();
        //Example implementation of the ListView, with mock entries
        //Not sure where the actual Feast data should be stored.
        feasts.add(new Feast("salmon"));
        feasts.add(new Feast("salmon"));
        feasts.add(new Feast("salmon")); //Don't ask me why it's salmon
        feasts.add(new Feast("salmon"));
        feastsAdapter = new FeastArrayAdapter(this, feasts);

        if(listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Feast feast = (Feast) parent.getItemAtPosition(position);
                    Log.v(TAG, "You clicked on: " + feast);
                    Intent intent = new Intent(MainActivity.this, DetailFeastActivity.class);
//                Bundle extra = new Bundle();
//                extra.putLong("id", id);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });

            listView.setAdapter(feastsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
