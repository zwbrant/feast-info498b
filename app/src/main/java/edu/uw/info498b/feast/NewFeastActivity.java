package edu.uw.info498b.feast;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nick on 5/23/16.
 */
public class NewFeastActivity extends AppCompatActivity {
    private static final String TAG = "NewFeastActivity";
    public static final String ACTION_SMS_SENT = "edu.uw.info498b.feast.ACTION_SMS_SENT";
    private static final int SEND_CODE = 0;
    private static final int PICK_CONTACT_REQUEST = 1;
    private HashMap<String, String> numbers;
    public static String date;
    public static String time;
    private String title;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feast);
        setTitle("New Feast");
        numbers = new HashMap<>();
        //controller
        adapter = new ArrayAdapter<String>(this,
                R.layout.category_item, R.id.txtItem, new ArrayList<String>()); //define adapter

        ListView listView = (ListView)findViewById(R.id.category_list);
        listView.setAdapter(adapter); //set adapter
    }

    public void handleSendPoll(View v) {
        Log.v(TAG, "sent poll");
        HashMap<String, Integer> map = new HashMap<>();
        title = ((EditText) this.findViewById(R.id.edit_title)).getText().toString();
        String pollString = "Feast Poll (at " + date + " " + time + "): \n " +
                title + " \n" +
                "Reply with name to vote: \n" +
                " \n";

        for(int i = 0; i < adapter.getCount(); i++) {
            pollString += (i + 1) + ". " + adapter.getItem(i) + " \n";
            map.put(adapter.getItem(i), 0);
        }

        pollString += "\n" +
                "OR REPLY \"FEAST: [custom entry]\" to add to the poll";


        for(String number: numbers.keySet()) {
            SmsManager smsManager = SmsManager.getDefault();

            Intent smsIntent = new Intent(ACTION_SMS_SENT);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SEND_CODE, smsIntent, 0);
            smsManager.sendTextMessage(number, null, pollString, pendingIntent, null);
            Toast.makeText(this, "Poll sent!", Toast.LENGTH_SHORT).show();
        }


        Feast feast = new Feast(title, date, time, new Date(), map, numbers);
        MainActivity.feastsAdapter.add(feast);


        numbers.clear();

    }

    public void handleAddPerson(View v) {
        pickContact();
    }

    //Allows the user to select a contact from their phone's existing list
    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    //Parses phone number from selected contact
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int columnNames = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number = cursor.getString(column);
                String name = cursor.getString(columnNames);
                numbers.put(number, name);
                Log.v(TAG, number + " " + name);

            }
        }
    }

    public void handleAddCategory(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Selection");
        alert.setTitle("Add a New Option");

        alert.setView(edittext);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String category = edittext.getText().toString();
                adapter.add(category);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();


    }

    public void handlePickTime(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void handlePickDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time = timeFormat(hourOfDay, minute);
            Log.v(TAG, time);
        }

        private String timeFormat (int hourOfDay, int minute) {
            String stamp = "";
            String minutes = "" + minute;

            if (minute < 10) {
                minutes = "0" + minute;
            }
            if(hourOfDay < 12) {
                stamp = "am";

            } else {
                hourOfDay -= 12;
                stamp = "pm";
            }
            if(hourOfDay == 0) {
                hourOfDay = 12;
            }
            return hourOfDay + ":" + minutes + " " + stamp;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            date = (month + 1) +"/" + day + "/" + year;
            Log.v(TAG, date);
        }
    }
}
