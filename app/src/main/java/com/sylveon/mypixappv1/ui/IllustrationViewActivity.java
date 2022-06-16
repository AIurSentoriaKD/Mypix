package com.sylveon.mypixappv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.sylveon.mypixappv1.Adapters.IllustrationViewCardAdapter;
import com.sylveon.mypixappv1.Adapters.TagsCardsAdapter;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.Models.IllustrationImages;
import com.sylveon.mypixappv1.Models.TagsCards;
import com.sylveon.mypixappv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class IllustrationViewActivity extends AppCompatActivity implements TagsCardsAdapter.OnTagListener {
    public RecyclerView pagesRecycler, commentsRecycler, relatedsRecycler, tagsRecycler;
    public CircleImageView authorImage;
    public TextView illustTitle, authorName, illustDescription;
    public ToggleButton isLikedToggle;

    public ArrayList<IllustrationImages> listaImages;
    public ArrayList<TagsCards> listaTags;

    private RequestQueue queue;

    private int authorId, illustId;
    private Boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illustration_view);
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        authorId = bundle.getInt("authorId");
        illustId = bundle.getInt("illustId");
        isLiked = bundle.getBoolean("isLiked");
        authorImage = (CircleImageView) findViewById(R.id.illustview_illustpage_userprofile);
        illustTitle = (TextView) findViewById(R.id.illustview_illusttitle);
        authorName = (TextView) findViewById(R.id.illustview_illustpage_userprofile_name);
        illustDescription = (TextView) findViewById(R.id.illustview_illustpage_illustdescription);
        isLikedToggle = (ToggleButton) findViewById(R.id.illustview_illustpage_liketoggle);
        isLikedToggle.setChecked(isLiked);
        prepararRecycler();
        queue = Volley.newRequestQueue(getApplicationContext());
        loadIllustPages(() -> {
            IllustrationViewCardAdapter adapter = new IllustrationViewCardAdapter(listaImages, this);
            TagsCardsAdapter adapter1 = new TagsCardsAdapter(listaTags, this, this);
            pagesRecycler.setAdapter(adapter);
            tagsRecycler.setAdapter(adapter1);
        });
    }

    private void prepararRecycler() {
        pagesRecycler = (RecyclerView) findViewById(R.id.illustview_illustpages_images);
        pagesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentsRecycler = (RecyclerView) findViewById(R.id.illustview_illustpage_commentsrecycler);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        relatedsRecycler = (RecyclerView) findViewById(R.id.illustview_illustpage_relatedrecycler);
        relatedsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tagsRecycler = (RecyclerView) findViewById(R.id.illustview_illustpage_tagsrecycler);
        tagsRecycler.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));
    }

    private void loadIllustPages(final VolleyCallback callback) {
        String url = "http://" + MainActivity.apiUrl + "/illust/getIllust/" + illustId;
        listaImages = new ArrayList<>();
        listaTags = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray pagesObject = jsonObject.getJSONArray("pages");
                for (int i = 0; i < pagesObject.length(); i++) {
                    JSONObject page = pagesObject.getJSONObject(i);
                    listaImages.add(new IllustrationImages(
                            page.getInt("page_index"),
                            page.getInt("illust_id"),
                            page.getString("page_url")
                    ));
                }
                JSONObject author = jsonObject.getJSONObject("author");
                Glide.with(this)
                        .load(author.getString("thumb_url"))
                        .centerCrop()
                        .into(authorImage);
                authorName.setText(author.getString("author_name"));
                illustTitle.setText(jsonObject.getString("title"));
                illustDescription.setText(jsonObject.getString("title"));

                JSONArray tagsObject = jsonObject.getJSONArray("tags");
                for (int i = 0; i < tagsObject.length(); i++) {
                    JSONObject tag = tagsObject.getJSONObject(i);
                    if (tag.getString("tag_trad").toString().equals("null")) {
                        listaTags.add(new TagsCards(
                                tag.getString("tag_name"),
                                ""
                        ));
                    } else {
                        listaTags.add(new TagsCards(
                                tag.getString("tag_name"),
                                tag.getString("tag_trad")
                        ));
                    }

                }

                callback.onSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i("Error ", "Al Obtener detalle de illust");
            Toast.makeText(this, "Error de Servidor", Toast.LENGTH_SHORT).show();
        });
        queue.add(getRequest);
    }

    public void cerrarIllustview(View view) {
        finish();
    }
    public void openArtistView(View view){
        Intent intent = new Intent(this, ArtistViewActivity.class);
        intent.putExtra("authorid", authorId);
        startActivity(intent);
        this.overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }
    @Override
    public void onTagClick(int position) {
        Toast.makeText(this, "Abriendo search con tag:" + listaTags.get(position).getTagName(), Toast.LENGTH_SHORT).show();
    }
    public void handleLikeToggle(View view){
        likeOrDislike(()->{

        });
    }
    public void likeOrDislike(final VolleyCallback callback){
        String action = "deleteBookmark";
        int userid = MainActivity.user.getUserId();
        if(isLiked)
            action = "addBookmark";
        String url = "http://" + MainActivity.apiUrl + "/illust/"+action+"/"+illustId+"/"+userid;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, response -> {
            callback.onSuccess();
        }, error -> {
            Log.i("error", "error al like");
        });
        queue.add(getRequest);
    }
}