package edu.uw.info498b.feast;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
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
    private static int MAX_SMS_MESSAGE_LENGTH = 160;
    private HashMap<String, String> numbers;
    public static String date;
    public static String time;
    private String title;
    private ArrayAdapter<String> adapterCategory;
    private ArrayAdapter<String> adapterPeople;
    private ListView listViewCategory;
    private ListView listViewPeople;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "*****NewFeast OnCreate");

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feast);
        setTitle("New Feast");
        numbers = new HashMap<>();
        //controller
        adapterCategory = new ArrayAdapter<>(this,
                R.layout.category_item, R.id.txtItem, new ArrayList<String>()); //define adapter

        adapterPeople = new ArrayAdapter<>(this,
                R.layout.category_item, R.id.txtItem, new ArrayList<String>()); //define adapter

        listViewCategory = (ListView)findViewById(R.id.category_list);
        listViewPeople = (ListView)findViewById(R.id.person_list);
        listViewCategory.setAdapter(adapterCategory); //set adapter
        listViewPeople.setAdapter(adapterPeople);
    }


    public void handleSendPoll(View v) {
        title = ((EditText) this.findViewById(R.id.edit_title)).getText().toString();

        if (adapterPeople.getCount() > 0 && adapterCategory.getCount() > 0 && title.length() > 0) {
            Log.v(TAG, "Sending poll");

            HashMap<String, Integer> map = new HashMap<>();
            int targetFeast = MainActivity.feastsAdapter.getCount() + 1;
            String pollString = "Feast Poll (at " + date + " " + time + "): \n" +
                    title + "\n" +
                    "Reply with 'FEAST " + targetFeast + " Vote' followed by each category you'd like to vote for." + "\n" +
                    "Eg: 'FEAST " + targetFeast + " Vote mexican, italian, chinese" + "\n";

            for(int i = 0; i < adapterCategory.getCount(); i++) {
                pollString += (i + 1) + ". " + adapterCategory.getItem(i) + "\n";
                map.put(adapterCategory.getItem(i), 0);
            }

            pollString += "\n" +
                    "To add a category simply vote for it.";

            //Send an SMS to each number
            for(String number: numbers.keySet()) {
                Log.d(TAG, "Sending text to: " + number);
                SmsManager smsManager = SmsManager.getDefault();
                Intent smsIntent = new Intent(ACTION_SMS_SENT);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SEND_CODE, smsIntent, 0);
                int length = pollString.length();
                if(length > MAX_SMS_MESSAGE_LENGTH) {
                    ArrayList<String> messagelist = smsManager.divideMessage(pollString);
                    smsManager.sendMultipartTextMessage(number, null, messagelist, null, null);
                } else {
                    smsManager.sendTextMessage(number, null, pollString, pendingIntent, null);

                }
            }

            Feast feast = new Feast(title, date, time, new Date(), map, numbers);
            Log.v(TAG, feast.toString());

            if(MainActivity.feastsAdapter.getCount() < MainActivity.feastsAdapter.getCount() + 1) {
                MainActivity.feastsAdapter.add(feast);
                Log.d(TAG, MainActivity.feastsAdapter.getCount() + " and " + MainActivity.feasts.size());
                Log.v(TAG, "feast added");
            }

            Toast.makeText(this, "Poll sent!", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
        }
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
                adapterPeople.add(name);
                numbers.put(number, name);
                Log.v(TAG, number + " " + name);
                setListViewHeightBasedOnChildren(listViewPeople);
            }
        }

    }

    @Override
    public void onUserInteraction() {
        setListViewHeightBasedOnChildren(listViewCategory);
        super.onUserInteraction();
    }

    public void handleAddCategory(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Selection");
        alert.setTitle("Add a New Option");

        alert.setView(edittext);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String category = edittext.getText().toString().toLowerCase();
                adapterCategory.add(category);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
