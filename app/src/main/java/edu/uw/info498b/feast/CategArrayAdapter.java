package edu.uw.info498b.feast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by inyong91 on 5/30/16.
 */
public class CategArrayAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> categName;
    private final ArrayList<Integer> vote;

    public CategArrayAdapter(Activity context, ArrayList<String> categName, ArrayList<Integer> vote) {
        super(context, R.layout.category_list_item, categName);

        this.context = context;
        this.categName = categName;
        this.vote = vote;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.category_list_item, null, true);

        ((TextView)rowView.findViewById(R.id.category_title)).setText(categName.get(position));
        ((TextView)rowView.findViewById(R.id.category_background)).setWidth(vote.get(position));

        return rowView;
    }
}
