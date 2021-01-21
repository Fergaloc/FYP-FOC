package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class editBleeds extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TreatmentDialog.TreatmentDialogListener  {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";



    TextView textViewArtistsName, textViewDate;
    EditText editTextBleedLocation;
    SeekBar seekBarRating;
    Button buttonAddTrack, buttonHome, buttonDatePicker,buttonAddTreatment;
    Spinner SpinnerBleedSide, SpinnerBleedSeverity, SpinnerBleedCause, SpinnerBleedLocation;

    DatabaseReference databaseBleeds;

    List<Bleed> bleeds;

    FirebaseAuth mAuth;


    DatabaseReference databaseTreatment;
    SwipeMenuListView listViewTreatment;
    Query query;
    List<Treatment> treatments;

    String bleedIntentID;
    String TreatmentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_bleed);
        mAuth = FirebaseAuth.getInstance();

        //gets button from xml
        textViewArtistsName = (TextView) findViewById(R.id.textViewArtistName);
        editTextBleedLocation = (EditText) findViewById(R.id.editTextName);
        seekBarRating = (SeekBar) findViewById((R.id.seekBarRating));
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);
        buttonHome = (Button) findViewById(R.id.buttonHome);
        SpinnerBleedSide = (Spinner) findViewById(R.id.SpinnerBleedSide);
        SpinnerBleedSeverity = (Spinner) findViewById(R.id.SpinnerBleedSeverity);
        SpinnerBleedCause = (Spinner) findViewById(R.id.SpinnerBleedCause);
        SpinnerBleedLocation = (Spinner) findViewById(R.id.SpinnerBleedLocation);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        buttonDatePicker = (Button) findViewById(R.id.buttonDatePicker);
        listViewTreatment = (SwipeMenuListView) findViewById(R.id.listViewTreatmentEditBleed);
        buttonAddTreatment = (Button) findViewById(R.id.buttonAddTreatmentEdit);



        //Gets bleed info from previous page
        Intent intent = getIntent();
        bleeds = new ArrayList<>();
        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        String name = intent.getStringExtra(PatientSettingsActivity.PATIENT_NAME);
        final String BleedID = intent.getStringExtra(ViewSingleBleedPatient.BLEED_ID);
        String Bleedname = intent.getStringExtra(ViewSingleBleedPatient.BLEED_NAME);
        String Bleedseverity = intent.getStringExtra(ViewSingleBleedPatient.BLEED_SEVERITY);
         String Bleedside = intent.getStringExtra(ViewSingleBleedPatient.BLEED_SIDE);
         String Bleedcause = intent.getStringExtra(ViewSingleBleedPatient.BLEED_CAUSE);
        String Bleeddate = intent.getStringExtra(ViewSingleBleedPatient.BLEED_DATE);




        //Code for swipe view to delete a treatment.
        //https://github.com/baoyongzhang/SwipeMenuListView
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listViewTreatment.setMenuCreator(creator);

        listViewTreatment.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Treatment item = treatments.get(position);
                switch (index) {
                    case 0:
                        // delete a treatment from the list view and firebase database

                        TreatmentID = item.getTreatmentID();

                        DatabaseReference deletetreatment;
                        deletetreatment = FirebaseDatabase.getInstance().getReference("treatment").child(BleedID).child(TreatmentID);

                        deletetreatment.removeValue();
                        treatments.remove(position);

                        TreatmentList treatmentListAdapter = new TreatmentList(editBleeds.this, treatments);
                        listViewTreatment.setAdapter(treatmentListAdapter);
                        treatmentListAdapter.notifyDataSetChanged();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });



        //Find treatment reference for bleed and querying location for our data.
        databaseTreatment = FirebaseDatabase.getInstance().getReference("treatment").child(BleedID);
        query = databaseTreatment.child(BleedID);
        treatments = new ArrayList<>();

        databaseTreatment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                treatments.clear();
                for(DataSnapshot treatmentSnapshot: dataSnapshot.getChildren()){
                    Treatment treatment = treatmentSnapshot.getValue(Treatment.class);
                    treatments.add(treatment);
                }

                TreatmentList treatmentListAdapter = new TreatmentList(editBleeds.this, treatments);
                listViewTreatment.setAdapter(treatmentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        textViewArtistsName.setText(name);

        //The following sets the spinner value to the selected bleed details.
        //Uses created spinners and intent data.
        //https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
        String bleedLocation = Bleedname;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bleedLocations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerBleedLocation.setAdapter(adapter);
        if (bleedLocation != null) {
            int spinnerPosition = adapter.getPosition(bleedLocation);
            SpinnerBleedLocation.setSelection(spinnerPosition);

        }


        String bleedSide= Bleedside;
        ArrayAdapter<CharSequence> adapterSide = ArrayAdapter.createFromResource(this, R.array.bleedSide, android.R.layout.simple_spinner_item);
        adapterSide.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerBleedSide.setAdapter(adapterSide);
        if (bleedSide != null) {
            int spinnerPosition = adapterSide.getPosition(bleedSide);
            SpinnerBleedSide.setSelection(spinnerPosition);
        }

        String bleedSeverity = Bleedseverity;
        ArrayAdapter<CharSequence> adapterSeverity = ArrayAdapter.createFromResource(this, R.array.bleedSeverity, android.R.layout.simple_spinner_item);
        adapterSeverity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerBleedSeverity.setAdapter(adapterSeverity);
        if (bleedSeverity != null) {
            int spinnerPosition = adapterSeverity.getPosition(bleedSeverity);
            SpinnerBleedSeverity.setSelection(spinnerPosition);
        }

        String bleedCause= Bleedcause;
        ArrayAdapter<CharSequence> adapterCause = ArrayAdapter.createFromResource(this, R.array.bleedCause, android.R.layout.simple_spinner_item);
        adapterSide.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerBleedCause.setAdapter(adapterCause);
        if (bleedCause != null) {
            int spinnerPosition = adapterCause.getPosition(bleedCause);
            SpinnerBleedCause.setSelection(spinnerPosition);
        }


        String BleedDate = Bleeddate;
        textViewDate.setText(BleedDate);




        //create new node in bleeds node, the bleeds id will match with the patients id
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();

                String bleedID = intent.getStringExtra(ViewSingleBleedPatient.BLEED_ID);
                String bleedLocation  = SpinnerBleedLocation.getSelectedItem().toString();
                int rating = seekBarRating.getProgress();
                String bleedSide = SpinnerBleedSide.getSelectedItem().toString();
                String bleedSeverity = SpinnerBleedSeverity.getSelectedItem().toString();
                String bleedCause = SpinnerBleedCause.getSelectedItem().toString();
                String bleedDate = textViewDate.getText().toString();

                updateBleed(bleedID,bleedLocation,rating,bleedSide,bleedSeverity,bleedCause,bleedDate);

                Toast.makeText(editBleeds.this, "Bleed Updated", Toast.LENGTH_SHORT).show();



            }
        });


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                startActivity(intentSettings);

            }

        });


        buttonAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });




