package edu.uw.info498b.feast;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
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
import java.util.Set;

/**
 * Created by Nick on 5/23/16.
 */

public class DetailFeastActivity extends AppCompatActivity {
    private static final String TAG = "DetailFeastActivity";
    private Bundle bundle;
    private Feast feast;
    private String winningCateg;
    public static final String ACTION_SMS_SENT = "edu.uw.info498b.feast.ACTION_SMS_SENT";
    private static final int SEND_CODE = 0;

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


            findViewById(R.id.item_icon).setBackgroundColor(feast.getColor());


            // people
            if (feast.getPhonenumbers() != null) {
                Set<String> numbers = (Set<String>) feast.getPhonenumbers().keySet();
                LinearLayout contactContainer = (LinearLayout)findViewById(R.id.item_contactImages);

                for (String key :numbers) {
                    String name = feast.getPhonenumbers().get(key);
                    Log.v(TAG, "****Adding " + name);
                    String[] subNames = name.split(" ");
                    String initial = "";

                    initial += Character.toUpperCase(subNames[0].charAt(0));
                    if (subNames.length > 1)
                        initial += Character.toUpperCase(subNames[subNames.length - 1].charAt(0));

                    TextView textView = new TextView(this);
                    textView.setText(initial);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    textView.setBackgroundColor(this.getResources().getColor(R.color.colorJelly));

                    LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(lps);
                    lps.setMargins(0,0, 15,10);
//                textView.setId(i);


                    contactContainer.addView(textView);


                }
            } else {
                Log.v(TAG, "****No Numbers detected ");

            }



            // food category
//            feast.categories.put("Korean", 10);
//            feast.categories.put("Chinese", 8);
//            feast.categories.put("Mexican", 15);

            ArrayList<String> categoryList = new ArrayList<String>();
            categoryList.addAll(feast.categories.keySet());

            int winningVote = 0;

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.detail_category_list);
            for (int i = 0; i < feast.categories.size(); i++) {
                RelativeLayout relative = new RelativeLayout(this);

                String categoryKey = categoryList.get(i);
                int votes = feast.categories.get(categoryKey);

                if (votes > winningVote) {
                    winningVote = votes;
                    winningCateg = categoryKey;
                }

                TextView textView = new TextView(this);
                textView.setText(categoryKey + " " + votes);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                textView.setId(i);

                TextView background = new TextView(this);
                background.setBackgroundResource(R.color.colorLightGray);
                background.setLayoutParams(new LinearLayout.LayoutParams(votes * 50, LinearLayout.LayoutParams.WRAP_CONTENT));

                relative.addView(background);
                relative.addView(textView);
                linearLayout.addView(relative);
            }

            if (feast.completed) {
                // result page
                result();
            }
        }
    }

    public void handleClosePoll(View v) {
        Toast.makeText(this, "Poll closed!", Toast.LENGTH_SHORT).show();
        feast.setCompleted(true);
        Log.v(TAG, "sent result");

        String pollString = "Feast Poll (at " + feast.date + " " + feast.time + "): \n " +
                feast.name + " \n" +
                "Host has closed poll. \n" + " \n" +
                "Final Result: \n" + winningCateg +
                " \n";

        for(String number: feast.phonenumbers.keySet()) {
            SmsManager smsManager = SmsManager.getDefault();

            Intent smsIntent = new Intent(ACTION_SMS_SENT);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SEND_CODE, smsIntent, 0);
            smsManager.sendTextMessage(number, null, pollString, pendingIntent, null);
            Toast.makeText(this, "Result sent!", Toast.LENGTH_SHORT).show();
        }

        result();
    }

    public void result() {
        ((ImageView) findViewById(R.id.detail_winning_image)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.detail_winning_text)).setText(winningCateg);
        ((TextView) findViewById(R.id.detail_winning_text)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.detail_close_button)).setVisibility(View.INVISIBLE);
    }
}
