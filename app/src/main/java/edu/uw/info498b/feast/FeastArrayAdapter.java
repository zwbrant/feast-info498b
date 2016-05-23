package edu.uw.info498b.feast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by astro.domine on 5/21/2016.
 * Adapter needed to use the ListView with Feasts; the standard ArrayAdapters (Simple)
 * only adapt Strings. They populate one TextView in the given item layout by calling the
 * array's toString() method. This custom implementation allows us to use all the data
 * in a Feast to populate the feast_list_item layout (icon, title, etc.).
 */

public class FeastArrayAdapter extends ArrayAdapter<Feast> {
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
//        ImageView icon = (ImageView)itemView.findViewById(R.id.item_icon);
//        TextView firstLine = (TextView)itemView.findViewById(R.id.item_firstLine);
//        TextView secondLine = (TextView)itemView.findViewById(R.id.item_secondLine);

//        firstLine.setText(feasts.get(position).name);
//        secondLine.setText("Position: " + position);
//        icon.setImageResource(R.drawable.salmon);

        return itemView;
    }
}

