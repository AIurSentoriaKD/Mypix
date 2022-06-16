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
import com.sylveon.mypixappv1.Models.RankingCards;
import com.sylveon.mypixappv1.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingCardAdapter extends RecyclerView.Adapter<RankingCardAdapter.ViewHolder> {
    private List<RankingCards> rankingList;
    private Context context;

    public RankingCardAdapter(List<RankingCards> rankingList, Context context) {
        this.rankingList = rankingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_cardview, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingCardAdapter.ViewHolder holder, int position) {
        holder.illustTitle.setText(rankingList.get(position).getIllustTitle());
        holder.authorName.setText(rankingList.get(position).getAuthorname());
        Glide.with(context)
                .load(rankingList.get(position).getImageUrl())
                .centerCrop()
                .into(holder.imageView);
        Glide.with(context)
                .load(rankingList.get(position).getAuthorImageUrl())
                .centerCrop()
                .into(holder.authorImageView);
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final CircleImageView authorImageView;
        private final TextView authorName;
        private final TextView illustTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_ranking_imageview);
            authorImageView = itemView.findViewById(R.id.home_ranking_artist_profile);
            authorName = itemView.findViewById(R.id.home_ranking_artist_name);
            illustTitle = itemView.findViewById(R.id.home_ranking_title);
        }
    }
}
