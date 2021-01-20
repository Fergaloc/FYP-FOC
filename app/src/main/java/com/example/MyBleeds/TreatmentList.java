package com.example.MyBleeds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TreatmentList extends ArrayAdapter<Treatment> {
    private Activity context;
    private List<Treatment> treatments;



    public TreatmentList(Activity context, List<Treatment> treatments){
        super(context, R.layout.layout_track_list, treatments);
        this.context = context;
        this.treatments = treatments;
    }

    @NonNull
    @Override
    //code for listview that displays bleeds
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_track_list,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

        Treatment bleed = treatments.get(position);

        textViewName.setText(bleed.getTreatmentReason());
        textViewRating.setText(String.valueOf(bleed.getTreatmentDate()));

        return listViewItem;


    }




}
