package com.radhasoamisatsangbeas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeActivity extends AppCompatActivity {
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    ApiInterface customAdApiInterface;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;
    @BindView(R.id.rv_video_list)
    RecyclerView rv_video_list;
    VideoAdapter videoAdapter;
    List<VideoModal> videoModalList= new ArrayList<>();
    VideoModal videoModal;
    private String youtubeAPI = "AIzaSyCCHuayCrwwcRAUZ__zTYyOP-ax5FD4R9E";
    YouTubePlayer youTubePlayer,youTubePlayer1;
    private static final int DELAY_MILLIS = 10;
    boolean isFullScreen;
    private static String pageToken="";
    LinearLayoutManager linearLayoutManager;
    private boolean isLastPage = false;
    public static final String KEY_TRANSITION = "KEY_TRANSITION";
    private boolean isLoading = false;
    @BindView(R.id.adView)
    AdView adView;
    Ads ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            //getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        }
        ads=new Ads(this);
        ads.googleAdBanner(this, adView);

        videoAdapter= new VideoAdapter(videoModalList,this);
        linearLayoutManager = new LinearLayoutManager(this);
        //rv_notification.setNestedScrollingEnabled(false);
        rv_video_list.setLayoutManager(linearLayoutManager);

        rv_video_list.setAdapter(videoAdapter);
        srl.setVisibility(View.GONE);
        getSupportActionBar().setTitle(getResources().getString(R.string.youtube));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getYouTubeVideos();
            }
        });

        getYouTubeVideos();

        FrameLayout containerYouTubePlayer = findViewById(R.id.youtube_holder);
        final YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
        getFragmentManager().beginTransaction().replace(containerYouTubePlayer.getId(), youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(youtubeAPI, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer1=youTubePlayer;

                youTubePlayer1.loadVideo("4ZPAuvvdrsA");
                youTubePlayer1.setShowFullscreenButton(true);
                youTubePlayer1.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                    @Override
                    public void onFullscreen(boolean b) {
                        isFullScreen=b;
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        rv_video_list.addOnScrollListener(recyclerViewOnScrollListener);

        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
                getFragmentManager().beginTransaction().replace(containerYouTubePlayer.getId(), youTubePlayerFragment).commit();
                youTubePlayerFragment.initialize(youtubeAPI, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer1=youTubePlayer;
                        youTubePlayer1.loadVideo(videoModalList.get(position).getVideoId());
                        youTubePlayer1.setShowFullscreenButton(true);
                        youTubePlayer1.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean b) {
                                isFullScreen=b;
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

            }

        });



    }

    @Override
    protected void onDestroy() {
        removeListeners();
        pageToken="";
        super.onDestroy();
    }

    private void removeListeners(){
        rv_video_list.removeOnScrollListener(recyclerViewOnScrollListener);
    }
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();


            if (!isLoading && !isLastPage && !TextUtils.isEmpty(pageToken)) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 20) {
                    loadMoreItems();
                }
            }
        }
    };

    public void loadMoreItems(){
        shimmer_view_container.startShimmer();
        shimmer_view_container.setVisibility(View.VISIBLE);

        customAdApiInterface= BaseYoutube.createService(ApiInterface.class);
        Call<JsonElement> call = customAdApiInterface.getNextVideos("AIzaSyCCHuayCrwwcRAUZ__zTYyOP-ax5FD4R9E",
                "UCrpUt9GBfmuaNUIcek8FUZw",
                "snippet,id",
                "date",
                "20",
                "video",
                pageToken
        );
//        if(isNetworkAvailable(getApplicationContext())) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.GONE);

                try {


                    if (response.isSuccessful()) {

                        JSONObject jsonObject= new JSONObject(response.body().toString());
                        if(jsonObject!=null){

                            pageToken = !jsonObject.isNull(jsonObject.getString("nextPageToken"))?jsonObject.getString("nextPageToken"):"";
                            JSONArray jsonArray= jsonObject.getJSONArray("items");
                            for(int i =0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONObject snippet= jsonObject1.getJSONObject("snippet");
                                JSONObject id= jsonObject1.getJSONObject("id");
                                videoModal= new VideoModal(id.getString("videoId"),
                                        snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url"),
                                        snippet.getString("title")
                                );
                                videoModalList.add(videoModal);
                            }

                            if(videoModalList.size()>0){
                                srl.setVisibility(View.VISIBLE);
                                videoAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {

                    }
                }catch (Exception e){

                    // pushToDefault();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");

        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(isFullScreen){
            if(youTubePlayer!=null) {
                youTubePlayer.setFullscreen(false);
            }
        }
        else {
            onBackPressed();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if(isFullScreen){
            if(youTubePlayer1!=null) {
                youTubePlayer1.setFullscreen(false);
            }
        }
        else {
            super.onBackPressed();
        }
    }
    public void getYouTubeVideos(){
        shimmer_view_container.startShimmer();
        shimmer_view_container.setVisibility(View.VISIBLE);

        customAdApiInterface= BaseYoutube.createService(ApiInterface.class);
        Call<JsonElement> call = customAdApiInterface.getVideos();
//        if(isNetworkAvailable(getApplicationContext())) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.GONE);

                try {


                    if (response.isSuccessful()) {

                        JSONObject jsonObject= new JSONObject(response.body().toString());
                        if(jsonObject!=null){
                            pageToken = jsonObject.has("nextPageToken")?jsonObject.getString("nextPageToken"):"";

                            JSONArray jsonArray= jsonObject.getJSONArray("items");
                            for(int i =0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONObject snippet= jsonObject1.getJSONObject("snippet");
                                JSONObject id= jsonObject1.getJSONObject("id");
                                videoModal= new VideoModal(id.getString("videoId"),
                                        snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url"),
                                        snippet.getString("title")
                                );
                                videoModalList.add(videoModal);
                            }

                            if(videoModalList.size()>0){
                                srl.setVisibility(View.VISIBLE);
                                videoAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                    }
                }catch (Exception e){

                    // pushToDefault();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.GONE);
            }
        });
    }
}
