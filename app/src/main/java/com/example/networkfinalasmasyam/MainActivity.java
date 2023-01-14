package com.example.networkfinalasmasyam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.networkfinalasmasyam.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;

    FirebaseUser currentUser ;

    FirebaseFirestore fireStore ;
    FirebaseStorage firebaseStorage ;
    ArrayList<NewsClass> arrayList ;

    String jj ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();



        fireStore.collection("News").document(currentUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        NewsClass newsClass = documentSnapshot.toObject(NewsClass.class);

                        arrayList = new ArrayList<>();

                        arrayList.add(newsClass);

                        NewsAdapter adapter = new NewsAdapter(arrayList , MainActivity.this);
                        binding.recyclerAdapter.setAdapter(adapter);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this , RecyclerView.VERTICAL ,
                                false);
                        binding.recyclerAdapter.setLayoutManager(lm);

                        for (int i = 0; i < arrayList.size(); i++) {

                           jj =  arrayList.get(i).getNews();
                        }
                        Log.d("size", "size: "+jj);

                    }
                });

    }
}