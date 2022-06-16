package com.sylveon.mypixappv1.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentNewest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class IllustrationCardAdapter extends RecyclerView.Adapter<IllustrationCardAdapter.ViewHolder> {
    private List<Illustrations> illustrationsList;
    private OnIllustListener monIllustListener;
    private Context context;

    public IllustrationCardAdapter(List<Illustrations> illustrationsList, Context context, OnIllustListener onIllustListener ) {
        this.illustrationsList = illustrationsList;
        this.context = context;
        this.monIllustListener = onIllustListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_illust_cardview, parent,false);

        return new ViewHolder(view, monIllustListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IllustrationCardAdapter.ViewHolder holder, int position) {
        holder.isLiked.setChecked(illustrationsList.get(position).isLiked());
        Glide.with(context)
                .load(illustrationsList.get(position).getThumbLink())
                .centerCrop()
                .into(holder.imageView);
        holder.illustId = illustrationsList.get(position).getIllustId();
        Boolean like = illustrationsList.get(position).isLiked();
        holder.isLikedBool = like.toString();
    }

    @Override
    public int getItemCount() {
        return illustrationsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageView;
        private final ToggleButton isLiked;
        OnIllustListener onIllustListener;
        private int illustId;
        private String isLikedBool;
        public ViewHolder(@NonNull View itemView, OnIllustListener onIllustListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.illustview);
            isLiked = itemView.findViewById(R.id.toggle_isliked_basicIllust);
            this.onIllustListener = onIllustListener;
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.toggle_isliked_basicIllust).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int userid = MainActivity.user.getUserId();
                    Toast.makeText(view.getContext(),"Liked: "+illustId+" En: "+userid,Toast.LENGTH_SHORT).show();
                    handleLikeClick(isLikedBool, userid, illustId, ()->{

                    });
                }
            });
        }

        @Override
        public void onClick(View view) {
            onIllustListener.onIllustClick(getAdapterPosition());
        }
        public void handleLikeClick(String isLikedBool, int userid, int illustId, final VolleyCallback callback){
            String action = "deleteBookmark";
            if(isLikedBool == "false")
                action = "addBookmark";
            String url = "http://" + MainActivity.apiUrl + "/illust/"+action+"/"+illustId+"/"+userid;
            StringRequest getRequest = new StringRequest(Request.Method.GET, url, response -> {
                callback.onSuccess();
            }, error -> {
                Log.i("error", "error al like");
            });
            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            queue.add(getRequest);
        }
    }

    public interface OnIllustListener{
        void onIllustClick(int position);
    }
}
