package edu.uw.info498b.feast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nick on 5/30/16.
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MessageReceiver";

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received something" + intent.toString());
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String number = "";
            String body = "";
            Bundle extras = intent.getExtras();

            try{
                Object[] pdus = (Object[]) extras.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                msgs[0] = SmsMessage.createFromPdu((byte[])pdus[0]);
                number = msgs[0].getOriginatingAddress();
                body = msgs[0].getMessageBody();

                Log.d(TAG, "This is the received message. FROM: " + number + " BODY: " + body);
                processMessage(body);



            } catch(Exception e){
                Log.d("Exception caught",e.getMessage());
            }
        }
    }

    private void processMessage(String body) {
        if (body.startsWith("FEAST ")) {
            Log.d(TAG, "I care about this message");
            body = body.replace(",", "");
            String[] parts = body.split(" ");

            if (parts.length > 3) {
                int targetFeast = Integer.parseInt(parts[1]);
                String command = parts[2].toLowerCase();

                switch (command) {
                    case "vote" :
                        Log.d(TAG, MainActivity.feasts.size() +"");
                        Log.d(TAG, MainActivity.feastsAdapter.getCount() + "'");
                        Feast feast = MainActivity.feasts.get(targetFeast - 1);
                        for (int i = 3; i < parts.length; i++) {
                            feast.vote(parts[i]);
                            Log.d(TAG, "Voting for: " + parts[i]);
                        }
                        return;
                    case "add" :
                        return;
                    default :
                        break;
                }
            } else {
                Log.d(TAG, "User left out something");
            }

        } else {
            Log.d(TAG, "I don't care about this message");
        }
    }
}
