package com.example.MyBleeds;

import android.content.Intent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginDoctorActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmailDoctor);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordDoctor);


        findViewById(R.id.textViewSignupDoctor).setOnClickListener(this);
        findViewById(R.id.buttonLoginDoctor).setOnClickListener(this);

    }

    //code to check all data is correct in EditTexts
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

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

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }


//Log in function, with code to send user to main activity page after completion, Function provided by Android
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(LoginDoctorActivity.this, DoctorHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Brings user to home page if already logged in , MUST CHANGE FOR TESTING - FOC(23NOV20)
        /*if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, View_Patients.class));
        }

*/
    }
    //switch case to decide what action is taken next.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignupDoctor:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.buttonLoginDoctor:
                userLogin();
                break;
        }
    }
}
