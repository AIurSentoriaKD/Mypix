package com.sylveon.mypixappv1.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sylveon.mypixappv1.Adapters.IllustrationCardAdapter;
import com.sylveon.mypixappv1.Adapters.ProfileFavoritesCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.databinding.FragmentGalleryBinding;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.IllustrationViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment implements  IllustrationCardAdapter.OnIllustListener{

    private FragmentGalleryBinding binding;

    public RecyclerView userWorksRecycler;

    public ArrayList<Illustrations> userWorksList;

    private RequestQueue queue;

    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        loadUserWorks(()->{
            IllustrationCardAdapter adapter = new IllustrationCardAdapter(userWorksList, getContext(), this);
            userWorksRecycler.setAdapter(adapter);
            Log.i("INFO", "Userworks Cargados: "+userWorksList.size());
        });
        return root;
    }

    private void prepararRecyclers() {
        userWorksRecycler = (RecyclerView) root.findViewById(R.id.fragment_gallery_illustsrecycler);
        userWorksRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void loadUserWorks(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        String url = "http://"+ MainActivity.apiUrl +"/user/getIllustFromAuthor/"+userId;
        userWorksList = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url, response -> {
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
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getCheckedItem();
    }

    @Override
    public void onIllustClick(int position) {
        Intent intent = new Intent(getActivity(), IllustrationViewActivity.class);
        intent.putExtra("authorId", userWorksList.get(position).getAuthorId());
        intent.putExtra("illustId", userWorksList.get(position).getIllustId());
        intent.putExtra("isLiked", userWorksList.get(position).isLiked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
}