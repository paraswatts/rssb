package com.radhasoamisatsangbeas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ApiInterface customAdApiInterface;
    @BindView(R.id.tv_quote)
    TextView tv_quote;
    @BindView(R.id.tv_quote_name)
    TextView tv_quote_name;
    @BindView(R.id.ll_about)
    LinearLayout ll_about;
    @BindView(R.id.ll_overview)
    LinearLayout ll_overview;
    @BindView(R.id.ll_youtube)
    LinearLayout ll_youtube;
    @BindView(R.id.ll_education)
    LinearLayout ll_education;
    @BindView(R.id.ll_bhajan)
    LinearLayout ll_bhajan;
    @BindView(R.id.ll_photos)
    LinearLayout ll_photos;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.ll_rate)
    LinearLayout ll_rate;
    @BindView(R.id.ll_play)
    LinearLayout ll_play;
    @BindView(R.id.ll_messages)
    LinearLayout ll_messages;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.punjabi)
    RadioButton punjabi;
    @BindView(R.id.hindi)
    RadioButton hindi;
    @BindView(R.id.english)
    RadioButton english;
    private MediaPlayer mediaPlayer;
    @BindView(R.id.tv_satsang)
    TextView tv_satsang;
    static String satsangUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            getWindow().setNavigationBarColor(Color.parseColor("#8b0000"));

        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSatsangSchedule();
        getQuoteOfTheDay();
        ll_about.setOnClickListener(this);
        ll_overview.setOnClickListener(this);
        ll_youtube.setOnClickListener(this);
        ll_education.setOnClickListener(this);
        ll_bhajan.setOnClickListener(this);
        ll_photos.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_play.setOnClickListener(this);
        ll_rate.setOnClickListener(this);
        ll_messages.setOnClickListener(this);

        hindi.setOnClickListener(this);
        punjabi.setOnClickListener(this);
        english.setOnClickListener(this);
        tv_satsang.setOnClickListener(this);

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        iv_play.setImageResource(R.drawable.ic_play_button);
                    }
                    else{
                        mediaPlayer.start();
                        iv_play.setImageResource(R.drawable.ic_pause);
                    }
                }
            }
        });
        SpannableString content = new SpannableString(tv_satsang.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_satsang.setText(content);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if(lang.equals("en")){
            english.setChecked(true);
            hindi.setChecked(false);
            punjabi.setChecked(false);
        }
        else if(lang.equals("hi")){
            hindi.setChecked(true);
            punjabi.setChecked(false);
            english.setChecked(false);
        }
        else if(lang.equals("pa")){
            hindi.setChecked(false);
            english.setChecked(false);
            punjabi.setChecked(true);
        }
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            recreate();
        }

    }

    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }
    @Override
    protected void onPause() {
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer= MediaPlayer.create(
                MainActivity.this,
                Uri.parse("https://www.rssb.org/audio/shabads/Volume%2010/65.%20Jis%20Ke%20Sir%20Oopar%20Tu%20Swaami%202%20-%20Guru%20Arjan%20Dev%20Ji.mp3"));

        mediaPlayer.start();
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                iv_play.setImageResource(R.drawable.ic_pause);
            } else {
                iv_play.setImageResource(R.drawable.ic_play_button);
            }
        }
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

    public void getSatsangSchedule() {

        customAdApiInterface = BaseApi.createService(ApiInterface.class);
        Call<JsonElement> call = customAdApiInterface.getSatsang(1);
//        if(isNetworkAvailable(getApplicationContext())) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("satsang");
                        satsangUrl=jsonArray.getJSONObject(0).getString("url");

                    } else {
                    }
                } catch (Exception e) {
                    // pushToDefault();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    public void getQuoteOfTheDay() {

        customAdApiInterface = BaseApi.createService(ApiInterface.class);
        Call<JsonElement> call = customAdApiInterface.getQuote(1);
//        if(isNetworkAvailable(getApplicationContext())) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("quotes");
                        tv_quote.setText(jsonArray.getJSONObject(0).getString("quote"));
                        tv_quote_name.setText(jsonArray.getJSONObject(0).getString("soucre"));

                    } else {

                    }
                } catch (Exception e) {
                    // pushToDefault();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_about) {
            startActivity(new Intent(MainActivity.this, AboutUs.class));
        }
        if (view.getId() == R.id.ll_overview) {
            startActivity(new Intent(MainActivity.this, OverviewActivity.class));

        }
        if (view.getId() == R.id.ll_youtube) {
            startActivity(new Intent(MainActivity.this, YoutubeActivity.class));

        }
        if (view.getId() == R.id.ll_education) {
            startActivity(new Intent(MainActivity.this, EducationActivity.class));

        }
        if (view.getId() == R.id.ll_bhajan) {
            startActivity(new Intent(MainActivity.this, AudioActivity.class));

        }
        if (view.getId() == R.id.ll_photos) {
            startActivity(new Intent(MainActivity.this, PhotosActivity.class));

        }
        if (view.getId() == R.id.ll_messages) {
            startActivity(new Intent(MainActivity.this, MessagesActivity.class));

        }
        if (view.getId() == R.id.ll_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                    getString(R.string.download_link)
            );
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        if (view.getId() == R.id.ll_play) {

        }

        if (view.getId() == R.id.ll_rate) {
            final String appPackageName = "com.radhasoamisatsangbeas"; // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        if (view.getId() == R.id.punjabi) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "pa").commit();

            punjabi.setChecked(true);
            hindi.setChecked(false);
            english.setChecked(false);
            setLangRecreate("pa");

        }
        if (view.getId() == R.id.hindi) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();

            punjabi.setChecked(false);
            hindi.setChecked(true);
            english.setChecked(false);
            setLangRecreate("hi");

        }
        if (view.getId() == R.id.english) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();

            punjabi.setChecked(false);
            hindi.setChecked(false);
            english.setChecked(true);
            setLangRecreate("en");

        }

        if(view.getId()==R.id.tv_satsang){
            Intent intent= new Intent(MainActivity.this,FullScreenImage.class);
            intent.putExtra("image",satsangUrl);
            startActivity(intent);
        }
    }
}
