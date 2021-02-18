package com.example.MyBleeds;


import android.content.Intent;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class welcomepage  extends AppCompatActivity {



    ImageView imgPatient;
    ImageView imgParent;
    ImageView imgDoctor;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);

        mAuth = FirebaseAuth.getInstance();

        imgDoctor = (ImageView) findViewById(R.id.imgDoctor);
        imgParent = (ImageView) findViewById(R.id.imgParent);
        imgPatient = (ImageView) findViewById(R.id.imgPatient);




        imgPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent patientIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(patientIntent);
            }
        });

        imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doctorIntent = new Intent(getApplicationContext(), LoginDoctorActivity.class);
                startActivity(doctorIntent);
            }
        });


    }
}
