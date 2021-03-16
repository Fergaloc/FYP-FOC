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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//@RequiresApi(api = Build.VERSION_CODES.O)
public class ParentViewHealth extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    FirebaseAuth mAuth;


    TextView txtUserName;
    //vars for recyclerview.
    private View bleedView;
    private RecyclerView recyclerView;
    ArrayList<Bleed> arrayList;
    Context context;

    private Query query, queryDatesD;

    Button btnChildHealthBack;


    DatabaseReference databaseBleeds,databaseRef,dataBleed,databaseName,databaseLS;
    Query databaseDates;
    List<Bleed> bleeds;


    ListView listViewTarget;

    TextView txtBleedAmount,txtTarget;

    int countBleeds = 0;

    //Sets Dates for now and for 6 months
    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    // String SixDates = (LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));



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
        setContentView(R.layout.parent_view_health);
        mAuth = FirebaseAuth.getInstance();


        txtBleedAmount = (TextView) findViewById(R.id.txtBleedAmount);
        txtTarget = (TextView) findViewById(R.id.txtTarget);
        listViewTarget = (ListView) findViewById(R.id.listViewTarget);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        btnChildHealthBack = (Button) findViewById(R.id.btnChildHealthBack);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        Date result = cal.getTime();
        String SixMonths = result.toString();

        //GETS iD
        Intent intent = getIntent();
        String id = intent.getStringExtra(ParentViewChildHome.PATIENT_ID);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateInSix = formatter.format(Date.parse(SixMonths));


        //recylcer
        arrayList = new ArrayList<>();
        context = getApplicationContext();
        recyclerView = findViewById(R.id.bleedRecycler);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        String uid = mAuth.getUid();
        queryDatesD = FirebaseDatabase.getInstance().getReference().child("bleeds").child(id).orderByChild("bleedDate").startAt(dateInSix).endAt(currentDate);

        dataBleed = FirebaseDatabase.getInstance().getReference().child("bleeds").child(id);
        databaseLS = FirebaseDatabase.getInstance().getReference().child("bleeds").child(id);

        // Toast.makeText(context,currentDate ,Toast.LENGTH_LONG).show();

//        databaseDates = dataBleed.orderByChild("bleedDates").limitToFirst(countBleeds);



        btnChildHealthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentViewHealth.this.onBackPressed();
                finish();
            }
        });


        //Gets user name
        databaseName = FirebaseDatabase.getInstance().getReference("patients").child(id).child("patientName");
        databaseName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                txtUserName.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        bleeds = new ArrayList<>();


        //Gets the patient ID and finds their bleeds
        String idS = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);
        query = databaseBleeds.child(id);




        //  A query that finds the amount of bleeds a user has had in the past 6 months

        Query queryDates = FirebaseDatabase.getInstance().getReference().child("bleeds").child(id).orderByChild("bleedDate").startAt(dateInSix).endAt(currentDate);
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
                    topBleeds.put( "Back", Back);
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

                            String names = entry.getKey();
                            txtTarget.setText(names);

                            Query TargetQ = databaseBleeds.orderByChild("bleedName").equalTo(names);
                            //Shows all bleeds for target joints
                            TargetQ.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bleeds.clear();
                                    for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                                        Bleed bleed = trackSnapshot.getValue(Bleed.class);
                                        bleeds.add(bleed);
                                    }
                                    final BleedList bleedListAdapter = new BleedList(ParentViewHealth.this, bleeds);
                                    listViewTarget.setAdapter(bleedListAdapter);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




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

        //Creates Firebase recycler that adds bleeds to horizontal view.

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Bleed>().setQuery(dataBleed, Bleed.class).build();

        FirebaseRecyclerAdapter<Bleed, ParentViewHealth.BleedViewHolder> adapter =  new FirebaseRecyclerAdapter<Bleed, ParentViewHealth.BleedViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final BleedViewHolder holder, int position, @NonNull Bleed model) {

                final String bleedID = getRef(position).getKey();

                dataBleed.child(bleedID).orderByChild("bleedDates").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String bleedID = dataSnapshot.child("bleedIDID").getValue().toString();
                        final String bleedName = dataSnapshot.child("bleedName").getValue().toString();
                        String bleedDate = dataSnapshot.child("bleedDate").getValue().toString();
                        String bleedSeverity = dataSnapshot.child("bleedSeverity").getValue().toString();

                        holder.txtSeverity.setText(bleedSeverity);
                        holder.txtName.setText(bleedName);
                        holder.txtDate.setText(bleedDate);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


            @NonNull
            @Override
            public BleedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bleedlist, parent, false);
                ParentViewHealth.BleedViewHolder viewHolder = new ParentViewHealth.BleedViewHolder(view);
                return viewHolder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class BleedViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtSeverity, txtDate ;


        public BleedViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtbleedName);
            txtSeverity = itemView.findViewById(R.id.bleedSeverity);
            txtDate  = itemView.findViewById(R.id.txtBleedDate);

        }
    }


}