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

public class ViewAllBleeds extends AppCompatActivity implements filterDialog.FilterDialogListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    FirebaseAuth mAuth;

    ListView listViewBleeds,listViewLS;
    Button buttonHome,btnSix,btnFilter;

    DatabaseReference databaseBleeds,databaseLS;
    List<Bleed> bleeds;
    List<Bleed> bleededs;

    TextView txtLoc,txtSev,txtCause;



    Query query;

    EditText SearchText;

    ArrayAdapter<BleedList> bleedListAdapter;

    BottomNavigationView itemSelectedListener;

    Spinner spbleedLocation;


    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_filer_bleeds);
        mAuth = FirebaseAuth.getInstance();


        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        listViewLS = (ListView) findViewById(R.id.listviewLS);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        txtCause = (TextView) findViewById(R.id.txtCauseFilter);
        txtLoc = (TextView) findViewById(R.id.txtFilterLocation);
        txtSev = (TextView) findViewById(R.id.txtFilterSevere);

        //Makes sure page starts at the top.

        Intent intent = getIntent();

        bleeds = new ArrayList<>();
        bleededs = new ArrayList<>();


        //Gets the patient ID and finds their bleeds
        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        String uid = FirebaseAuth.getInstance().getUid();
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(uid);
        databaseLS = FirebaseDatabase.getInstance().getReference().child("bleeds").child(uid);
        query = databaseBleeds.child(uid);



        Intent Filterintent = getIntent();
        final String filterLocation = Filterintent.getStringExtra("BLEED_LOCATION");
        final String filterSeverity = Filterintent.getStringExtra("BLEED_SEVERITY");
        final String filterCause = Filterintent.getStringExtra("BLEED_CAUSE");


        if(TextUtils.isEmpty(filterSeverity)){

            txtSev.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(filterLocation)){

            txtLoc.setVisibility(View.GONE);

        }

        if(TextUtils.isEmpty(filterCause)){

            txtCause.setVisibility(View.GONE);

        }

        txtSev.setText(filterSeverity);
        txtLoc.setText(filterLocation);
        txtCause.setText(filterCause);


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

        //if all items are passed thro.
        Query all = databaseLS.orderByChild("bleedName").equalTo(filterLocation);
        all.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bleededs.clear();

                for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                    Bleed bleeded = bleedSnapshot.getValue(Bleed.class);

                    if (bleeded.getBleedCause().equals(filterCause) && bleeded.getBleedSeverity().equals(filterSeverity)) {
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);
                    } else {


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        //if no Cause is passed.
        if(TextUtils.isEmpty(filterCause)) {

            Query location = databaseLS.orderByChild("bleedName").equalTo(filterLocation);
            location.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);


                        if (bleeded.getBleedSeverity().equals(filterSeverity)) {

                            bleededs.add(bleeded);
                            final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                            listViewLS.setAdapter(bleedListAdapter);
                        } else {


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        //if no location is selected we filter without it
        if (TextUtils.isEmpty(filterLocation)){

            //Sample code to show Severe Bleeds from the Calf Location.
            Query Cause = databaseLS.orderByChild("bleedCause").equalTo(filterCause);
            Cause.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);


                        if (bleeded.getBleedSeverity().equals(filterSeverity)) {
                            bleededs.add(bleeded);
                            final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                            listViewLS.setAdapter(bleedListAdapter);
                        } else {


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        //if no Severity is selected we filter without it
        if (TextUtils.isEmpty(filterSeverity)){

            Query Severity = databaseLS.orderByChild("bleedName").equalTo(filterLocation);
            Severity.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);

                        if (bleeded.getBleedCause().equals(filterCause)) {
                            bleededs.add(bleeded);
                            final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                            listViewLS.setAdapter(bleedListAdapter);
                        } else {


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



        if(TextUtils.isEmpty(filterCause) && (TextUtils.isEmpty(filterSeverity))) {
            //if only Location is passed
            Query onlyLoc = databaseLS.orderByChild("bleedName").equalTo(filterLocation);
            onlyLoc.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if(TextUtils.isEmpty(filterCause) && (TextUtils.isEmpty(filterLocation))) {
            //If only severity is selected
            Query onlySeverity = databaseLS.orderByChild("bleedSeverity").equalTo(filterSeverity);
            onlySeverity.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        if(TextUtils.isEmpty(filterSeverity) && (TextUtils.isEmpty(filterLocation))){
            //if only cause is picked
            Query onlyCause = databaseLS.orderByChild("bleedCause").equalTo(filterCause);
            onlyCause.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


        //if all filters are empty, we display all bleeds.

        if(TextUtils.isEmpty(filterSeverity) && (TextUtils.isEmpty(filterLocation)) && (TextUtils.isEmpty(filterCause))){
            //if only cause is picked

           databaseBleeds.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bleededs.clear();

                    for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                        Bleed bleeded = bleedSnapshot.getValue(Bleed.class);
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewAllBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }




        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        listViewLS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bleed bleed = (Bleed) parent.getAdapter().getItem(position);


                Intent viewIntent = new Intent(ViewAllBleeds.this, ViewSingleBleedPatient.class);
                viewIntent.putExtra(BLEED_ID, bleed.getBleedIDID());
                viewIntent.putExtra(BLEED_NAME, bleed.getBleedName());
                viewIntent.putExtra(BLEED_SEVERITY, bleed.getBleedSeverity());
                viewIntent.putExtra(BLEED_SIDE, bleed.getBleedSide());
                viewIntent.putExtra(BLEED_DATE, bleed.getBleedDate());
                viewIntent.putExtra(BLEED_CAUSE, bleed.getBleedCause());


                startActivity(viewIntent);

            }
        });


    }
    public void openFilterDialog(){

        filterDialog filterDialog = new filterDialog();
        filterDialog.show(getSupportFragmentManager(), "Filter Dialog");

    }


    @Override
    public void applyData(String Location, String Severity, String Cause) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
