package com.sylveon.mypixappv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sylveon.mypixappv1.Models.IllustrationImages;
import com.sylveon.mypixappv1.R;

import java.util.List;

public class IllustrationViewCardAdapter extends RecyclerView.Adapter<IllustrationViewCardAdapter.ViewHolder> {
    private List<IllustrationImages> illustrationImagesList;
    private Context context;

    public IllustrationViewCardAdapter(List<IllustrationImages> illustrationImagesList, Context context) {
        this.illustrationImagesList = illustrationImagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.illustview_imagecard, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IllustrationViewCardAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(illustrationImagesList.get(position).getIllustPageUrl())
                .centerInside()
                .placeholder(R.drawable.holder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return illustrationImagesList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.illustview_illustpage);
        }
    }
}
