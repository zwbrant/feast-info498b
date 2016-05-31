package edu.uw.info498b.feast;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 5/23/16.
 */

public class DetailFeastActivity extends AppCompatActivity {
    private static final String TAG = "DetailFeastActivity";
    private Bundle bundle;
    private Feast feast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feast);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            int position = bundle.getInt("position");
            feast = MainActivity.feastsAdapter.getItem(position);

            ((TextView)findViewById(R.id.item_title)).setText(feast.name);
            ((TextView) findViewById(R.id.item_time)).setText(feast.time);
            ((TextView) findViewById(R.id.item_date)).setText(feast.date);

//            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
//            ((TextView) findViewById(R.id.item_time)).setText(timeFormat.format(new Date(feast.time)));
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
//            ((TextView) findViewById(R.id.item_date)).setText(dateFormat.format(new Date(feast.date)));


            // people



            // food category
            feast.categories.put("Korean", 10);
            feast.categories.put("Chinese", 8);
            feast.categories.put("Mexican", 15);

            ArrayList<String> categoryList = new ArrayList<String>();
            categoryList.addAll(feast.categories.keySet());

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.detail_category_list);
            for (int i = 0; i < feast.categories.size(); i++) {
                RelativeLayout relative = new RelativeLayout(this);
                String categoryKey = categoryList.get(i);
                int votes = feast.categories.get(categoryKey);
                TextView textView = new TextView(this);
                TextView background = new TextView(this);
                textView.setText(categoryKey + " " + votes);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                textView.setId(i);
                background.setBackgroundResource(R.color.colorLightGray);
                background.setLayoutParams(new LinearLayout.LayoutParams(votes * 50, LinearLayout.LayoutParams.WRAP_CONTENT));
                relative.addView(background);
                relative.addView(textView);
                linearLayout.addView(relative);
            }

            if (feast.completed) {
                // result page
                ((ImageView) findViewById(R.id.detail_winning_image)).setVisibility(View.VISIBLE);
//                ((TextView) findViewById(R.id.detail_winning_text)).setText(feast.winning);
                ((TextView) findViewById(R.id.detail_winning_text)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.detail_close_button)).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void handleClosePoll(View v) {
        Toast.makeText(this, "Poll closed!", Toast.LENGTH_SHORT).show();
        feast.setCompleted(true);
        ((ImageView) findViewById(R.id.detail_winning_image)).setVisibility(View.VISIBLE);
//                ((TextView) findViewById(R.id.detail_winning_text)).setText(feast.winning);
        ((TextView) findViewById(R.id.detail_winning_text)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.detail_close_button)).setVisibility(View.INVISIBLE);
    }
}
