package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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

public class ParentViewID extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextID;
    Button  buttonCopy,btnReturnID;

    TextView txtIdentifier;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_viewmy_id);
        mAuth = FirebaseAuth.getInstance();

        final String uid = FirebaseAuth.getInstance().getUid();
        txtIdentifier = (TextView) findViewById(R.id.txtIdentifier);
        buttonCopy = (Button) findViewById(R.id.btnCopy);
        btnReturnID = (Button) findViewById(R.id.btnReturnID);


        txtIdentifier.setText(uid);

       // https://stackoverflow.com/questions/12780085/button-to-copy-the-value-of-a-string-to-the-clipboard
       //Copies ID to clipboard
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ID" ,txtIdentifier.getText().toString().trim());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ParentViewID.this, "Saved ID to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });


        btnReturnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentViewID.this.onBackPressed();
                finish();
            }
        });







    }


}

