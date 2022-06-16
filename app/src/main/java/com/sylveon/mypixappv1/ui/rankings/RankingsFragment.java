package com.sylveon.mypixappv1.ui.rankings;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sylveon.mypixappv1.Adapters.IllustrationCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.Models.RankingCards;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.databinding.FragmentRankingsBinding;
import com.sylveon.mypixappv1.ui.IllustrationViewActivity;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingsFragment extends Fragment implements  IllustrationCardAdapter.OnIllustListener{

    public ArrayList<Illustrations> listaRankings;

    public RecyclerView rankingRecycler;

    private FragmentRankingsBinding binding;

    private RequestQueue queue;

    View root;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RankingsViewModel rankingViewModel =
                new ViewModelProvider(this).get(RankingsViewModel.class);
        binding = FragmentRankingsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        prepararRecyclers();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        loadRankingsFragment(()-> {
            IllustrationCardAdapter adapter = new IllustrationCardAdapter(listaRankings, getContext(), this);
            rankingRecycler.setAdapter(adapter);
        });

        return root;
    }

    private void prepararRecyclers() {
        rankingRecycler = (RecyclerView) root.findViewById(R.id.fragment_ranking_rankingrecycler);
        rankingRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
    private void loadRankingsFragment(final VolleyCallback callback) {
        int userId = MainActivity.user.getUserId();
        String url = "http://" + MainActivity.apiUrl + "/illust/illustRanking/"+userId;
        listaRankings = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray newestData = jsonObject.getJSONArray("ranking");
                for (int i = 0; i < newestData.length(); i++) {
                    JSONObject newestInfo = newestData.getJSONObject(i);
                    listaRankings.add(new Illustrations(
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
            Log.i("Error en ranking fragment request", "no rankings");
            Toast.makeText(getContext(), "Error de Servidor", Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(getContext(),"Abriendo illustview con: "+listaIllustrations.get(position).getIllustId(),Toast.LENGTH_SHORT).show();
        //Navigation.findNavController(root).navigate(R.id.illustrationViewFragment);
        Intent intent = new Intent(getActivity(), IllustrationViewActivity.class);
        intent.putExtra("authorId", listaRankings.get(position).getAuthorId());
        intent.putExtra("illustId", listaRankings.get(position).getIllustId());
        intent.putExtra("isLiked", listaRankings.get(position).isLiked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
}