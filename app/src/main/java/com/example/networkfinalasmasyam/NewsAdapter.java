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


import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<NewsClass> list ;
    Context context ;

    Listener listener ;




    public NewsAdapter(List<NewsClass> list  , Context context ,  Listener listener) {
        this.list = list;
        this.context = context ;
        this.listener = listener ;
    }


   public void setData(List<NewsClass> list){
        this.list = list ;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false);

        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        int pos = position ;

         String image = list.get(pos).getImage();
         Glide.with(context)
                 .load(image)
                 .into(holder.newsImage);

         String policy = list.get(pos).getPolicy();
         holder.newsText.setText(policy);

         String documentId = list.get(pos).getDocumentId();

         holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listener.IsFavorite(pos , list.get(pos));
             }
         });

    }

    @Override
    public int getItemCount() {
        if(list == null)
            return  0 ;
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
