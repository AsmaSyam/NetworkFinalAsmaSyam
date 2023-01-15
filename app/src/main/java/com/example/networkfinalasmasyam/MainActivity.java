package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.networkfinalasmasyam.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Listener{

    ActivityMainBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;

    boolean isInMyFavorite ;
    String Policy ;

    String jj ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();



        firebaseStorage.getReference().child("newsImages/")
                .listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        NewsAdapter adapter = new NewsAdapter(MainActivity.this , listResult.getItems());
                        binding.recyclerAdapter.setAdapter(adapter);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this , RecyclerView.VERTICAL ,
                                false);
                        binding.recyclerAdapter.setLayoutManager(lm);

                        Toast.makeText(MainActivity.this, ""+ listResult.getItems(), Toast.LENGTH_SHORT).show();
                      //  Log.d("referenceList", "referenceList: " + listResult.getItems());

                        // Download directly from StorageReference using Glide
                       /* Glide.with(MainActivity.this)
                                .load(listResult.getItems())
                                .into(holder.newsImage);*/

                    }
                });

           fireStore.collection("News").document(currentUser.getUid())
                   .get()
                  .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                          if(task.isSuccessful()){
                              NewsClass newsClass = task.getResult().toObject(NewsClass.class);
                              List<NewsClass> list = new ArrayList<>() ;
                              list.add(newsClass);
                              NewsAdapter adapter = new NewsAdapter(list , MainActivity.this);
                              binding.recyclerAdapter.setAdapter(adapter);
                              RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this , RecyclerView.VERTICAL ,
                                      false);
                              binding.recyclerAdapter.setLayoutManager(lm);
                          }else {
                              Log.d("TAG", "onComplete: " + task.getException().getMessage());
                          }
                      }
                  });
    }


    @Override
    public void IsFavorite(int position, String policy) {

        Policy = policy;

        if(isInMyFavorite){

            deleteFromFavorite();
            // in favorite , remove from favorite
            isInMyFavorite = true ;
        }else{
            // not in favorite , add to favorite

            addToFavorite();
            isInMyFavorite = false ;
        }
    }


    public void addToFavorite(){

        NewsClass newsClass = new NewsClass();
        newsClass.setPolicy(Policy);
        fireStore.collection("Favorite").document(currentUser.getUid()).set(newsClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Added to favorite" , Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Failed to add to favorite " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void deleteFromFavorite(){

        fireStore.collection("Favorite").document(currentUser.getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Remove from your favorite List...", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Failed to remove from your favorite due to" +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }




}
