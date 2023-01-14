package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.networkfinalasmasyam.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;
    List<NewsClass> list ;
    String jj ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        firebaseStorage.getReference().child("newsImages/"+currentUser.getUid())
                .listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        NewsAdapter adapter = new NewsAdapter(MainActivity.this , listResult.getItems());
                        binding.recyclerAdapter.setAdapter(adapter);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this , RecyclerView.VERTICAL ,
                                false);
                        binding.recyclerAdapter.setLayoutManager(lm);

                        // Download directly from StorageReference using Glide
                       /* Glide.with(MainActivity.this)
                                .load(listResult.getItems())
                                .into(holder.newsImage);*/

                    }
                });

           fireStore.collection("News")
                .orderBy("policy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            list = task.getResult().toObjects(NewsClass.class);
                        }

                       /* for (int i = 0; i < list.size(); i++) {

                            jj =  list.get(i).getNews();
                        }
                        Log.d("size", "size: "+jj);*/


                        NewsAdapter adapter = new NewsAdapter(list , MainActivity.this);
                        binding.recyclerAdapter.setAdapter(adapter);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this , RecyclerView.VERTICAL ,
                                false);
                        binding.recyclerAdapter.setLayoutManager(lm);

                    }
                });

    }
}