//https://www.youtube.com/watch?v=AdTzD96AhE0
        // code from an online tutorial that shows a date picker once the button is picked.
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this ,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // code to detect changes in values

        databaseBleeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bleeds.clear();
                for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                    Bleed bleed = trackSnapshot.getValue(Bleed.class);
                    bleeds.add(bleed);
                }
                BleedList bleedListAdapter = new BleedList(editBleeds.this, bleeds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Code to update the current bleed.
    private boolean updateBleed(String bleedID, String bleedName, int bleedRating, String bleedSide, String bleedSeverity, String bleedCause,String bleedDate){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //gets the bleed ID so we can change the attributes.
        Intent intent = getIntent();
        String BleedID = intent.getStringExtra(ViewSingleBleedPatient.BLEED_ID);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bleeds").child(uid).child(BleedID);


        Bleed bleed = new Bleed(bleedID, bleedName,bleedRating,bleedSide,bleedSeverity,bleedCause,bleedDate);

        databaseReference.setValue(bleed);

        return true;

    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date =  dayOfMonth + "/" + (month+1) +  "/ "+ year;
        textViewDate.setText(date);
    }


    public void openDialog() {

        Intent intent = getIntent();
        String BleedID = intent.getStringExtra(ViewSingleBleedPatient.BLEED_ID);

        TreatmentDialog treatmentDialog = new TreatmentDialog();
        treatmentDialog.show(getSupportFragmentManager(), "Treatment Dialog");

        //Sending bleedID to dialog fragment via a bundle
        Bundle bundle = new Bundle();
        bundle.putString("BLEED_ID", BleedID);
        treatmentDialog.setArguments(bundle);


    }

    @Override
    public void applyData(String Reason, String Type, String Date) {

    }

}
