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
import android.widget.AdapterView;
import android.widget.Button;
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


//Dialog that opens the filter dialog for the list view of bleeds.

public class filterDialog extends AppCompatDialogFragment {

    private Spinner spinnerLocation;
    private Spinner spinnerSeverity;
    private Spinner spinnerCause;
    private FilterDialogListener filterDialogListener;

    private Button btnReset,btnApply;

    private TextView txtLocation,txtCause,txtSeverity;

    DatabaseReference databaseBleed;

    FirebaseAuth mAuth;



    //Code for design and main functionality to add treatment to bleed.
    //  https://www.youtube.com/watch?v=ARezg1D9Zd0 - main code taken from tutorial.

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);

        builder.setView(view);

        spinnerLocation = view.findViewById(R.id.spLocations);
        spinnerSeverity = view.findViewById(R.id.spBleedSeverity);
        spinnerCause = view.findViewById(R.id.spinnerCause);
        btnReset = view.findViewById(R.id.btnReset);
        btnApply = view.findViewById(R.id.btnApply);

        txtLocation = view.findViewById(R.id.txtLocations);
        txtCause = view.findViewById(R.id.txtCause);
        txtSeverity = view.findViewById(R.id.txtSeverity);



//resets textfields and spinners
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtLocation.setText("");
                txtCause.setText("");
                txtSeverity.setText("");

                spinnerLocation.setSelection(0);
                spinnerCause.setSelection(0);
                spinnerSeverity.setSelection(0);
            }
        });

        //Applys filter
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFilterData();
            }
        });



        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtLocation.setText(spinnerLocation.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCause.setText(spinnerCause.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSeverity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtSeverity.setText(spinnerSeverity.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return builder.create();


    }


    //
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            filterDialogListener = (FilterDialogListener) context;
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + "Must implement FilterDialogListener");
        }
    }

    public interface FilterDialogListener {

        void applyData(String Location, String Severity, String Cause);

    }

    private void saveFilterData(){

        //saves the filter data to the activity.
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseBleed = FirebaseDatabase.getInstance().getReference().child("bleeds").child(uid);

        String bleedLocation = txtLocation.getText().toString();
        String bleedCause = txtCause.getText().toString();
        String bleedSeverity = txtSeverity.getText().toString();

        Intent Filterintent = new Intent(getActivity(), ViewAllBleeds.class);

       Filterintent.putExtra("BLEED_LOCATION", bleedLocation);
        Filterintent.putExtra("BLEED_CAUSE", bleedCause);
        Filterintent.putExtra("BLEED_SEVERITY", bleedSeverity);
      startActivity(Filterintent);

    }

}

