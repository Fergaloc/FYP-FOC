package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewBleeds extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    FirebaseAuth mAuth;

    ListView listViewBleeds;
    Button buttonHome,btnSix;

    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;


    Query query;

    EditText SearchText;

    ArrayAdapter<BleedList> bleedListAdapter;

    BottomNavigationView itemSelectedListener;

    Spinner spbleedLocation;


    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bleeds_activity);
        mAuth = FirebaseAuth.getInstance();

        listViewBleeds = (ListView) findViewById(R.id.listViewBleeds);
        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
       spbleedLocation = (Spinner) findViewById(R.id.SpinnerBleedLocationView);
        //Makes sure page starts at the top.
        listViewBleeds.setFocusable(false);

        Intent intent = getIntent();

        bleeds = new ArrayList<>();


        //Gets the patient ID and finds their bleeds
        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);
        query = databaseBleeds.child(id);




        // code to detect changes in values
        /*
        databaseBleeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bleeds.clear();
                for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                    Bleed bleed = trackSnapshot.getValue(Bleed.class);
                    bleeds.add(bleed);
                }

                BleedList bleedListAdapter = new BleedList(ViewBleeds.this, bleeds);
                listViewBleeds.setAdapter(bleedListAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        //Bottom navigation switch case to decide location based upon selected item
        itemSelectedListener.setSelectedItemId(R.id.ic_search);

        itemSelectedListener.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intent = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_search:
                        break;

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












    }

    @Override
    protected void onStart() {
        super.onStart();


        listViewBleeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bleed bleed = (Bleed) parent.getAdapter().getItem(position);


                Intent viewIntent = new Intent(ViewBleeds.this, ViewSingleBleedPatient.class);
                viewIntent.putExtra(BLEED_ID, bleed.getBleedIDID());
                viewIntent.putExtra(BLEED_NAME, bleed.getBleedName());
                viewIntent.putExtra(BLEED_SEVERITY, bleed.getBleedSeverity());
                viewIntent.putExtra(BLEED_SIDE, bleed.getBleedSide());
                viewIntent.putExtra(BLEED_DATE, bleed.getBleedDate());
                viewIntent.putExtra(BLEED_CAUSE, bleed.getBleedCause());


                startActivity(viewIntent);

            }
        });



        //Query the data on what data is selected from the spinner and change the list view to show this data.
        spbleedLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerSelector = spbleedLocation.getSelectedItem().toString();
                Query queryLocation = databaseBleeds.orderByChild("bleedName").equalTo(spinnerSelector);

                queryLocation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bleeds.clear();
                        for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                            Bleed bleed = trackSnapshot.getValue(Bleed.class);
                            bleeds.add(bleed);
                        }
                        final BleedList bleedListAdapter = new BleedList(ViewBleeds.this, bleeds);
                        listViewBleeds.setAdapter(bleedListAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}
