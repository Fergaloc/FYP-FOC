package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddParent extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextID;
    Button  buttonSave;



    DatabaseReference databasepatient,dbParent;

    FirebaseAuth mAuth;

    Context context;
    BottomNavigationView itemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addparent);
        mAuth = FirebaseAuth.getInstance();

        final String uid = FirebaseAuth.getInstance().getUid();

        databasepatient = FirebaseDatabase.getInstance().getReference("patients").child("U32N7b9ZetXeQtBx9o9YIZBI7yB2").child(uid);

        //getting views
     buttonSave = (Button) findViewById(R.id.btnSaveuid) ;
     editTextID = (EditText) findViewById(R.id.editUID);
        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        editTextID.setFocusable(false);
        context = getApplicationContext();
        String uida = FirebaseAuth.getInstance().getUid();


//Code to add Parent to Patients Account
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ParentID = editTextID.getText().toString().trim();
                dbParent = FirebaseDatabase.getInstance().getReference("parent");


               dbParent.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if (dataSnapshot.hasChild(ParentID)) {

                           databasepatient.child("parentID").setValue(ParentID);

                           Toast.makeText(context, "Parent Added ", Toast.LENGTH_SHORT).show();


                       }

                       Toast.makeText(context, "Unique Identifier not found ", Toast.LENGTH_SHORT).show();


                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

            }
        });



        //Bottom navigation switch case to decide location based upon selected item
        itemSelectedListener.setSelectedItemId(R.id.ic_account);

        itemSelectedListener.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        String patientnEW = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                        intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intentSettings);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_search:
                        String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intent = new Intent(getApplicationContext(), ViewAllBleeds.class);

                        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_account:

                        String patientnEWs = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intentSetting = new Intent(getApplicationContext(), PatientAccountMenu.class);

                        intentSetting.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                        startActivity(intentSetting);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });












    }


}

