package edu.uw.info498b.feast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.ivbaranov.mli.MaterialLetterIcon;

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

        Feast feast = feasts.get(position);

        //Disabled these until the layout is finalized
        ImageView icon = (ImageView)itemView.findViewById(R.id.item_icon);
        icon.setBackgroundColor(feasts.get(position).getColor());
        TextView title = (TextView)itemView.findViewById(R.id.item_title);
        TextView time = (TextView)itemView.findViewById(R.id.item_time);
        TextView date = (TextView)itemView.findViewById(R.id.item_date);
        LinearLayout contactContainer = (LinearLayout)itemView.findViewById(R.id.item_contactContainer);

        title.setText(feast.getName());
        time.setText(feast.getTime());
        date.setText(feast.getDate());

        if (contactContainer.getChildCount() > 0) {
            contactContainer.removeAllViews();
        }
        if (feast.getPhonenumbers() != null) {

            Set<String> numbers = (Set<String>) feast.getPhonenumbers().keySet();

            Log.v(TAG, "****" + title.getText() + " Numbers detected " + numbers.size());

            for (String key :numbers) {
                String name = feast.getPhonenumbers().get(key);
                Log.v(TAG, "****Adding " + name);
                String[] subNames = name.split(" ");
                String initial = "";

                initial += Character.toUpperCase(subNames[0].charAt(0));
                if (subNames.length > 1)
                    initial += Character.toUpperCase(subNames[subNames.length - 1].charAt(0));

                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(initial);

                ImageView imgView = new ImageView(getContext());

                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .width(95)  // width in px
                        .height(95) // height in px
                        .useFont(Typeface.DEFAULT_BOLD)
                        .endConfig()
                        .buildRound(initial, color);

                imgView.setImageDrawable(drawable);



                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lps.setMargins(0,0, 20,0);
//                lps.gravity = Gravity.LEFT;
                imgView.setLayoutParams(lps);

                //matIcon.setId(i);


                contactContainer.addView(imgView);


            }
        } else {
            Log.v(TAG, "****No Numbers detected ");

        }


        return itemView;
    }


}

