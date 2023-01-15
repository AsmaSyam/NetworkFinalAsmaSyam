package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.networkfinalasmasyam.databinding.ActivityFavoriteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    ActivityFavoriteBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        fireStore.collection("Favorite").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            NewsClass newsClass = task.getResult().toObject(NewsClass.class);
                            List<NewsClass> list = new ArrayList<>() ;
                            list.add(newsClass);
                            NewsAdapter adapter = new NewsAdapter(list , FavoriteActivity.this);
                            binding.recyclerAdapter.setAdapter(adapter);
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(FavoriteActivity.this , RecyclerView.VERTICAL ,
                                    false);
                            binding.recyclerAdapter.setLayoutManager(lm);

                        }else {
                            Toast.makeText(FavoriteActivity.this, "Failed get date from fireStore" +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}