package com.sylveon.mypixappv1.ui.home.TabsHome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sylveon.mypixappv1.Adapters.IllustrationCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.IllustrationViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragmentNewest extends Fragment implements IllustrationCardAdapter.OnIllustListener {

    public SwipeRefreshLayout swipeRefreshLayout;

    public RecyclerView latestRecycler;

    public ArrayList<Illustrations> listaIllustrations;

    private RequestQueue queue;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home_newest, container, false);
        initializeSwipeRefresh();
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        loadNewestView(() -> {
            IllustrationCardAdapter adapter = new IllustrationCardAdapter(listaIllustrations, getContext(), this);
            latestRecycler.setAdapter(adapter);
            Log.i("INFO", "Newest Cargados: " + listaIllustrations.size());
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void prepararRecyclers() {
        latestRecycler = (RecyclerView) root.findViewById(R.id.latest_newestRecycler);
        latestRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //latestRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void loadNewestView(final VolleyCallback callback) {
        int userId = MainActivity.user.getUserId();
        String url = "http://" + MainActivity.apiUrl + "/user/latestPublish/" + userId;
        listaIllustrations = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray newestData = jsonObject.getJSONArray("newest");
                for (int i = 0; i < newestData.length(); i++) {
                    JSONObject newestInfo = newestData.getJSONObject(i);
                    listaIllustrations.add(new Illustrations(
                            newestInfo.getInt("illust_id"),
                            newestInfo.getInt("i_author_id"),
                            newestInfo.getInt("views"),
                            newestInfo.getInt("favorites"),
                            newestInfo.getString("title"),
                            newestInfo.getString("thumb_url"),
                            newestInfo.getString("publish_date"),
                            newestInfo.getBoolean("is_liked")
                    ));
                }
                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //Log.i("Error en newest request", "Json error?");
            Toast.makeText(getContext(), "Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }

    private void initializeSwipeRefresh() {
        swipeRefreshLayout = root.findViewById(R.id.home_newest_refreshLayout);
        if (swipeRefreshLayout == null) {
            Toast.makeText(getContext(), "nulo :c", Toast.LENGTH_SHORT).show();
        } else {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadNewestView(() -> {
                        IllustrationCardAdapter adapter = new IllustrationCardAdapter(listaIllustrations, getContext(), HomeFragmentNewest.this);
                        latestRecycler.setAdapter(adapter);
                        Log.i("INFO", "Newest Cargados: " + listaIllustrations.size());
                    });
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
    public void updateFavStatus(int position){

    }
    @Override
    public void onIllustClick(int position) {
        //Toast.makeText(getContext(),"Abriendo illustview con: "+listaIllustrations.get(position).getIllustId(),Toast.LENGTH_SHORT).show();
        //Navigation.findNavController(root).navigate(R.id.illustrationViewFragment);
        Intent intent = new Intent(getActivity(), IllustrationViewActivity.class);
        intent.putExtra("authorId", listaIllustrations.get(position).getAuthorId());
        intent.putExtra("illustId", listaIllustrations.get(position).getIllustId());
        intent.putExtra("isLiked", listaIllustrations.get(position).isLiked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }

}