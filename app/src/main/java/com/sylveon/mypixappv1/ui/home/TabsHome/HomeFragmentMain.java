package com.sylveon.mypixappv1.ui.home.TabsHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sylveon.mypixappv1.Adapters.RankingCardAdapter;
import com.sylveon.mypixappv1.Adapters.RecommendedCardAdapter;
import com.sylveon.mypixappv1.Adapters.TrendingTagsCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.RankingCards;
import com.sylveon.mypixappv1.Models.RecommendedCards;
import com.sylveon.mypixappv1.Models.TrendingTags;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.ui.ArtistViewActivity;
import com.sylveon.mypixappv1.ui.IllustrationViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragmentMain extends Fragment implements TrendingTagsCardAdapter.OnTagListener, RecommendedCardAdapter.OnAuthorListener{
    public SwipeRefreshLayout swipeRefreshLayout;

    public RecyclerView rankingRecycler;
    public RecyclerView trendingTagsRecycler;
    public RecyclerView recommendedRecycler;

    public static ArrayList<RankingCards> listaRankings;
    public static ArrayList<TrendingTags> listaTrendings;
    public ArrayList<RecommendedCards> listaRecommended;

    private RequestQueue queue;

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home_main, container, false);
        initializeSwipeRefresh();
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        loadRankingsView(()->{
            RankingCardAdapter adapter = new RankingCardAdapter(listaRankings, getContext());
            rankingRecycler.setAdapter(adapter);
            Log.i("INFO", "Ranks Cargados: "+listaRankings.size());
        });
        loadTrendingsView(()->{
            TrendingTagsCardAdapter adapter = new TrendingTagsCardAdapter(listaTrendings, getContext(), this);
            trendingTagsRecycler.setAdapter(adapter);
            Log.i("INFO", "Trendings Cargados: "+listaTrendings.size());
        });
        loadRecommendedView(()->{
            RecommendedCardAdapter adapter = new RecommendedCardAdapter(listaRecommended, getContext(), this);
            recommendedRecycler.setAdapter(adapter);
            Log.i("INFO", "Recommended Cargados: "+listaRecommended.size());
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void prepararRecyclers(){
        rankingRecycler = (RecyclerView) root.findViewById(R.id.rankingRecycler);
        rankingRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        trendingTagsRecycler = (RecyclerView) root.findViewById(R.id.trendingRecycler);
        trendingTagsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recommendedRecycler = (RecyclerView) root.findViewById(R.id.recommendedRecycler);
        recommendedRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }
    private void loadRankingsView(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        String url = "http://"+ MainActivity.apiUrl +"/illust/illustRanking/"+userId;
        listaRankings = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url, response -> {
            try{
                JSONObject jsonobject = new JSONObject(response);
                JSONArray rankingData = jsonobject.getJSONArray("ranking");
                for(int i = 0; i < 5; i++){
                    JSONObject imagerankinfo = rankingData.getJSONObject(i);
                    int illustId = imagerankinfo.getInt("illust_id");
                    String titleIllust = imagerankinfo.getString("title");
                    String imageUrl = imagerankinfo.getString("thumb_url");
                    String authorName = imagerankinfo.getString("author_name");
                    String authorUrl = imagerankinfo.getString("author_url");
                    listaRankings.add(new RankingCards(illustId,authorName,titleIllust,imageUrl,authorUrl));
                }
                callback.onSuccess();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> {
            Log.i("Error en ranking request", "Fallo de servidor?");
            Toast.makeText(getContext(),"Error de Servidor", Toast.LENGTH_SHORT).show();
        });

        queue.add(getRequest);
    }

    private void loadTrendingsView(final VolleyCallback callback){
        String url = "http://"+MainActivity.apiUrl+"/illust/illustTrendingTags/";
        listaTrendings = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray trendingData = jsonObject.getJSONArray("trendings");
                for(int i = 0; i < 5; i++){
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
    public void loadRecommendedView(final VolleyCallback callback){
        int userId = MainActivity.user.getUserId();
        Log.i("UserId","es: "+userId);
        String url = "http://"+MainActivity.apiUrl+"/user/illustRecommended/"+userId;
        listaRecommended = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray recommendedData = jsonObject.getJSONArray("recommended");
                for (int i = 0; i < recommendedData.length(); i++){
                    JSONObject recommendedInfo = recommendedData.getJSONObject(i);
                    JSONArray userIllustrations = recommendedInfo.getJSONArray("illusts");
                    if(listaRecommended.size() == 3) break;
                    if(userIllustrations.length() > 3){
                        JSONObject illust1 = userIllustrations.getJSONObject(0);
                        JSONObject illust2 = userIllustrations.getJSONObject(1);
                        listaRecommended.add(new RecommendedCards(
                                recommendedInfo.getString("thumb_url"),
                                recommendedInfo.getString("author_name"),
                                illust1.getString("thumb_url"),
                                illust2.getString("thumb_url"),
                                recommendedInfo.getBoolean("is_following"),
                                recommendedInfo.getInt("author_id"),
                                recommendedInfo.getInt("followers")
                        ));
                    }
                }
                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("Error en recommended request", error.getMessage());
            Toast.makeText(getContext(),"Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }

    public void initializeSwipeRefresh(){
        swipeRefreshLayout = root.findViewById(R.id.home_main_swipelayout);
        if(swipeRefreshLayout == null){
            Toast.makeText(getContext(),"nulo :c",Toast.LENGTH_SHORT).show();
        }else {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Toast.makeText(getContext(),"Refrescando home",Toast.LENGTH_SHORT).show();
                    loadRankingsView(()->{
                        RankingCardAdapter adapter = new RankingCardAdapter(listaRankings, getContext());
                        rankingRecycler.setAdapter(adapter);
                        Log.i("INFO", "Reankings Refrescados: "+listaRankings.size());
                    });
                    loadTrendingsView(()->{
                        TrendingTagsCardAdapter adapter = new TrendingTagsCardAdapter(listaTrendings, getContext(), HomeFragmentMain.this);
                        trendingTagsRecycler.setAdapter(adapter);
                        Log.i("INFO", "Trendings Refrescados: "+listaTrendings.size());
                    });
                    loadRecommendedView(()->{
                        RecommendedCardAdapter adapter = new RecommendedCardAdapter(listaRecommended, getContext(), HomeFragmentMain.this);
                        recommendedRecycler.setAdapter(adapter);
                        Log.i("INFO", "Recommended Refrescados: "+listaRecommended.size());
                    });
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }


    @Override
    public void onTagClick(int position) {
        Toast.makeText(getContext(),"Abriendo search con: "+listaTrendings.get(position).getTagName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthorclick(int position) {
        //Toast.makeText(getContext(), "abirendo author con: "+listaRecommended.get(position).getUserId(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"Abriendo illustview con: "+listaIllustrations.get(position).getIllustId(),Toast.LENGTH_SHORT).show();
        //Navigation.findNavController(root).navigate(R.id.illustrationViewFragment);
        Intent intent = new Intent(getActivity(), ArtistViewActivity.class);
        intent.putExtra("authorid", listaRecommended.get(position).getUserId());
        intent.putExtra("isfollowed", listaRecommended.get(position).getFollowing());
        intent.putExtra("followers", listaRecommended.get(position).getFollowersCount());
        intent.putExtra("authorprofile",listaRecommended.get(position).getUserProfile());
        intent.putExtra("authorname", listaRecommended.get(position).getUserName());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
}