package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Build;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    TextView txtBleedAmount;

    int countBleeds = 0;

    //Sets Dates for now and for 6 months
    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    String SixDates = (LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_health);
        mAuth = FirebaseAuth.getInstance();


        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        txtBleedAmount = (TextView) findViewById(R.id.txtBleedAmount);


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





    }

    @Override
    protected void onStart() {
        super.onStart();




    }

}
