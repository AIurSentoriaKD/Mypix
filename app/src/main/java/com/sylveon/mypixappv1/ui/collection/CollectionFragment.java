package com.sylveon.mypixappv1.ui.collection;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sylveon.mypixappv1.Adapters.IllustrationCardAdapter;
import com.sylveon.mypixappv1.Adapters.IllustrationViewCardAdapter;
import com.sylveon.mypixappv1.Adapters.ProfileFavoritesCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.IllustrationViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollectionFragment extends Fragment implements IllustrationCardAdapter.OnIllustListener{

    public RecyclerView likesRecycler;

    public ArrayList<Illustrations> likesList;

    private RequestQueue queue;

    View root;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getCheckedItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_collection, container, false);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        prepararRecyclers();
        loadUserLikes(()->{
            IllustrationCardAdapter adapter = new IllustrationCardAdapter(likesList, getContext(), this);
            likesRecycler.setAdapter(adapter);
            Log.i("INFO", "Likes Cargados: "+likesList.size());
        });
        return root;
    }

    private void prepararRecyclers() {
        likesRecycler = (RecyclerView) root.findViewById(R.id.fragment_collection_likesrecycler);
        likesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void loadUserLikes(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        String url = "http://"+ MainActivity.apiUrl +"/user/userBookmarksIllust/"+userId;
        likesList = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray likesData = jsonObject.getJSONArray("favorites");
                for(int i = 0; i < likesData.length(); i++){
                    JSONObject likeinfo = likesData.getJSONObject(i);
                    likesList.add(new Illustrations(
                            likeinfo.getInt("illust_id"),
                            likeinfo.getInt("i_author_id"),
                            likeinfo.getInt("views"),
                            likeinfo.getInt("favorites"),
                            likeinfo.getString("title"),
                            likeinfo.getString("thumb_url"),
                            likeinfo.getString("publish_date"),
                            likeinfo.getBoolean("is_liked")
                    ));
                }
                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i("Error en likes request", error.getMessage());
            Toast.makeText(getContext(),"Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }

    @Override
    public void onIllustClick(int position) {
        //Toast.makeText(getContext(),"Abriendo illustview con: "+listaIllustrations.get(position).getIllustId(),Toast.LENGTH_SHORT).show();
        //Navigation.findNavController(root).navigate(R.id.illustrationViewFragment);
        Intent intent = new Intent(getActivity(), IllustrationViewActivity.class);
        intent.putExtra("authorId", likesList.get(position).getAuthorId());
        intent.putExtra("illustId", likesList.get(position).getIllustId());
        intent.putExtra("isLiked", likesList.get(position).isLiked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
}