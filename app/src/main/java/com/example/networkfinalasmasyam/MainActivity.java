package com.example.networkfinalasmasyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
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

    NewsClass newsclass;

    NewsClass news ;

    NewsAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        adapter = new NewsAdapter(new ArrayList<>(), MainActivity.this , this);
        binding.recyclerAdapter.setAdapter(adapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,
                false);
        binding.recyclerAdapter.setLayoutManager(lm);


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
                                newsclass = newsClass ;

                                Log.d("policy", "onComplete: " + newsClass.getPolicy());
                            }

                            Log.d("policy", "onComplete: " + newsclass.getPolicy());

                            adapter.setData(list);

                        } else {
                            Log.d("TAG", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

    }
    @Override
    public void IsFavorite(int position, NewsClass newsClass) {

        news = newsClass ;

        addToFavorite();

        Log.d("newsClass", "IsFavorite: "+ newsClass);
      /*  if(isInMyFavorite){

            addToFavorite();
            // not in favorite , add from favorite
            isInMyFavorite = true ;
        }else{
            //  in favorite , remove to favorite

            deleteFromFavorite();
            isInMyFavorite = false ;
        }*/
    }


    public void addToFavorite(){

        fireStore.collection("Favorite").document(currentUser.getUid()).collection("MyFavorite")
                .document(news.getDocumentId()).set(news)
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
                .document(news.getDocumentId())
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search , menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s) {

        fireStore.collection("News")
                .orderBy("type").startAt(s).endAt(s+"\uf8ff")
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
                                newsclass = newsClass ;
                            }

                            adapter.setData(list);

                        } else {
                            Log.d("TAG", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

    }


    }



