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
import com.sylveon.mypixappv1.Models.TrendingTags;
import com.sylveon.mypixappv1.R;

import java.util.List;

public class TrendingTagsInFragmentAdapter extends RecyclerView.Adapter<TrendingTagsInFragmentAdapter.ViewHolder> {
    private List<TrendingTags> trendingTagsList;
    private OnTagListener monTagListener;
    private Context context;

    public TrendingTagsInFragmentAdapter(List<TrendingTags> trendingTagsList, Context context, OnTagListener onTagListener) {
        this.trendingTagsList = trendingTagsList;
        this.context = context;
        this.monTagListener = onTagListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trendingsfragment_trendingstagscard, parent, false);
        return new ViewHolder(view, monTagListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingTagsInFragmentAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(trendingTagsList.get(position).getTagImage())
                .centerCrop()
                .into(holder.imageView);
        holder.tagName.setText(trendingTagsList.get(position).getTagName());
        String tagtrad = trendingTagsList.get(position).getTagTranslate();
       /* if(tagtrad.equals(" "))*/
            holder.tagTrad.setText(tagtrad);
       /* else
            holder.tagTrad.setVisibility(View.GONE);*/
    }

    @Override
    public int getItemCount() {
        return trendingTagsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView tagName, tagTrad;
        OnTagListener onTagListener;

        public ViewHolder(@NonNull View itemView, OnTagListener onTagListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trendingfragment_tagimage);
            tagName = itemView.findViewById(R.id.trendingfragment_tagname);
            tagTrad = itemView.findViewById(R.id.trendingfragment_tagtrad);
            this.onTagListener = onTagListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTagListener.onTagClick(getAdapterPosition());
        }
    }

    public interface OnTagListener {
        void onTagClick(int position);
    }
}
