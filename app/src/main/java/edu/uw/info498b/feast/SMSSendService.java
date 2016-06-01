package edu.uw.info498b.feast;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Nick on 5/30/2016.
 * Service intent to handle sending messages.
 */
public class SMSSendService extends IntentService {
    private static final String TAG = "SMSSendService";

    private static final int SMS_SENT_CODE = 2;

    public static final String ACTION_SMS_STATUS = "edu.uw.info498b.feast.ACTION_SMS_STATUS";

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//

    public SMSSendService() {
        super(SMSSendService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_SMS_STATUS)) {
            Log.d(TAG, "Sending text!");
            Intent broadcastIntent = new Intent(ACTION_SMS_STATUS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SMS_SENT_CODE, broadcastIntent, 0);
            Bundle extra = intent.getExtras();
            String number = extra.getString("number");
            String message = extra.getString("message");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, pendingIntent, null);
        }
    }
}
