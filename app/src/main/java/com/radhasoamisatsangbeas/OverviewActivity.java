package com.radhasoamisatsangbeas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_dera)
    ImageView iv_dera;
    @BindView(R.id.iv_hospital)
    ImageView iv_hospital;
    @BindView(R.id.iv_commnity)
    ImageView iv_commnity;
    @BindView(R.id.iv_hostal)
    ImageView iv_hostal;
    @BindView(R.id.iv_langar)
    ImageView iv_langar;
    @BindView(R.id.iv_school)
    ImageView iv_school;
    @BindView(R.id.iv_lib)
    ImageView iv_lib;
    @BindView(R.id.iv_meeting)
    ImageView iv_meeting;

    @BindView(R.id.adView)
    AdView adView;
    Ads ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            //getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        }
        ads=new Ads(this);
        ads.googleAdBanner(this, adView);
        getSupportActionBar().setTitle(getResources().getString(R.string.overview));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        iv_dera.setOnClickListener(this);
        iv_commnity.setOnClickListener(this);
        iv_hospital.setOnClickListener(this);
        iv_hostal.setOnClickListener(this);
        iv_school.setOnClickListener(this);
        iv_meeting.setOnClickListener(this);
        iv_langar.setOnClickListener(this);
        iv_lib.setOnClickListener(this);

        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/dera_0.jpg?alt=media&token=aa8e78af-eb52-4dd5-8d07-f880d7080542",
                iv_dera);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/1_library.jpg?alt=media&token=4519a17d-05f3-4eb8-a32e-627a760a8c86",
                iv_lib);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/hospital.jpg?alt=media&token=58caa147-bb14-4dcf-a6aa-a873e158a5f2",
                iv_hospital);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/1_venue.jpg?alt=media&token=8fb745f4-340e-4897-9fdb-24764ec74ebc",
                iv_meeting);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/langar.jpg?alt=media&token=6a8e33f7-697e-449d-b2c9-b4aa0faac45c",
                iv_langar);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/hostal.jpg?alt=media&token=cb45b56e-7ff8-4a26-9bcf-db50ff0f9f16",
                iv_hostal);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/solar.jpg?alt=media&token=4960ab41-f249-4571-b8e7-b5f3e13693c5",
                iv_commnity);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/school.jpg?alt=media&token=d2c53c22-f858-4f06-8685-613ba4c4deaa",
                iv_school);
    }

    public void loadImage(String url,ImageView imageView){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(OverviewActivity.this);
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(40f);
        int[] colors1 = {getResources().getColor(R.color.googleBlue),
                getResources().getColor(R.color.googleRed),
                getResources().getColor(R.color.googleYellow),
                getResources().getColor(R.color.googleGreen)};
        circularProgressDrawable.setColorSchemeColors(colors1);
        circularProgressDrawable.start();
        circularProgressDrawable.setStrokeCap(Paint.Cap.ROUND);

        Glide
                .with(OverviewActivity.this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //((StaffViewHolder) holder).staff_image.setImageResource(R.drawable.img_avatar);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .apply(new RequestOptions().placeholder(circularProgressDrawable))
                .into(imageView);
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

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.iv_dera){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Fdera_0.jpg?alt=media&token=0b4bf9b9-32a2-4427-b5da-76f9cf79370f");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_commnity){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Fsolar.jpg?alt=media&token=6b2f1960-5177-4d83-911e-cc90e457af9e");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_langar){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Flangar.jpg?alt=media&token=6b80cb09-abc0-41a9-adf6-760771ea50d9");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_lib){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2F1_library.jpg?alt=media&token=f2710496-997a-4cca-b0fb-d24807391759");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_school){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Fschool.jpg?alt=media&token=4fda21f3-8193-4730-b8b6-ec3165737df3");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_hostal){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Fhostal.jpg?alt=media&token=d955c3d8-2c9a-4005-a3ee-649d3ae29ac9");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_meeting){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2F1_venue.jpg?alt=media&token=a0783b94-a012-4fd8-b12d-fdc2c4499084");
            startActivity(intent);
        }
        if(view.getId()==R.id.iv_hospital){
            Intent intent= new Intent(OverviewActivity.this,FullScreenImage.class);
            intent.putExtra("image","https://firebasestorage.googleapis.com/v0/b/namo-4f619.appspot.com/o/radhasoami%2Fhospital.jpg?alt=media&token=59859f9d-b310-482d-b5ec-d72131924dd9");
            startActivity(intent);
        }

    }
}
