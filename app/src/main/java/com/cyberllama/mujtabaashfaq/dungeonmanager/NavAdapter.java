package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;


/**
 * Sets last highlighted item on MainActivity OnCreate.
 */
public class NavAdapter extends ArrayAdapter<String> {

    //Get saved values.
    private SharedPreferences savedVals = getContext().getSharedPreferences("SavedVals", 0);

    //Set last list view selected from saved values.
    private int fragId = savedVals.getInt("fragId", 0);



    /**
     * Default constructor.
     */
    public NavAdapter(Context context, int resource, String[] data) {
        super(context, resource, data);
    }


    /**
     * Creates view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        //Highlight correct view.
        if (position == fragId) {
            v.setBackgroundColor(getContext().getResources().getColor(R.color.colorShadeDark));
        } else
            v.setBackgroundColor(getContext().getResources().getColor(R.color.colorShade));

        return v;
    }
}
