package com.radhasoamisatsangbeas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class EducationActivity extends AppCompatActivity {
    @BindView(R.id.iv_school)
    ImageView iv_school;
    @BindView(R.id.iv_yoga_tod)
    ImageView iv_yoga_tod;
    @BindView(R.id.iv_yoga_pri)
    ImageView iv_yoga_pri;
    @BindView(R.id.iv_lib)
    ImageView iv_lib;
    @BindView(R.id.iv_annual)
    ImageView iv_annual;
    @BindView(R.id.iv_weave)
    ImageView iv_weave;
    @BindView(R.id.iv_sports_period)
    ImageView iv_sports_period;
    @BindView(R.id.iv_sports_complex)
    ImageView iv_sports_complex;
    @BindView(R.id.iv_comp_lab)
    ImageView iv_comp_lab;
    @BindView(R.id.iv_robo)
    ImageView iv_robo;
    @BindView(R.id.iv_hockey)
    ImageView iv_hockey;
    @BindView(R.id.iv_bad)
    ImageView iv_bad;
    @BindView(R.id.iv_basket)
    ImageView iv_basket;
    @BindView(R.id.iv_car)
    ImageView iv_car;
    @BindView(R.id.iv_class_1)
    ImageView iv_class_1;
    @BindView(R.id.iv_class_2)
    ImageView iv_class_2;
    @BindView(R.id.iv_sculp)
    ImageView iv_sculp;
    @BindView(R.id.iv_sack_race)
    ImageView iv_sack_race;
    @BindView(R.id.iv_path_school)
    ImageView iv_path_school;
    @BindView(R.id.iv_music)
    ImageView iv_music;
    @BindView(R.id.iv_dance)
    ImageView iv_dance;
    @BindView(R.id.iv_morning)
    ImageView iv_morning;
    @BindView(R.id.iv_lang_lab)
    ImageView iv_lang_lab;
    @BindView(R.id.iv_judo)
    ImageView iv_judo;
    @BindView(R.id.iv_gym)
    ImageView iv_gym;
    @BindView(R.id.iv_gym_pri)
    ImageView iv_gym_pri;
    @BindView(R.id.iv_football)
    ImageView iv_football;
    @BindView(R.id.iv_field)
    ImageView iv_field;
    @BindView(R.id.iv_compo)
    ImageView iv_compo;
    @BindView(R.id.iv_dining)
    ImageView iv_dining;
    @BindView(R.id.iv_chef)
    ImageView iv_chef;
    @BindView(R.id.adView)
    AdView adView;
    Ads ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            //getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        }
        getSupportActionBar().setTitle(getResources().getString(R.string.education));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ads=new Ads(this);
        ads.googleAdBanner(this, adView);

        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/school_main.jpg?alt=media&token=ecb65c27-4821-40ab-bdf1-bc791689a9f9",
                iv_school);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/yoga2.jpg?alt=media&token=e6ccc8f4-3627-41f3-bef9-4cdaa2aba1c0",
                iv_yoga_tod);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/yoga.jpg?alt=media&token=f89a912e-ac48-4c0e-8a47-16ebeaca9eb7",
                iv_yoga_pri);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/lib.jpg?alt=media&token=3d8d8aa5-3698-4473-b66e-b09bb151e57d",
                iv_lib);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/sports3.jpg?alt=media&token=3365d267-ce8e-44d1-b140-14e6c419ae87",
                iv_annual);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/badminton.jpg?alt=media&token=4a1a3826-9c3a-4a83-8acf-f22e52856657",
                iv_bad);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/basket.jpg?alt=media&token=d9e5078a-3f92-4ba9-b57f-b0f4356afa91",
                iv_basket);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/car.jpg?alt=media&token=4ed1b5bd-098e-4919-a82a-b2dd97e9b143",
                iv_car);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/chef.jpg?alt=media&token=5b3cc3df-0485-4de4-a20d-9979345a722b",
                iv_chef);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/class1.jpg?alt=media&token=f053ded0-5aee-4b98-bc0a-32e647ba39e0",
                iv_class_1);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/class2.jpg?alt=media&token=25dba2f1-b5d6-4231-8841-8d1ae8af76da",
                iv_class_2);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/computer.jpg?alt=media&token=082919c2-7d3c-4e2f-b49c-6c8729b64e76",
                iv_comp_lab);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/compo.jpg?alt=media&token=d537b573-3455-4f5d-ae7f-8e7508df360e",
                iv_compo);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/dance.jpg?alt=media&token=f3a681d8-5f0b-47f1-8ca4-f9c6fa4d20c7",
                iv_dance);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/dining.jpg?alt=media&token=1a582b95-20df-4712-942f-f9af29f8774c",
                iv_dining);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/field_image.jpg?alt=media&token=dbe68f08-6a91-4859-a240-fcb811220be5",
                iv_field);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/foot.jpg?alt=media&token=f8288da9-cd7b-40eb-8399-1b23b327438b",
                iv_football);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/gym.jpg?alt=media&token=b9989d48-2a9f-42d2-af6d-0c5cf6a19530",
                iv_gym);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/gymna.jpg?alt=media&token=4d9d0861-dcc1-4ce8-aa86-98125d6bae3c",
                iv_gym_pri);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/hockey.jpg?alt=media&token=01d478df-d411-4c6a-ab96-9cbc93e32b00",
                iv_hockey);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/judo.jpg?alt=media&token=c816d5d1-08ba-45dd-91a4-0674a5439188",
                iv_judo);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/language.jpg?alt=media&token=53779812-a4ff-4cf6-8362-aedfae27ce93",
                iv_lang_lab);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/assembly.jpg?alt=media&token=18855ef6-6e39-4f45-934c-80fa996196f1",
                iv_morning);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/music.jpg?alt=media&token=634581ba-4967-4a05-a46b-b25d501f5886",
                iv_music);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/school.jpg?alt=media&token=d2c53c22-f858-4f06-8685-613ba4c4deaa",
                iv_path_school);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/robo.jpg?alt=media&token=0237ddb4-ac28-488f-94a6-57a4504833db",
                iv_robo);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/sack.jpg?alt=media&token=01f11bd7-c532-423b-9e55-f413279b787e",
                iv_sack_race);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/sculpture.jpg?alt=media&token=5de77363-2e31-479d-aa8d-c2e967ad389e",
                iv_sculp);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/sports1.jpg?alt=media&token=7c943202-d9ce-4bf8-8887-8c844b747b92",
                iv_sports_complex);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/sports2.jpg?alt=media&token=395cb2ae-9e2f-46f1-95d0-89a40c879a93",
                iv_sports_period);
        loadImage("https://firebasestorage.googleapis.com/v0/b/radha-soami-33d78.appspot.com/o/weaving.jpg?alt=media&token=d6a08fe6-88ba-416d-a733-8f2ed4f8ba51",
                iv_weave);








    }


    public void loadImage(String url, ImageView imageView){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(EducationActivity.this);
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
                .with(EducationActivity.this)
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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");

        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
