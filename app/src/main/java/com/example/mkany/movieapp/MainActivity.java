package com.example.mkany.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.opengl.EGLExt;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mkany.movieapp.adaptor.MovieAaptor;
import com.example.mkany.movieapp.api.Client;
import com.example.mkany.movieapp.api.Service;
import com.example.mkany.movieapp.model.Movie;
import com.example.mkany.movieapp.model.MovieResponse;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final static String API_KEY = "62e640366d4a28a538995764bd7d1401";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiViews();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.background_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                intiViews();
            }
        });
    }

    private Activity getActivity() {
        Context context= this;
        while(context instanceof ContextWrapper)
        {
            if(context instanceof Activity)
            {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void intiViews() {
        if(isNetworkConnected())
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetchig movies...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            connectAndGetApiData();
        }
        else {
            final AlertDialog builder = new AlertDialog.Builder(this)
                    .setTitle("Connection failed")
                    .setMessage("This application requires network access, Please enable mobile network or Wi-Fi.")
                    .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int i){
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int i){
                        }
                    })
                    .show();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // This method create an instance of Retrofit
    public void connectAndGetApiData(){

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        Service movieApiService = retrofit.create(Service.class);

        Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MovieAaptor(movies, R.layout.movie_card, getApplicationContext()));
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.smoothScrollToPosition(0);
                    if(swipeRefreshLayout.isRefreshing())
                    {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
