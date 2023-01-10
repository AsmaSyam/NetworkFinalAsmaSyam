package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.networkfinalasmasyam.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding ;
    FirebaseAuth firebaseAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();


        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String email = binding.email.getText().toString();
               String password = binding.password.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email , password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Log.d("RegisterActivity" ,task.getResult().getUser().toString() );
                                    startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                                }else {
                                    Toast.makeText(RegisterActivity.this,
                                            task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}