package com.example.networkfinalasmasyam;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.networkfinalasmasyam.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding ;
    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;

    UserProfileChangeRequest userProfileChangeRequest ;
    int phone ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        DocumentReference docRef = fireStore.collection("Users").document("asma");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("document", "DocumentSnapshot data: " + document.getData());


                        for (int i = 0; i < document.getData().size(); i++) {

                            //document.getData().get

                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });




        ActivityResultLauncher<String> arl = registerForActivityResult(new ActivityResultContracts.GetContent(),//
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        binding.imageView.setImageURI(result);
                    }
                });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch("image/*");
            }
        });

        binding.dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();

                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                binding.dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                              /*  int year1 = Calendar.YEAR;
                                if (year <= year1){
                                    binding.dateOfBirth.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                                }else if (year > year1){
                                    Toast.makeText(ProfileActivity.this, "Can not choose it", Toast.LENGTH_SHORT).show();
                                }*/
                                // int day = Calendar.DAY_OF_MONTH;
                                // int month = Calendar.MONTH;


                            }
                        },
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection

                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

            }
        });



         userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(String.valueOf(binding.imageView.getImageAlpha())))
                .build();


        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentUser.updateProfile(userProfileChangeRequest)
                        .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    currentUser.reload();
                                    Log.d("PhotoUrl", "" + currentUser.getPhotoUrl());
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                String userName= binding.userName.getText().toString();
                try {
                    phone = Integer.parseInt(binding.editTextPhone.getText().toString());


                }catch (NumberFormatException exception){

                }
                String dateOfBirth= binding.dateOfBirth.getText().toString();
                String fullAddress= binding.editTextFullAddress.getText().toString();

                Users users = new Users(userName , phone , dateOfBirth , fullAddress);

                fireStore.collection("Users").document(users.getUserName()).set(users);



            }
        });



        Uri img = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        binding.imageView.setImageURI(img);

    }
}