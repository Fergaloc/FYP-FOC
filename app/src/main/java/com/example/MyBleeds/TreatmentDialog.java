package com.example.MyBleeds;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TreatmentDialog extends AppCompatDialogFragment {

    private Spinner spinnerReason;
    private Spinner spinnerType;
    private TreatmentDialogListener treatmentDialogListener;
    TextView datePicker;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    DatabaseReference databaseTreatment;

    FirebaseAuth mAuth;


     //Code for design and main functionality to add treatment to bleed.
    //  https://www.youtube.com/watch?v=ARezg1D9Zd0 - main code taken from tutorial.

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.treatment_dialog, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTreatment();


                    }


                });

        Bundle bundle = getArguments();
        String BleedID = bundle.getString("TEXT", "");




        spinnerReason = view.findViewById(R.id.treatment_reason);
        spinnerType = view.findViewById(R.id.treatmentType);
        datePicker = view.findViewById(R.id.datePicker);

        //Displays date picker and sets date to todays date.
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);


                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //Sets date to text view.

                String date = dayOfMonth + "/" + (month + 1) + "/ " + year;
                datePicker.setText(date);


            }

        };


        return builder.create();

    }


    //
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            treatmentDialogListener = (TreatmentDialogListener) context;
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + "Must implement TreatmentDialogListener");
        }
    }

    public interface TreatmentDialogListener {

        void applyData(String Reason, String Type, String Date);

    }


    private void saveTreatment() {

        //Saving the treatment with BleedID as parent ID
        Bundle bundle = getArguments();
        String BleedID = bundle.getString("BLEED_ID","");

        databaseTreatment = FirebaseDatabase.getInstance().getReference("treatment").child(BleedID);

        String treatmentReason = spinnerReason.getSelectedItem().toString();
        String treatmentType = spinnerType.getSelectedItem().toString();
        String treatmentDate = datePicker.getText().toString();

        if (!TextUtils.isEmpty(treatmentDate)) {
            String id = databaseTreatment.push().getKey();

            Treatment treatment = new Treatment(id, treatmentReason, treatmentType, treatmentDate);
            databaseTreatment.child(id).setValue(treatment);

            Toast.makeText(getContext(), "Treatment saved successfully", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "Treatment Date should not be empty", Toast.LENGTH_LONG).show();
        }
    }

}

