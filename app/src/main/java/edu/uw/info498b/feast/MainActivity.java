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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String jsonFileName = "feastJson.json" ;

    public static ArrayList<Feast> feasts;
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

                Intent intent = new Intent(MainActivity.this, NewFeastActivity.class);
                startActivity(intent);
            }
        });

    }

    void initListView() {
        ListView listView = (ListView)findViewById(R.id.listView);

        if(feasts == null) {
            feasts = new ArrayList<>();
        }

        if(feastsAdapter == null) {
            feastsAdapter = new FeastArrayAdapter(this, feasts);
        }
        String json = tryLoadJson();
        if (json == null) {
            Log.v(TAG, "***No JSON found");


            //Example implementation of the ListView, with mock entries
            //Not sure where the actual Feast data should be stored.
//
//            feasts.add(new Feast("Salmon celebration", "8:45pm","Apr 30th",new Date()));
//            feasts.add(new Feast("Mongoose fest", "8:45pm","Sep 30th",new Date()));
//            feasts.add(new Feast("Platypus party", "3:45pm","Oct 30th",new Date()));
//            feasts.add(new Feast("Dachshund dance", "5:45pm","May 30th",new Date()));

        } else {
            Gson gson = new Gson();
            Log.v(TAG, "***Loading JSON");

            Log.v(TAG, "" + feastsAdapter.getCount());

            Type collectionType = new TypeToken<ArrayList<Feast>>(){}.getType();
            feasts = gson.fromJson(json, collectionType);
        }



        if(listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Feast feast = (Feast) parent.getItemAtPosition(position);
                    Log.v(TAG, "You clicked on: " + feast);
                    Intent intent = new Intent(MainActivity.this, DetailFeastActivity.class);
                    Bundle extra = new Bundle();
                    extra.putInt("position", position);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            });

            listView.setAdapter(feastsAdapter);
        }
    }

    String tryLoadJson(){
        try {
            File f = new File(getFilesDir().getPath() + "/" + jsonFileName);
            if (f == null)
                return null;
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String json = gson.toJson(feasts);

        try {
            FileWriter file = new FileWriter(getFilesDir().getPath() + "/" + jsonFileName);
            file.write(json);
            file.flush();
            file.close();
            Log.v(TAG, "***Saved JSON");
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
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
