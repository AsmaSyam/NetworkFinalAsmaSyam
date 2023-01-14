package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.networkfinalasmasyam.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding ;
    FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = binding.Email.getText().toString();
                String password = binding.Password.getText().toString();

                if (!(email.isEmpty() && password.isEmpty())){

                    firebaseAuth.signInWithEmailAndPassword(email , password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        Log.d("LoginActivity" ,task.getResult().getUser().toString() );
                                        startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Error while Login"
                                                + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });


        binding.Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext() , RegisterActivity.class));
            }
        });
    }
}