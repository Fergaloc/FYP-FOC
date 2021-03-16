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

public class ViewPatientBleeds extends AppCompatActivity implements DoctorFilterDialog.DoctorFilterDialogListener {

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
    Button buttonHome,btnSix,btnFilter,btnReturnParentFilter;

    DatabaseReference databaseBleeds,databaseLS;
    List<Bleed> bleeds;
    List<Bleed> bleededs;

    TextView txtLoc,txtSev,txtCause,txtChildID;



    Query query;

    EditText SearchText;

    ArrayAdapter<BleedList> bleedListAdapter;

    BottomNavigationView itemSelectedListener;

    Spinner spbleedLocation;

    String patientID;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_viewfilter);
        mAuth = FirebaseAuth.getInstance();


        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        listViewLS = (ListView) findViewById(R.id.listviewLS);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        txtChildID = (TextView) findViewById(R.id.txtChildID);
        btnReturnParentFilter = (Button) findViewById(R.id.btnReturnParentFilter);

        txtCause = (TextView) findViewById(R.id.txtCauseFilter);
        txtLoc = (TextView) findViewById(R.id.txtFilterLocation);
        txtSev = (TextView) findViewById(R.id.txtFilterSevere);



        String uid = FirebaseAuth.getInstance().getUid();


        Intent Filterintent = getIntent();
        final String filterLocation = Filterintent.getStringExtra("BLEED_LOCATION");
        final String filterSeverity = Filterintent.getStringExtra("BLEED_SEVERITY");
        final String filterCause = Filterintent.getStringExtra("BLEED_CAUSE");
        final String newsID = Filterintent.getStringExtra("PATIENT_ID");


        btnReturnParentFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPatientBleeds.this.onBackPressed();
                finish();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra(ViewPatientHome.PATIENT_ID);
        txtChildID.setVisibility(View.GONE);

        bleeds = new ArrayList<>();
        bleededs = new ArrayList<>();

        //code to decide which ID is active and which one to use.

        if(TextUtils.isEmpty(newsID)){
            databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);
            databaseLS = FirebaseDatabase.getInstance().getReference().child("bleeds").child(id);
            query = databaseBleeds.child(id);
            txtChildID.setText(id);
            patientID = id;
        }else{

            databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(newsID);
            databaseLS = FirebaseDatabase.getInstance().getReference().child("bleeds").child(newsID);
            query = databaseBleeds.child(newsID);
            txtChildID.setText(newsID);
            patientID = newsID;


        }


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




        //if all items are passed through from Filtet.
        Query all = databaseLS.orderByChild("bleedName").equalTo(filterLocation);
        all.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bleededs.clear();

                for (DataSnapshot bleedSnapshot : dataSnapshot.getChildren()) {
                    Bleed bleeded = bleedSnapshot.getValue(Bleed.class);

                    if (bleeded.getBleedCause().equals(filterCause) && bleeded.getBleedSeverity().equals(filterSeverity)) {
                        bleededs.add(bleeded);
                        final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
                        listViewLS.setAdapter(bleedListAdapter);
                    } else {


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //if no Cause is passed through
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
                            final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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


        //if no location is passed through
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
                            final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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
                            final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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
                        final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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
                        final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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
                        final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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
                        final BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleededs);
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


                Intent viewIntent = new Intent(ViewPatientBleeds.this, ViewSingleBleed.class);
                viewIntent.putExtra(BLEED_ID, bleed.getBleedIDID());
                viewIntent.putExtra(BLEED_NAME, bleed.getBleedName());
                viewIntent.putExtra(BLEED_SEVERITY, bleed.getBleedSeverity());
                viewIntent.putExtra(BLEED_SIDE, bleed.getBleedSide());
                viewIntent.putExtra(BLEED_DATE, bleed.getBleedDate());
                viewIntent.putExtra(BLEED_CAUSE, bleed.getBleedCause());
                viewIntent.putExtra(PATIENT_ID, patientID);


                startActivity(viewIntent);

            }
        });


    }
    public void openFilterDialog(){

        DoctorFilterDialog DoctorFilterDialog = new DoctorFilterDialog();
        DoctorFilterDialog.show(getSupportFragmentManager(), "Filter Dialog");

        Bundle bundle = new Bundle();
        bundle.putString(PATIENT_ID, patientID);
        DoctorFilterDialog.setArguments(bundle);


    }


    @Override
    public void applyData(String Location, String Severity, String Cause) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
