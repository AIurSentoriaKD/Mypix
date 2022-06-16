package com.sylveon.mypixappv1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.sylveon.mypixappv1.Callbacks.VolleyCallback;
import com.sylveon.mypixappv1.Models.LoggedInUser;
import com.sylveon.mypixappv1.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    //public SwipeRefreshLayout swipeRefreshLayout;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    // nota: made this navcontroller variable in class
    // dont know if it will cause problems
    private NavController navController;
    private NavigationView navigationView;
    public static int currentViewSelected;

    public static LoggedInUser user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_userhome, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_collection, R.id.nav_rankings, R.id.nav_trending, R.id.nav_recommended)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // setting the transition animations on the drawer

        navigationView.setNavigationItemSelectedListener( menuItem ->{
            int id = menuItem.getItemId();
            NavOptions.Builder optionsBuilder = new NavOptions.Builder();
            switch (id){
                case R.id.nav_home:{
                    optionsBuilder.setEnterAnim(R.anim.from_left).setExitAnim(R.anim.to_right);
                }
                break;
                case R.id.nav_userhome:
                case R.id.nav_gallery:
                case R.id.nav_slideshow:
                case R.id.nav_rankings:
                case R.id.nav_trending:
                case R.id.nav_recommended:
                case R.id.nav_collection:
                case R.id.nav_settings:{
                    optionsBuilder.setEnterAnim(R.anim.from_right).setExitAnim(R.anim.to_left);
                }
                break;
            }
            navController.navigate(id,null,optionsBuilder.build());
            drawer.close();
            return true;
        });
        initSession();
    }
    public void fillDrawerUserInfo(){
        TextView username = (TextView) findViewById(R.id.profile_name_navbar);
        CircleImageView userprofile = (CircleImageView) findViewById(R.id.profile_image_navbar);
        username.setText(user.getUserName());
        Glide.with(this)
                .load(user.getUserProfileUrl())
                .centerCrop()
                .into(userprofile);
    }
    public void initSession(){
        // algun codigo para comprobar si el usuario esta logeado utilizando db local sqlite
        SQLiteHelper conn = new SQLiteHelper(this,"userdata",null,1);
        SQLiteDatabase getuserinfo = conn.getReadableDatabase();
        try{
            navController.navigate(R.id.splashFragment);
            getSupportActionBar().hide();

            Cursor cursor = getuserinfo.rawQuery("select * from userLogin",null);
            cursor.moveToFirst();
            user = new LoggedInUser(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5), cursor.getString(6));
            reauthenticateUser(()-> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                navController.navigate(R.id.nav_home);
                getSupportActionBar().show();
                fillDrawerUserInfo();
            });
            cursor.close();
            conn.close();
        }catch (Exception e){
            //Toast.makeText(this,"Vuelva a iniciar sesion",Toast.LENGTH_SHORT).show();

            Log.i("INFO", "No previous session found");
            navController.navigate(R.id.userLogin);
            getSupportActionBar().hide();
            // luego llama a authUser con el button click
        }
    }
    public void cerrarSesion(View view) {
        int userid = user.getUserId();
        user = null;
        SQLiteHelper conn = new SQLiteHelper(this,"userdata",null,1);
        SQLiteDatabase deleteuserinfo = conn.getWritableDatabase();
        try {
            deleteuserinfo.execSQL("delete from userLogin where idusuario="+userid);
        } catch (SQLiteException e){
          Log.i("Info","intente borrar rows que no existem, por alguna razon");
          e.printStackTrace();
        }
        deleteuserinfo.close();
        conn.close();
        getSupportActionBar().hide();
        navController.navigate(R.id.userLogin);
    }
    public static String apiUrl = "192.168.1.4:5000";

    public void reauthenticateUser(final VolleyCallback callback){
        String url = "http://"+apiUrl+"/auth/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    //Toast.makeText(this,"Exito en request o login?",Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String username = jsonObject.getString("author_name");
                        int userId = jsonObject.getInt("author_id");
                        String thumb_url = jsonObject.getString("thumb_url");
                        String usermail = jsonObject.getString("email");
                        int followers = jsonObject.getInt("followers");
                        String password = jsonObject.getString("password");
                        user = new LoggedInUser(userId,username,usermail,thumb_url,2,followers,password);
                    } catch (JSONException e) {
                        Log.i("Error", ": al comprobar la response de la reautorización");
                        //e.printStackTrace();
                    }
                    callback.onSuccess();
                },
                error -> {
                    Toast.makeText(this,"Error de servidor",Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.userLogin);
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("email",user.getUserMail());
                params.put("password",user.getPassword());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void authUser(View view){
        authenticateUserCall(()->{
            navController.navigate(R.id.splashFragment);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            navController.navigate(R.id.nav_home);
            getSupportActionBar().show();
            fillDrawerUserInfo();
            try{
                SQLiteHelper conn = new SQLiteHelper(this,"userdata",null,1);
                SQLiteDatabase setuserinfo = conn.getWritableDatabase();
                String date = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
                String userinsert = "insert into userLogin values("+user.getUserId()+",'"+user.getUserName()+"','"+user.getUserMail()+"','"+user.getUserProfileUrl()+"',"+user.getAllowNsfw()+","+user.getFollowersCount()+",'"+date+"','"+user.getPassword()+"')";
                //setuserinfo.execSQL("create table userLogin(idusuario integer primary key, username text, email text, profileurl text, allownsfw integer, followers integer,dateLog text)");
                setuserinfo.execSQL(userinsert);
                setuserinfo.close();
                conn.close();
            }catch (Exception e){
                Log.i("Info","Error al insertar datos a sqlite");
                e.printStackTrace();
            }
        });
    }
    public void authenticateUserCall(final VolleyCallback callback){
        // metodo de autenticación Login
        EditText emailField = (EditText) findViewById(R.id.editTextEmail);
        EditText passwordField = (EditText) findViewById(R.id.editTextPassword);

        String url = "http://"+apiUrl+"/auth/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    //Toast.makeText(this,"Exito en request o login?",Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String username = jsonObject.getString("author_name");
                        int userId = jsonObject.getInt("author_id");
                        String thumb_url = jsonObject.getString("thumb_url");
                        String usermail = jsonObject.getString("email");
                        int followers = jsonObject.getInt("followers");
                        String password = jsonObject.getString("password");
                        user = new LoggedInUser(userId,username,usermail,thumb_url,2,followers,password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onSuccess();
                },
                error -> {
                    Toast.makeText(this,"Error de servidor",Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("email",emailField.getText().toString());
                params.put("password",passwordField.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void getCheckedItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                Log.i("info", "getCheckedItem: "+i);
                currentViewSelected = i;
            }
        }
        if(currentViewSelected == 1){
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            //getSupportActionBar().hide();
        }else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            //getSupportActionBar().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            // do something here
            Toast.makeText(getApplicationContext(),"cargando fragment busqueda",Toast.LENGTH_LONG).show();
            navController.navigate(R.id.nav_userhome);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void navigateToRankings(View view){
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_rankings);
    }
    public void openTrendingTagsActivity(View view){
        //Toast.makeText(getApplicationContext(),"Abriendo trending tags",Toast.LENGTH_LONG).show();
        navController.navigate(R.id.nav_trending);
    }
    public void openRecommendedArtistsActivity(View view){
        //Toast.makeText(getApplicationContext(),"Abriendo recommended Artists",Toast.LENGTH_LONG).show();
        navController.navigate(R.id.nav_recommended);
    }
    public void openLatestActivity(View view){
        Toast.makeText(getApplicationContext(),"Abriendo latest",Toast.LENGTH_LONG).show();
    }

    public void navigateAnimReset(Fragment destination){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.from_right,
                        R.anim.to_left
                ).replace(R.id.nav_home,destination)
                .addToBackStack(null)
                .commit();
    }
}