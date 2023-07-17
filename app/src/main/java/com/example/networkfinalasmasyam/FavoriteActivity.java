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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements Listener {

    ActivityFavoriteBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;

    NewsClass newsClass ;

    NewsAdapter adapter ;
    List<NewsClass> list ;

    int pos ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        fireStore.collection("Favorite").document(currentUser.getUid()).collection("MyFavorite")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                       if(task.isSuccessful()){

                           list = task.getResult().toObjects(NewsClass.class);
                            adapter = new NewsAdapter(list , FavoriteActivity.this  , FavoriteActivity.this);
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

    @Override
    public void IsFavorite(int position, NewsClass newsClass) {

        pos = position ;
        this.newsClass = newsClass ;
        deleteFromFavorite();

    }

    public void deleteFromFavorite(){

        fireStore.collection("Favorite").document(currentUser.getUid()).collection("MyFavorite")
                .document(newsClass.getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            list.remove(newsClass);
                            adapter.notifyItemRemoved(pos);
                            Toast.makeText(FavoriteActivity.this, "Remove from your favorite List...", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(FavoriteActivity.this, "Failed to remove from your favorite due to" +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}