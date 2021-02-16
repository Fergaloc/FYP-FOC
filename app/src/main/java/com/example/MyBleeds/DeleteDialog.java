package com.example.MyBleeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteDialog extends AppCompatDialogFragment {


    private DeleteDialogListener deleteDialogListener;

    DatabaseReference databaseBleed,databaseTreatments,databaseImages;

  StorageReference dbStorage,imageRef;

    public static final String PATIENT_ID = "patientid";

    FirebaseAuth mAuth;

    String uid;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteBleed();

                        uid = FirebaseAuth.getInstance().getUid();

                        Intent intent = new Intent(getContext(), Patient_HomeActivity.class);
                        intent.putExtra(PATIENT_ID, uid);
                       startActivity(intent);
                    }
                });


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_dialog, null);




        builder.setView(view);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //makes sure the dialog is implemented in the activity.
        try {
            deleteDialogListener = (DeleteDialog.DeleteDialogListener) context;
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + "Must implement Dialog");
        }
    }




    public interface DeleteDialogListener {

        void applyData();

    }



    private void DeleteBleed(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle = getArguments();
        String BleedID = bundle.getString("BLEED_ID","");

        //Gets reference to bleed data and deletes it.
        databaseImages = FirebaseDatabase.getInstance().getReference().child("bleedImages").child(BleedID).child(BleedID);
        databaseBleed = FirebaseDatabase.getInstance().getReference().child("bleeds").child(uid).child(BleedID);
        databaseTreatments = FirebaseDatabase.getInstance().getReference().child("treatment").child(BleedID);
        dbStorage = FirebaseStorage.getInstance().getReference();

        imageRef = dbStorage.child("bleedImages/" + BleedID);

        imageRef.delete();
        databaseTreatments.removeValue();
        databaseBleed.removeValue();
        databaseImages.removeValue();



    }
}
