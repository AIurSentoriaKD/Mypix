package com.sylveon.mypixappv1.ui.trendingtags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.sylveon.mypixappv1.Adapters.TrendingTagsInFragmentAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.TrendingTags;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrendingTagsFragment extends Fragment implements TrendingTagsInFragmentAdapter.OnTagListener {
    public ArrayList<TrendingTags> listaTrendings;
    public RecyclerView recyclerTrendings;
    View root;
    private RequestQueue queue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_trending_tags, container, false);
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        listaTrendings = HomeFragmentMain.listaTrendings;
        if(HomeFragmentMain.listaTrendings != null){
            loadTrendingsView(()->{
                TrendingTagsInFragmentAdapter adapter = new TrendingTagsInFragmentAdapter(listaTrendings, getContext(),this);
                recyclerTrendings.setAdapter(adapter);
            });
        }
        return root;
    }

    private void prepararRecyclers() {
        recyclerTrendings = (RecyclerView) root.findViewById(R.id.fragment_trendings_trendingsrecycler);
        recyclerTrendings.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getCheckedItem();
    }
    private void loadTrendingsView(final VolleyCallback callback){
        String url = "http://"+MainActivity.apiUrl+"/illust/illustTrendingTags/";
        StringRequest getRequest = new StringRequest(Request.Method.GET,url, response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray trendingData = jsonObject.getJSONArray("trendings");
                for(int i = 5; i < trendingData.length(); i++){
                    JSONObject trendinginfo = trendingData.getJSONObject(i);
                    String tagname = trendinginfo.getString("tag_name");
                    String tagtrad = trendinginfo.getString("tag_trad");
                    String imageUrl = trendinginfo.getString("img");

                    listaTrendings.add(new TrendingTags(tagname,tagtrad,imageUrl));
                }
                callback.onSuccess();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> {
            Log.i("Error en trending request", error.getMessage());
            Toast.makeText(getContext(),"Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }
    @Override
    public void onTagClick(int position) {
        Toast.makeText(getContext(),"Abriendo search con: "+listaTrendings.get(position).getTagName(), Toast.LENGTH_SHORT).show();
    }
}