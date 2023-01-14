package com.example.networkfinalasmasyam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.networkfinalasmasyam.databinding.ItemNewsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<StorageReference> referenceList ;
    List<NewsClass> list ;
    Context context ;



    public NewsAdapter(List<NewsClass> list  , Context context) {
        this.list = list;
        this.context = context ;
    }
    public NewsAdapter(Context context , List<StorageReference> referenceList) {
        this.context = context ;
        this.referenceList = referenceList ;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false);

        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

       // holder.newsText.setText(list.get(position).getNews());

         StorageReference reference = referenceList.get(position);

         Glide.with(context)
                 .load(reference)
                 .into(holder.newsImage);

        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return referenceList.size();
    }

    public int getItemCounts() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView newsImage ;
        TextView newsText ;
        ImageView favoriteImage ;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());

            newsImage = binding.imageNews ;
            newsText = binding.textNews ;
            favoriteImage = binding.favorite ;
        }
    }
}
