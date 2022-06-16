package com.sylveon.mypixappv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.R;

import java.util.List;

public class ProfileFavoritesCardAdapter extends RecyclerView.Adapter<ProfileFavoritesCardAdapter.ViewHolder> {
    private List<Illustrations> illustrationsList;
    private Context context;

    public ProfileFavoritesCardAdapter(List<Illustrations> illustrationsList, Context context) {
        this.illustrationsList = illustrationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_illust_cardview, parent,false);

        return new ProfileFavoritesCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFavoritesCardAdapter.ViewHolder holder, int position) {
        holder.isLiked.setVisibility(View.GONE);
        Glide.with(context)
                .load(illustrationsList.get(position).getThumbLink())
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return illustrationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final ToggleButton isLiked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.illustview);
            isLiked = itemView.findViewById(R.id.toggle_isliked_basicIllust);
        }
    }
}
