package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParentHome extends AppCompatActivity {

    public static final String PARENT_NAME = "PARENT_NAME";

    Button buttonViewChild;
    Button buttonLogOut,buttonSettings;

    List<Patient> patients;

    DatabaseReference databasePatients;

    FirebaseAuth mAuth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_home);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String parentName = intent.getStringExtra(ParentSignUp.PARENT_NAME);
        String uid = FirebaseAuth.getInstance().getUid();

        databasePatients = FirebaseDatabase.getInstance().getReference("parents");
        buttonViewChild = (Button) findViewById(R.id.buttonViewChild);
        buttonSettings = (Button) findViewById(R.id.buttonViewSettingsParent);



        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Code to ensure its a parent that logs in.
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("parent");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(id)) {
                    Toast.makeText(ParentHome.this, "Logged in", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(ParentHome.this, LogInActivity.class));
                    Toast.makeText(ParentHome.this, "You have been Logged Out, please Login via the Patient Portal", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {


            }

        });


        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Settings = new Intent(ParentHome.this, ParentSettingsHome.class);
                startActivity(Settings);
            }
        });


        buttonViewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Child = new Intent(ParentHome.this , ParentViewChildren.class);
                startActivity(Child);
            }
        });


    }



}
