package com.sylveon.mypixappv1.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sylveon.mypixappv1.Models.TagsCards;
import com.sylveon.mypixappv1.R;

import java.util.List;

public class TagsCardsAdapter extends RecyclerView.Adapter<TagsCardsAdapter.ViewHolder> {
    private List<TagsCards> tagsCardsList;
    private Context context;
    private OnTagListener monTagListener;

    public TagsCardsAdapter(List<TagsCards> tagsCardsList, Context context, OnTagListener onTagListener) {
        this.tagsCardsList = tagsCardsList;
        this.context = context;
        this.monTagListener = onTagListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.illustview_tagcard, parent,false);
        return new ViewHolder(view, monTagListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsCardsAdapter.ViewHolder holder, int position) {
        holder.tagName.setText("#"+tagsCardsList.get(position).getTagName());
        holder.tagName.setPaintFlags(holder.tagName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tagTrad.setText(tagsCardsList.get(position).getTagTrad());
        if(tagsCardsList.get(position).getTagTrad().equals("")){
            holder.tagTrad.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tagsCardsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tagName;
        private final TextView tagTrad;
        OnTagListener onTagListener;
        public ViewHolder(@NonNull View itemView, OnTagListener onTagListener) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagview_tagname);
            tagTrad = itemView.findViewById(R.id.tagview_tagtrad);
            this.onTagListener = onTagListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTagListener.onTagClick(getAdapterPosition());
        }
    }
    public interface OnTagListener{
        void onTagClick(int position);
    }
}
