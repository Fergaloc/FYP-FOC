package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

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
    Button buttonHome;

    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;


    Query query;

    EditText SearchText;

    ArrayAdapter<BleedList> bleedListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bleeds_activity);
        mAuth = FirebaseAuth.getInstance();

        listViewBleeds = (ListView) findViewById(R.id.listViewBleeds);
        buttonHome = (Button) findViewById(R.id.buttonHome);


        Intent intent = getIntent();

        bleeds = new ArrayList<>();


        //Gets the patient ID and finds their bleeds
        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);
        query = databaseBleeds.child(id);


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                startActivity(intentSettings);

            }

        });

        // code to detect changes in values
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



    }

}
