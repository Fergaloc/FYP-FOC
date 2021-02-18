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

public class ParentNewDetails extends AppCompatActivity {

    public static final String PARENT_NAME = "PARENT_NAME";

    Button buttonAdvance;

    EditText editTextName;

    List<Patient> patients;

    DatabaseReference databasePatients;

    FirebaseAuth mAuth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_new);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String uid = FirebaseAuth.getInstance().getUid();


        databasePatients = FirebaseDatabase.getInstance().getReference("parents");

        buttonAdvance = (Button) findViewById(R.id.btnAdvance);
        editTextName = (EditText) findViewById(R.id.editTextNames);

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        buttonAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ParentName  = editTextName.getText().toString().trim();

                CreateParent(ParentName, id);
            }
        });

    }

    //code to add parent to Firebase

    private boolean CreateParent(String parentName, String parentID){

        String name = editTextName.getText().toString().trim();
        if(name.isEmpty()){
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return false ;
        }


        FirebaseAuth mAuth;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parent").child(uid);

        Parent parent = new Parent (parentName, parentID);

        databaseReference.setValue(parent);

        startActivity(new Intent(ParentNewDetails.this, ParentHome.class));

        return true;

    }

}
