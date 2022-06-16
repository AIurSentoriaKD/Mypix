package com.sylveon.mypixappv1.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.sylveon.mypixappv1.Adapters.ProfileFavoritesCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public RecyclerView likesRecycler;
    public RecyclerView userWorksRecycler;

    public ArrayList<Illustrations> likesList;
    public ArrayList<Illustrations> userWorksList;

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

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        fillUserInfo();
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        loadUserLikes(()->{
            ProfileFavoritesCardAdapter adapter = new ProfileFavoritesCardAdapter(likesList, getContext());
            likesRecycler.setAdapter(adapter);
            Log.i("INFO", "Likes Cargados: "+likesList.size());
        });
        loadUserWorks(()->{
            ProfileFavoritesCardAdapter adapter = new ProfileFavoritesCardAdapter(userWorksList, getContext());
            userWorksRecycler.setAdapter(adapter);
            Log.i("INFO", "Userworks Cargados: "+userWorksList.size());
        });
        return root;
    }
    private void prepararRecyclers() {
        likesRecycler = (RecyclerView) root.findViewById(R.id.profile_user_favorites_recycler);
        likesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        userWorksRecycler = (RecyclerView) root.findViewById(R.id.profile_user_publications);
        userWorksRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
    }
    private void loadUserLikes(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        String url = "http://"+ MainActivity.apiUrl +"/user/userBookmarksIllust/"+userId;
        likesList = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
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
    private void loadUserWorks(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        String url = "http://"+ MainActivity.apiUrl +"/user/getIllustFromAuthor/"+userId;
        userWorksList = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray likesData = jsonObject.getJSONArray("illusts");
                for(int i = 0; i < likesData.length(); i++){
                    JSONObject likeinfo = likesData.getJSONObject(i);
                    userWorksList.add(new Illustrations(
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
            Log.i("Error en userworks request", error.getMessage());
            Toast.makeText(getContext(),"Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }
    public void fillUserInfo(){
        TextView profileUsername = root.findViewById(R.id.profile_user_name);
        CircleImageView profileImage = root.findViewById(R.id.profile_user_image);
        TextView profileDescription = root.findViewById(R.id.profile_user_description);
        profileUsername.setText(MainActivity.user.getUserName());
        Glide.with(getContext())
                .load(MainActivity.user.getUserProfileUrl())
                .centerCrop()
                .into(profileImage);
        profileDescription.setText("Hola, soy "+MainActivity.user.getUserName()+". ¡Bienvenido a mi perfil!");
    }
}