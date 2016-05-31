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

                if (body.startsWith("FEAST: ")) {
                    Log.d(TAG, "This is a message FEAST cares about. FROM: " + number + " BODY: " + body);
                    if (body.length() > 8) {
                        body = body.substring(7);
                        processMessage(body);
                    } else {
                        Log.d(TAG, "No command was entered.");
                    }
                }


            } catch(Exception e){
                Log.d("Exception caught",e.getMessage());
            }
        }
    }

    private void processMessage(String body) {
        if (body.startsWith("Vote ")) {
            Log.d(TAG, "We are voting");

            if (body.length() > 7) {
                body = body.substring(6);
                body.replace(",", " ");

                Scanner lineScanner = new Scanner(body);
//                while()

            } else {
                Log.d(TAG, "Vote command registered but nothing was voted for");
            }
        }
    }
}
