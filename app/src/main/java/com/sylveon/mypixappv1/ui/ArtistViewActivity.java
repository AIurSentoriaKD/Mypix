package com.sylveon.mypixappv1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sylveon.mypixappv1.Adapters.IllustrationCardAdapter;
import com.sylveon.mypixappv1.Adapters.RecommendedCardAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.Illustrations;
import com.sylveon.mypixappv1.Models.RecommendedCards;
import com.sylveon.mypixappv1.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ArtistViewActivity extends AppCompatActivity implements RecommendedCardAdapter.OnAuthorListener, IllustrationCardAdapter.OnIllustListener{
    private int authorId, followers;
    private Boolean isFollowed;
    private String authorProfile, authorName;

    public CircleImageView authorPic;
    public ImageView authorBack;
    public TextView authorNametxt, authorFollowerstxt;
    public ToggleButton isfollowedStatus;

    public RecyclerView similarRecycler;
    public RecyclerView artworksRecycler;

    public ArrayList<RecommendedCards> listaRecommended;
    public ArrayList<Illustrations> listaArtworks;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_view);

        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getSupportActionBar().hide();

        queue = Volley.newRequestQueue(getApplicationContext());

        authorPic = (CircleImageView) findViewById(R.id.artistview_profileimage);
        authorBack = (ImageView) findViewById(R.id.artistview_blurbackground);
        authorNametxt = (TextView) findViewById(R.id.artistview_profilename);
        authorFollowerstxt = (TextView) findViewById(R.id.artistview_followercount);
        isfollowedStatus = (ToggleButton) findViewById(R.id.artistview_followtoggle);

        Bundle bundle = getIntent().getExtras();

        authorId = bundle.getInt("authorid");
        followers = bundle.getInt("followers");
        isFollowed = bundle.getBoolean("isfollowed");
        authorProfile = bundle.getString("authorprofile");
        authorName = bundle.getString("authorname");
        if (authorProfile != null) {
            loadImagesAndInitialData();
        }else{
            reObtainAuthorData(()->{
                loadImagesAndInitialData();
            });
        }
        prepararRecyclers();
        loadSimilarArtists(()->{
            RecommendedCardAdapter adapter = new RecommendedCardAdapter(listaRecommended, this, this);
            similarRecycler.setAdapter(adapter);
            Log.i("INFO", "Relacionados Cargados: "+listaRecommended.size());
        });
        loadUserArtWorks(()->{
            IllustrationCardAdapter adapter = new IllustrationCardAdapter(listaArtworks, this, this);
            artworksRecycler.setAdapter(adapter);
            Log.i("INFO", "Artworks Cargados: "+listaArtworks.size());
        });
    }

    private void prepararRecyclers() {
        similarRecycler = (RecyclerView) findViewById(R.id.artistview_similarartistrecycler);
        artworksRecycler = (RecyclerView) findViewById(R.id.artistview_illustrecycler);
        similarRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        artworksRecycler.setLayoutManager(new GridLayoutManager(this, 2));

    }
    private void loadSimilarArtists(final VolleyCallback callback){
        String url = "http://"+MainActivity.apiUrl+"/user/illustRecommended/"+authorId;
        listaRecommended = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray recommendedData = jsonObject.getJSONArray("recommended");
                for (int i = 0; i < recommendedData.length(); i++){
                    JSONObject recommendedInfo = recommendedData.getJSONObject(i);
                    JSONArray userIllustrations = recommendedInfo.getJSONArray("illusts");
                    if(listaRecommended.size() == 3) break;
                    if(userIllustrations.length() > 1){
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
            Toast.makeText(this,"Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }
    private void loadUserArtWorks(final VolleyCallback callback){
        String url = "http://"+MainActivity.apiUrl+"/user/getIllustFromAuthor/"+authorId;
        listaArtworks = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray illusts = jsonObject.getJSONArray("illusts");
                for(int i = 0; i < illusts.length(); i++){
                    JSONObject illustinfo = illusts.getJSONObject(i);
                    listaArtworks.add(new Illustrations(
                            illustinfo.getInt("illust_id"),
                            illustinfo.getInt("i_author_id"),
                            illustinfo.getInt("views"),
                            illustinfo.getInt("favorites"),
                            illustinfo.getString("title"),
                            illustinfo.getString("thumb_url"),
                            illustinfo.getString("publish_date"),
                            illustinfo.getBoolean("is_liked")
                    ));
                }
                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }
    private void loadImagesAndInitialData(){
        isfollowedStatus.setChecked(isFollowed);
        authorNametxt.setText(authorName);
        authorFollowerstxt.setText(String.valueOf(followers));
        Glide.with(this)
                .load(authorProfile)
                .into(authorPic);
        Glide.with(this)
                .load(authorProfile)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(authorBack);
    }
    private void reObtainAuthorData(final VolleyCallback callback) {
        int userid = MainActivity.user.getUserId();
        String url = "http://" + MainActivity.apiUrl + "/user/authorInfo/"+authorId+"/"+userid;
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                followers = jsonObject.getInt("followers");
                isFollowed = jsonObject.getBoolean("is_following");
                authorProfile = jsonObject.getString("thumb_url");
                authorName = jsonObject.getString("author_name");
                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i("Error", "Al Obtener info de author");
            Toast.makeText(this, "Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }
    public void exitAristView(View view){
        finish();
    }
    public void handleFollowClick(View view){
        String action = "followUser";
        if(isFollowed){
            // descmarcar
            action = "unfollowUser";
        }
        followOrUnfollow(action, ()->{
            Log.i("Action", "follow or unfollow completado");
        });
    }
    public void followOrUnfollow(String action, final VolleyCallback callback){
        int userid = MainActivity.user.getUserId();
        String url = "http://" + MainActivity.apiUrl + "/user/"+action+"/"+authorId+"/"+userid;
        StringRequest getRequest = new StringRequest(Request.Method.GET,url,response -> {
            callback.onSuccess();
        },error -> {
            Log.i("Error", "En accion del boton seguir");
            Toast.makeText(this, "Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }

    @Override
    public void onAuthorclick(int position) {
        //Toast.makeText(getContext(), "abirendo author con: "+listaRecommended.get(position).getUserId(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"Abriendo illustview con: "+listaIllustrations.get(position).getIllustId(),Toast.LENGTH_SHORT).show();
        //Navigation.findNavController(root).navigate(R.id.illustrationViewFragment);
        Intent intent = new Intent(this, ArtistViewActivity.class);
        intent.putExtra("authorid", listaRecommended.get(position).getUserId());
        intent.putExtra("isfollowed", listaRecommended.get(position).getFollowing());
        intent.putExtra("followers", listaRecommended.get(position).getFollowersCount());
        intent.putExtra("authorprofile",listaRecommended.get(position).getUserProfile());
        intent.putExtra("authorname", listaRecommended.get(position).getUserName());
        startActivity(intent);
        this.overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }

    @Override
    public void onIllustClick(int position) {
        Intent intent = new Intent(this, IllustrationViewActivity.class);
        intent.putExtra("authorId", listaArtworks.get(position).getAuthorId());
        intent.putExtra("illustId", listaArtworks.get(position).getIllustId());
        intent.putExtra("isLiked", listaArtworks.get(position).isLiked());
        startActivity(intent);
        this.overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
}