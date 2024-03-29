package com.example.networkfinalasmasyam;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.networkfinalasmasyam.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding ;
    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;

    int phone ;

    Uri image ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //جزئية الادموب
       // AdRequest adRequest = new AdRequest.Builder().build();
       // binding.adView.loadAd(adRequest);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        firebaseStorage.getReference().child("images/"+currentUser.getUid())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ImageView imageView = findViewById(R.id.imageView);

                        // Download directly from StorageReference using Glide
                        Glide.with(ProfileActivity.this)
                                .load(uri)
                                .into(imageView);
                    }
                });

        fireStore.collection("Users").document(currentUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Users users = documentSnapshot.toObject(Users.class);

                        if (users != null){

                            String userName = users.getUserName();
                            String dateOfBirth = users.getDateOfBirth();
                            String fullAddress = users.getFullAddress();
                            int phone = users.getPhone();

                            binding.userName.setText(userName);
                            binding.dateOfBirth.setText(dateOfBirth);
                            binding.editTextFullAddress.setText(fullAddress);
                            binding.editTextPhone.setText(String.valueOf(phone));
                        }

                    }
                });




        ActivityResultLauncher<String> arl = registerForActivityResult(new ActivityResultContracts.GetContent(),//
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        binding.imageView.setImageURI(result);
                        image = result;
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



        /* userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(String.valueOf(binding.imageView.getImageAlpha())))
                .build();*/


        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImageForFireBase();


              /*  currentUser.updateProfile(userProfileChangeRequest)
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
                        });*/

                String userName= binding.userName.getText().toString();
                try {
                    phone = Integer.parseInt(binding.editTextPhone.getText().toString());


                }catch (NumberFormatException exception){

                }
                String dateOfBirth= binding.dateOfBirth.getText().toString();
                String fullAddress= binding.editTextFullAddress.getText().toString();

                Users users = new Users();
                users.setUserName(userName);
                users.setDateOfBirth(dateOfBirth);
                users.setFullAddress(fullAddress);
                users.setPhone(phone);

                fireStore.collection("Users").document(currentUser.getUid()).set(users);

            }
        });



       /* Uri img = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        binding.imageView.setImageURI(img);*/

    }

    private void uploadImageForFireBase() {

        if (image != null){

            UploadTask uploadTask =  firebaseStorage.getReference()
                    .child("images/"+currentUser.getUid())
                    .putFile(image);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    //أسماء في المكان هاد بقدر اخزن الرابط هاد في الفيرستور وبكون ربطت ستورج بالفير ستور وهو مطلوب.
                    return  firebaseStorage.getReference()
                            .child("images/"+currentUser.getUid()).getDownloadUrl();

                    // fireStore.collection("Users").document(currentUser.getUid()).set();


                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                startActivity(new Intent(ProfileActivity.this , FavoriteActivity.class));
                break;
            case R.id.logOut:
                finish();
                break;
            case R.id.mainActivity:
                startActivity(new Intent(ProfileActivity.this , MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}