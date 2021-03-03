package com.example.MyBleeds;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PatientList extends ArrayAdapter<Patient> {

    private Activity context;
    private List<Patient> patientList;

    private Uri uriConvert;
    private String imgCheck;


    public PatientList(Activity context, List<Patient> patientList){
        super(context, R.layout.list_layout, patientList);
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewSeverity = (TextView) listViewItem.findViewById(R.id.textViewSeveritys);
        ImageView patientPic = (ImageView) listViewItem.findViewById(R.id.patientPic);
        TextView textViewDOB = (TextView) listViewItem.findViewById(R.id.patientDOB);

        Patient patient = patientList.get(position);

        textViewName.setText(patient.getPatientName());
        textViewSeverity.setText(patient.getPatientSeverity());
        textViewDOB.setText(patient.getPatientDOB());

        imgCheck = patient.getImageURL();
        if(imgCheck!= null) {
            uriConvert = Uri.parse(imgCheck);
        }
        if(imgCheck == null){
            patientPic.setVisibility(View.GONE);
        }

        Glide.with(context).load(uriConvert).into(patientPic);

        return listViewItem;

    }
}
