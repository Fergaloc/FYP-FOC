package com.example.MyBleeds;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextHospital, editTextDoctorName;
    Button buttonSignUp,buttonSave;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextDoctorName = (EditText) findViewById(R.id.editTextDoctorName);
        editTextHospital = (EditText) findViewById(R.id.editTextHospital);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSave = (Button) findViewById(R.id.buttonSave);


        //This calls the Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextDoctorName.getText().toString().trim();
                String hospital = editTextHospital.getText().toString().trim();

                addDoctor(name, hospital);
            }
        });

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        //makes sure email is right format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        //Password must be < than 6 for firebase
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }


        //Android provided function to create user with Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //finish();
                    //startActivity(new Intent(AddDoctorActivity.this, PatientSettingsActivity.class));
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
/*
    //switch case to decide what action is taken next.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                String name = editTextDoctorName.getText().toString().trim();
                String hospital = editTextHospital.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    editTextDoctorName.setError("Name Required");
                    return;
                }

                registerUser();
                addDoctor(name, hospital);

                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
    }
*/


    private boolean addDoctor(String name, String hospital){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor").child(uid);

        Doctor doctor = new Doctor(name , hospital);

        //overide with new patient

        databaseReference.setValue(doctor);

        return true;

    }

}





