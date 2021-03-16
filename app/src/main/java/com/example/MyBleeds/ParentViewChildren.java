package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//@RequiresApi(api = Build.VERSION_CODES.O)
public class ParentViewChildren extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    RecyclerView childRecylcer;
    Button buttonSettings;
    FirebaseAuth mAuth;


    List<Patient> patients;

    DatabaseReference databaseChild;
    Query queryChild;
    private ArrayList<String> keys = new ArrayList<>();


    ListView listViewChild;

    TextView txtBleedAmount,txtTarget;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_view_child);
        mAuth = FirebaseAuth.getInstance();

        patients = new ArrayList<>();

        final List<String> keys = new ArrayList<>();

       final String uid = FirebaseAuth.getInstance().getUid();


        listViewChild = (ListView) findViewById(R.id.listViewChildren);
        buttonSettings = (Button) findViewById(R.id.buttonViewSettingsParent);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            // No user is signed in
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(ParentViewChildren.this, welcomepage.class));

        }

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Code to ensure its a parent that logs in.
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("parent");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(id)) {
                    Toast.makeText(ParentViewChildren.this, "Logged in", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(ParentViewChildren.this, LogInActivity.class));
                    Toast.makeText(ParentViewChildren.this, "You have been Logged Out, please Login via the Patient Portal", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {


            }

        });


        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Settings = new Intent(ParentViewChildren.this, ParentSettingsHome.class);
                startActivity(Settings);
            }
        });




        databaseChild = FirebaseDatabase.getInstance().getReference().child("patients");

        queryChild = databaseChild.orderByChild("parentID").equalTo(uid);

//loads data into list view
        queryChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients.clear();
                for(DataSnapshot patientSnapshot: dataSnapshot.getChildren()){
                    Patient patient = patientSnapshot.getValue(Patient.class);
                    patients.add(patient);

                   final String goodID = String.valueOf(patientSnapshot.getRef());
                   keys.add(patientSnapshot.getKey());


                }

                ChildList childListAdapter = new ChildList(ParentViewChildren.this, patients);
                listViewChild.setAdapter(childListAdapter);


                listViewChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final  String patientID =  queryChild.getRef().getKey();
                        Patient patient = (Patient) parent.getAdapter().getItem(position);

                        //This key is the patients unique ID.
                        String myKey = keys.get(position);


                        Intent viewChild = new Intent(ParentViewChildren.this, ParentViewChildHome.class);
                        viewChild.putExtra(PATIENT_ID, myKey);
                        viewChild.putExtra(PATIENT_NAME, patient.getPatientName());
                        startActivity(viewChild);


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();



}}
