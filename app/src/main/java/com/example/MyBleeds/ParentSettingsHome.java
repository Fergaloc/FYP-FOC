package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ParentSettingsHome extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button  buttonLogOut,buttonAccount,buttonParent,btnPBack;

    FirebaseAuth mAuth;
    FirebaseStorage storage;


    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_account_menu);
        mAuth = FirebaseAuth.getInstance();


        //getting views
        buttonLogOut = (Button) findViewById(R.id.buttonLogoutParent);
        buttonParent = (Button) findViewById(R.id.buttonParentID);
        buttonAccount = (Button) findViewById(R.id.buttonAccountParent);
        btnPBack = (Button) findViewById(R.id.btnPBack);


        context = getApplicationContext();
        String uid = FirebaseAuth.getInstance().getUid();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            // No user is signed in
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(ParentSettingsHome.this, welcomepage.class));

        }


        //Logs out user and send them to the Log-in page.
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ParentSettingsHome.this, welcomepage.class));
            }
        });


        buttonParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parentIntent = new Intent(ParentSettingsHome.this, ParentViewID.class);
                startActivity(parentIntent);
            }
        });


        btnPBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentSettingsHome.this.onBackPressed();
                finish();
            }
        });


    }


}
