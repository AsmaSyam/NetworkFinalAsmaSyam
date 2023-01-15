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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Listener{

    ActivityMainBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;

    boolean isInMyFavorite ;

    NewsClass newsClass ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


       // firebaseStorage.getReference().child("newsImages")
         //       .listAll()
           //     .addOnSuccessListener(new OnSuccessListener<ListResult>() {
             //       @Override
               //     public void onSuccess(ListResult listResult) {
//
  //                      NewsAdapter adapter = new NewsAdapter(MainActivity.this, listResult.getItems());
    //                    binding.recyclerAdapter.setAdapter(adapter);
      //                  RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,
          //                      false);
        //                binding.recyclerAdapter.setLayoutManager(lm);
//
  //                      Toast.makeText(MainActivity.this, "" + listResult.getItems(), Toast.LENGTH_SHORT).show();
    //                    //  Log.d("referenceList", "referenceList: " + listResult.getItems());
//
  //                      // Download directly from StorageReference using Glide
    //                   /* Glide.with(MainActivity.this)
      //                          .load(listResult.getItems())
        //                        .into(holder.newsImage);*/
//
  //                  }
    //            });

        fireStore.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            List<NewsClass> list = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewsClass newsClass = document.toObject(NewsClass.class);
                                newsClass.setDocumentId(document.getId());
                                list.add(newsClass);
                            }

                            NewsAdapter adapter = new NewsAdapter(list, MainActivity.this , MainActivity.this);
                            binding.recyclerAdapter.setAdapter(adapter);
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,
                                    false);
                            binding.recyclerAdapter.setLayoutManager(lm);
                        } else {
                            Log.d("TAG", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

    }
    @Override
    public void IsFavorite(int position, NewsClass newsClass) {

        newsClass = newsClass ;

        Log.d("newsClass", "IsFavorite: "+ newsClass);
        if(isInMyFavorite){

            addToFavorite();
            // in favorite , remove from favorite
            isInMyFavorite = true ;
        }else{
            // not in favorite , add to favorite

            deleteFromFavorite();
            isInMyFavorite = false ;
        }
    }


    public void addToFavorite(){

        fireStore.collection("Favorite").document(currentUser.getUid()).collection("MyFavorite")
                .document(newsClass.getDocumentId()).set(newsClass)
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

        fireStore.collection("Favorite").document(currentUser.getUid()).collection("MyFavorite")
                .document(newsClass.getDocumentId())
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
