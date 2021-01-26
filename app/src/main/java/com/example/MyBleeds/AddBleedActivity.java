package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class AddBleedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TreatmentDialog.TreatmentDialogListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";
    public static String BLEED_ID = "bleedid";

    TextView textViewArtistsName, textViewDate;
    EditText editTextBleedLocation;
    SeekBar seekBarRating;
    Button buttonAddTrack, buttonHome, buttonDatePicker, buttonAddTreatment;
    Spinner SpinnerBleedSide, SpinnerBleedSeverity, SpinnerBleedCause, SpinnerBleedLocation;
    ListView listviewTreatment;
    TextView txtAddTreat;

    DatabaseReference databaseBleeds, databaseTreatment;

    List<Bleed> bleeds;
    List<Treatment> treatments;

    FirebaseAuth mAuth;

    String bleedIntentID;

    Query query;

    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bleed);
        mAuth = FirebaseAuth.getInstance();

        //gets button from xml
        textViewArtistsName = (TextView) findViewById(R.id.textViewArtistName);
        editTextBleedLocation = (EditText) findViewById(R.id.editTextName);
        seekBarRating = (SeekBar) findViewById((R.id.seekBarRating));
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);
        SpinnerBleedSide = (Spinner) findViewById(R.id.SpinnerBleedSide);
        SpinnerBleedSeverity = (Spinner) findViewById(R.id.SpinnerBleedSeverity);
        SpinnerBleedCause = (Spinner) findViewById(R.id.SpinnerBleedCause);
        SpinnerBleedLocation = (Spinner) findViewById(R.id.SpinnerBleedLocation);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        buttonDatePicker = (Button) findViewById(R.id.buttonDatePicker);
        buttonAddTreatment = (Button) findViewById(R.id.buttonAddTreatment);
        txtAddTreat = (TextView) findViewById(R.id.textAddTreatment) ;
        listviewTreatment = (ListView) findViewById(R.id.listViewTreatment);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);



        buttonAddTreatment.setVisibility(View.INVISIBLE);
        txtAddTreat.setVisibility(View.INVISIBLE);
        listviewTreatment.setVisibility(View.INVISIBLE);







        Intent intent = getIntent();

        bleeds = new ArrayList<>();

        final String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        String name = intent.getStringExtra(PatientSettingsActivity.PATIENT_NAME);

        textViewArtistsName.setText(name);

        //create new node in bleeds node, the bleeds id will match with the patients id
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);



        //Opening dialog to add treatment data.
        buttonAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });


        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBleed();
            }
        });



        //Bottom navigation switch case to decide location based upon selected item
        bottomNavigationView.setSelectedItemId(R.id.ic_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:

                        String patient3 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intentHome = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                        intentHome.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intentHome);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_search:
                        String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intent = new Intent(getApplicationContext(), ViewBleeds.class);

                        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_account:

                        String patientnEW = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intentSettings = new Intent(getApplicationContext(), PatientSettingsActivity.class);

                        intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                        startActivity(intentSettings);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
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
                BleedList bleedListAdapter = new BleedList(AddBleedActivity.this, bleeds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Code taken from https://www.youtube.com/watch?v=EM2x33g4syY&list=PLk7v1Z2rk4hj6SDHf_YybDeVhUT9MXaj1
    //saves bleed to Firebase.
    private void saveBleed(){


        String bleedLocation  = SpinnerBleedLocation.getSelectedItem().toString();
        int rating = seekBarRating.getProgress();
        String bleedSide = SpinnerBleedSide.getSelectedItem().toString();
        String bleedSeverity = SpinnerBleedSeverity.getSelectedItem().toString();
        String bleedCause = SpinnerBleedCause.getSelectedItem().toString();
        String bleedDate = textViewDate.getText().toString();

        if(!TextUtils.isEmpty(bleedLocation) && !TextUtils.isEmpty(bleedDate)){
            String id = databaseBleeds.push().getKey();

            Bleed bleed = new Bleed(id,bleedLocation , rating, bleedSide, bleedSeverity, bleedCause,bleedDate );
            databaseBleeds.child(id).setValue(bleed);

            bleedIntentID = bleed.getBleedIDID();


            txtAddTreat.setVisibility(View.VISIBLE);
            buttonAddTreatment.setVisibility(View.VISIBLE);
            listviewTreatment.setVisibility(View.VISIBLE);

            Toast.makeText(this,"Bleed Saved Successfully", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(this,"Bleed Date should not be empty", Toast.LENGTH_LONG).show();
        }
        }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date =  dayOfMonth + "/" + (month+1) +  "/ "+ year;
        textViewDate.setText(date);
    }

    public void openDialog() {

        TreatmentDialog treatmentDialog = new TreatmentDialog();
        treatmentDialog.show(getSupportFragmentManager(), "Treatment Dialog");

        //Sending bleedID to dialog fragment via a bundle
        Bundle bundle = new Bundle();
        bundle.putString("BLEED_ID", bleedIntentID);
        treatmentDialog.setArguments(bundle);


        //Find treatment reference for bleed and querying location for our data.
        databaseTreatment = FirebaseDatabase.getInstance().getReference("treatment").child(bleedIntentID);
        query = databaseTreatment.child(bleedIntentID);
        treatments = new ArrayList<>();

        databaseTreatment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                treatments.clear();
                for(DataSnapshot treatmentSnapshot: dataSnapshot.getChildren()){
                    Treatment treatment = treatmentSnapshot.getValue(Treatment.class);
                    treatments.add(treatment);
                }

                TreatmentList treatmentListAdapter = new TreatmentList(AddBleedActivity.this, treatments);
                listviewTreatment.setAdapter(treatmentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void applyData(String Reason,String Type, String Date){


    }



}
