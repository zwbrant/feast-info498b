package edu.uw.info498b.feast;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Nick on 5/23/16.
 */
public class NewFeastActivity extends AppCompatActivity {
    private static final String TAG = "NewFeastActivity";
    public static final String ACTION_SMS_SENT = "edu.uw.info498b.feast.ACTION_SMS_SENT";

    private static final int SEND_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        Log.v("TAG", "1234123eds");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feast);
        setTitle("New Feast");
    }

    public void handleSendPoll(View v) {
        Log.v(TAG, "sent poll");

        SmsManager smsManager = SmsManager.getDefault();

        Intent smsIntent = new Intent(ACTION_SMS_SENT);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SEND_CODE, smsIntent, 0);
        smsManager.sendTextMessage("5554",null, "THIS IS A TEST TEXT. PLEASE IGNORE. THANKS",pendingIntent, null);
        Toast.makeText(this, "Poll sent!", Toast.LENGTH_SHORT).show();

    }
}
