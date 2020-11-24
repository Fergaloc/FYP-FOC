package com.example.MyBleeds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BleedList extends ArrayAdapter<Bleed> {
    private Activity context;
    private List<Bleed> bleeds;

    public BleedList(Activity context, List<Bleed> bleeds){
        super(context, R.layout.layout_track_list, bleeds);
        this.context = context;
        this.bleeds = bleeds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_track_list,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

       Bleed bleed = bleeds.get(position);

        textViewName.setText(bleed.getBleedName());
        textViewRating.setText(String.valueOf(bleed.getBleedSeverity()));

        return listViewItem;

    }


}
