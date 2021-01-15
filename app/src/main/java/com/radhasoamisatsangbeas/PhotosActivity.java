package com.radhasoamisatsangbeas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosActivity extends AppCompatActivity {
    @BindView(R.id.rv_photos)
    RecyclerView rv_photos;
    List<PhotosModal> photosModalList=new ArrayList<>();
    PhotosModal photosModal;
    PhotosAdapter photosAdapter;
    @BindView(R.id.adView)
    AdView adView;
    Ads ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            //getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        }
        ads=new Ads(this);
        ads.googleAdBanner(this, adView);

        getSupportActionBar().setTitle(getResources().getString(R.string.photos));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/soamiji.jpg?alt=media&token=8235b6a6-25a8-4f57-93d7-24ce15799c35",
                R.string.soami_ji);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/popup_sawan_ji.jpg?alt=media&token=3f6fa392-eec7-4ee6-8e98-9814a006d4eb",
                R.string.sawan_singh);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/popup_jagat_ji.jpg?alt=media&token=17188546-7629-4d71-8645-cd565dc4721b",
                R.string.jagat_singh);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/popup_charan_ji.jpg?alt=media&token=b5ad6203-7b65-49a8-a958-21193070e3d1",
                R.string.charan);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/babaji.jpg?alt=media&token=ede9592c-b65e-46b6-a9b6-3104340b99b8",
                R.string.gurinder);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/babaji2.jpg?alt=media&token=73da2f83-b7d3-442b-8051-b13a3e824e98",
                R.string.gurinder);
        setImages("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/babaji_gate.jpg?alt=media&token=751ca722-924c-4d74-b1a7-76e661837f81",
                R.string.gurinder);
        photosAdapter= new PhotosAdapter(photosModalList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //rv_notification.setNestedScrollingEnabled(false);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_photos.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv_photos.setAdapter(photosAdapter);
        photosAdapter.notifyDataSetChanged();
    }
    public void setImages(String url,int title){
        photosModal= new PhotosModal(url,
                getResources().getString(title));
        photosModalList.add(photosModal);
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
        onBackPressed();
        return true;
    }
}
