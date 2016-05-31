package edu.uw.info498b.feast;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by astro.domine on 5/21/2016.
 * Adapter needed to use the ListView with Feasts; the standard ArrayAdapters (Simple)
 * only adapt Strings. They populate one TextView in the given item layout by calling the
 * array's toString() method. This custom implementation allows us to use all the data
 * in a Feast to populate the feast_list_item layout (icon, title, etc.).
 */

public class FeastArrayAdapter extends ArrayAdapter<Feast> {
    private static final String TAG = "FeastAdapater";
    private final Context context;
    private final ArrayList<Feast> feasts;

    public FeastArrayAdapter(Context context, ArrayList<Feast> feasts) {
        super(context, -1, feasts);
        this.context = context;
        this.feasts = feasts;
    }

    //Called when the ListView is populating the list from the feasts ArrayList
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        if (convertView == null) //If the adapter isn't populating an existing list item
             itemView = inflater.inflate(R.layout.feast_list_item, parent, false);
        else
            itemView = convertView;

        //Disabled these until the layout is finalized
        ImageView icon = (ImageView)itemView.findViewById(R.id.item_icon);
        TextView title = (TextView)itemView.findViewById(R.id.item_title);
        TextView time = (TextView)itemView.findViewById(R.id.item_time);
        TextView date = (TextView)itemView.findViewById(R.id.item_date);
        LinearLayout contactContainer = (LinearLayout)itemView.findViewById(R.id.item_contactContainer);
        if(((LinearLayout) contactContainer).getChildCount() > 0)
            ((LinearLayout) contactContainer).removeAllViews();

        title.setText(feasts.get(position).getName());
        time.setText(feasts.get(position).getTime());
        date.setText(feasts.get(position).getDate());

        if (feasts.get(position).getPhonenumbers() != null) {

            Set<String> numbers = (Set<String>) feasts.get(position).getPhonenumbers().keySet();

            Log.v(TAG, "****" + title.getText() + " Numbers detected " + numbers.size());

            for (String key :numbers) {
                String name = feasts.get(position).getPhonenumbers().get(key);
                Log.v(TAG, "****Adding " + name);
                String[] subNames = name.split(" ");
                String initial = "";

                initial += Character.toUpperCase(subNames[0].charAt(0));
                if (subNames.length > 1)
                    initial += Character.toUpperCase(subNames[subNames.length - 1].charAt(0));

                TextView textView = new TextView(getContext());
                textView.setText(initial);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(lps);
                lps.setMargins(0,0, 10,0);
//                textView.setId(i);


                contactContainer.addView(textView);


            }
        } else {
            Log.v(TAG, "****No Numbers detected ");

        }


        return itemView;
    }


}

