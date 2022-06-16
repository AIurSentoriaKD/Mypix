package com.sylveon.mypixappv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sylveon.mypixappv1.Models.RecommendedCards;
import com.sylveon.mypixappv1.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendedCardAdapter extends RecyclerView.Adapter<RecommendedCardAdapter.ViewHolder> {
    private List<RecommendedCards> recommendedCardsList;
    private Context context;
    private OnAuthorListener monAuthorclick;
    public RecommendedCardAdapter(List<RecommendedCards> recommendedCardsList, Context context, OnAuthorListener onAuthorclick) {
        this.recommendedCardsList = recommendedCardsList;
        this.context = context;
        this.monAuthorclick = onAuthorclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recartist_cardview,parent,false);
        return new ViewHolder(view, monAuthorclick);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedCardAdapter.ViewHolder holder, int position) {
        holder.authorName.setText(recommendedCardsList.get(position).getUserName());
        Glide.with(context)
                .load(recommendedCardsList.get(position).getUserProfile())
                .centerCrop()
                .into(holder.authorProfile);
        Glide.with(context)
                .load(recommendedCardsList.get(position).getUserIlOne())
                .centerCrop()
                .into(holder.illust1);
        Glide.with(context)
                .load(recommendedCardsList.get(position).getUserIlTwo())
                .centerCrop()
                .into(holder.illust2);
    }

    @Override
    public int getItemCount() {
        return recommendedCardsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final CircleImageView authorProfile;
        private final ImageView illust1;
        private final ImageView illust2;
        private final TextView authorName;
        OnAuthorListener onAuthorListener;
        public ViewHolder(@NonNull View itemView, OnAuthorListener onAuthorListener) {
            super(itemView);
            authorProfile = itemView.findViewById(R.id.home_recommended_artist_profile);
            illust1 = itemView.findViewById(R.id.home_recommended_artist_picture_one);
            illust2 = itemView.findViewById(R.id.home_recommended_artist_picture_two);
            authorName = itemView.findViewById(R.id.home_recommended_artist_name);
            this.onAuthorListener = onAuthorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onAuthorListener.onAuthorclick(getAdapterPosition());
        }
    }
    public interface OnAuthorListener{
        void onAuthorclick(int position);
    }
}
