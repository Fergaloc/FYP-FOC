package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class myHealthActivity extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    FirebaseAuth mAuth;


    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;
    Query query;


    BottomNavigationView itemSelectedListener;

    TextView txtBleedAmount,txtTarget;

    int countBleeds = 0;

    //Sets Dates for now and for 6 months
    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    String SixDates = (LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));



    //Array to hold the ints.
    List<Integer> listofBleeds = new ArrayList<Integer>();

    //Creating ints to count the amount of a certain bleed location
            int abdominal;
         int Ankle;
       int Arm;
      int Back;
        int Buttock;
        int Calf;
  int DentalProcedure;
        int Ear;
        int Elbow;
       int Eye;
      int Face;
       int Foot;
       int Gums;
     int Hand;
       int Head;
        int Hip;
    int Knee;
       int Neck;
     int Nose;
        int Shoulder;
       int Thigh;
     int Toe;
        int Tongue ;
       int Wrist;
       int Other;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_health);
        mAuth = FirebaseAuth.getInstance();


        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        txtBleedAmount = (TextView) findViewById(R.id.txtBleedAmount);
        txtTarget = (TextView) findViewById(R.id.txtTarget);




        Intent intent = getIntent();

        bleeds = new ArrayList<>();


        //Gets the patient ID and finds their bleeds
        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);
        query = databaseBleeds.child(id);


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


        //  A query that finds the amount of bleeds a user has had in the past 6 months
        final String uid = FirebaseAuth.getInstance().getUid();

        Query queryDates = FirebaseDatabase.getInstance().getReference().child("bleeds").child(uid).orderByChild("bleedDate").startAt(SixDates).endAt(currentDate);

        queryDates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countBleeds = (int) dataSnapshot.getChildrenCount();
                    txtBleedAmount.setText(Integer.toString(countBleeds ));

                } else{
                    txtBleedAmount.setText("O ");


            }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Finds the count of all bleeds for each bleed location, used to find the target joint.
        databaseBleeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapShot :dataSnapshot.getChildren()) {

                    switch (snapShot.child("bleedName").getValue(String.class)) {
                        case "Abdominal Bleed":
                            ++abdominal;
                            break;
                        case "Ankle":
                            ++Ankle;
                            break;
                        case "Arm":
                            ++Arm;
                            break;
                        case "Back":
                            ++Back;
                            break;
                        case "Buttock":
                            ++Buttock;
                            break;
                        case "Calf":
                            ++Calf;
                            break;
                        case "Dental Procedure":
                            ++DentalProcedure;
                            break;
                        case "Ear":
                            ++Ear;
                            break;
                        case "Elbow":
                            ++Elbow;
                            break;
                        case "Eye":
                            ++Eye;
                            break;
                        case "Face":
                            ++Face;
                            break;
                        case "Foot":
                            ++Foot;
                            break;
                        case "Gums":
                            ++Gums;
                            break;
                        case "Hand":
                            ++Hand;
                            break;
                        case "Head":
                            ++Head;
                            break;
                        case "Hip":
                            ++Hip;
                            break;
                        case "Knee":
                            ++Knee;
                            break;
                        case "Neck":
                            ++Neck;
                            break;
                        case "Nose":
                            ++Nose;
                            break;
                        case "Shoulder":
                            ++Shoulder;
                            break;
                        case "Thigh":
                            ++Thigh;
                            break;
                        case "Toe":
                            ++Toe;
                            break;
                        case "Tongue":
                            ++Tongue;
                            break;
                        case "Wrist":
                            ++Wrist;
                            break;
                        case "Other":
                            ++Other;
                            break;

                    }

                    /*
                    listofBleeds.add(abdominal);
                    listofBleeds.add(Ankle);
                    listofBleeds.add(Arm);
                    listofBleeds.add(Back);
                    listofBleeds.add(Buttock);
                    listofBleeds.add(Calf);
                    listofBleeds.add(DentalProcedure);
                    listofBleeds.add(Ear);
                    listofBleeds.add(Elbow);
                    listofBleeds.add(Eye);
                    listofBleeds.add(Face);
                    listofBleeds.add(Foot);
                    listofBleeds.add(Gums);
                    listofBleeds.add(Head);
                    listofBleeds.add(Hip);
                    listofBleeds.add(Knee);
                    listofBleeds.add(Neck);
                    listofBleeds.add(Nose);
                    listofBleeds.add(Shoulder);
                    listofBleeds.add(Thigh);
                    listofBleeds.add(Tongue);
                    listofBleeds.add(Toe);
                    listofBleeds.add(Wrist);
                    listofBleeds.add(Other);
*/

                    // Integer max = Collections.max(listofBleeds);
                    // txtTarget.setText(Integer.toString(max));


                    //A hash map that has all bleed locations no. of bleeds there.
                    HashMap<String , Integer> topBleeds = new HashMap<String , Integer>();

                    topBleeds.put( "Abdominal Bleed",abdominal);
                    topBleeds.put("Ankle", Ankle);
                    topBleeds.put( "Arm",Arm);
                    topBleeds.put( "Abdominal Bleed", Back);
                    topBleeds.put( "Buttock",Buttock);
                    topBleeds.put( "Calf",Calf);
                    topBleeds.put( "Dental",DentalProcedure);
                    topBleeds.put( "Ear",Ear);
                    topBleeds.put( "Elbow",Elbow);
                    topBleeds.put("Eye",Eye);
                    topBleeds.put( "Face",Face);
                    topBleeds.put( "Foot",Foot);
                    topBleeds.put( "Gums",Gums);
                    topBleeds.put( "Head",Head);
                    topBleeds.put( "Hip",Hip);
                    topBleeds.put( "Knee",Knee);
                    topBleeds.put("Neck",Neck);
                    topBleeds.put("Nose", Nose);
                    topBleeds.put( "Thigh",Thigh);
                    topBleeds.put( "Tongue",Tongue);
                    topBleeds.put( "Toe",Toe);
                    topBleeds.put( "Wrist",Wrist);
                    topBleeds.put( "Other",Other);

                    //Code to find the max value from the created hash map.
                    //https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
                    Map.Entry<String, Integer> maxEntry = null;
                    for (Map.Entry<String, Integer> entry : topBleeds.entrySet()) {
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                            maxEntry = entry;
                            String TargetJoint = maxEntry.toString();
                            txtTarget.setText(TargetJoint);

                        }
                        String maxKey = Collections.max(topBleeds.keySet());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();




    }



}
