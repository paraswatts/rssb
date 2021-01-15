package com.radhasoamisatsangbeas;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {
    Query next;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.rl_send)
    RelativeLayout rl_send;
    ApiInterface customAdApiInterface;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;
    @BindView(R.id.rv_video_list)
    RecyclerView rv_video_list;
    MessagesAdapter messagesAdapter;
    List<MessageModal> messageModalList = new ArrayList<>();
    MessageModal messageModal;
    private static final int DELAY_MILLIS = 10;
    boolean isFullScreen;
    private static String pageToken = "";
    LinearLayoutManager linearLayoutManager;
    private boolean isLastPage = false;
    public static final String KEY_TRANSITION = "KEY_TRANSITION";
    private boolean isLoading = false;
    @BindView(R.id.adView)
    AdView adView;
    Ads ads;
    String LOG = getClass().getSimpleName();
    FirebaseFirestore firebaseFirestore;
    TextInputEditText et_send_message;
    private int limit = 5;
    boolean isScrolling = true;
    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached = false;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, Color.parseColor("#8b0000"));

            setTaskDescription(td);
            getWindow().setStatusBarColor(Color.parseColor("#8b0000"));
            //getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        ads = new Ads(this);
        ads.googleAdBanner(this, adView);
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        messagesAdapter = new MessagesAdapter(messageModalList, this, deviceId);
        linearLayoutManager = new LinearLayoutManager(this);
        //rv_notification.setNestedScrollingEnabled(false);
        rv_video_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);


        rv_video_list.setAdapter(messagesAdapter);
        srl.setVisibility(View.GONE);
        getSupportActionBar().setTitle(getResources().getString(R.string.messages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        et_send_message = findViewById(R.id.et_send_message);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                messageModalList.clear();

                getMessages();
                srl.setRefreshing(false);
            }
        });

        getMessages();


        rv_video_list.addOnScrollListener(recyclerViewOnScrollListener);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG, et_send_message.getText().toString().trim() + "===");

                if (et_send_message.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MessagesActivity.this, "Type message", Toast.LENGTH_SHORT).show();
                } else {
                    MessageModal messageModal = new MessageModal();
                    messageModal.setMessage(et_send_message.getText().toString().trim());
                    messageModal.setDeviceId(deviceId);
                    messageModal.setTimeStamp(System.currentTimeMillis());

                    CollectionReference collectionReference = firebaseFirestore.collection("messages");
                    collectionReference.add(messageModal).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            et_send_message.setText(null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MessagesActivity.this, "Failed to send Message", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        removeListeners();
        pageToken = "";
        super.onDestroy();
    }

    private void removeListeners() {
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
//            Boolean reachBottom = !recyclerView.canScrollVertically(-1);
//            int visibleItemCount = linearLayoutManager.getChildCount();
//            int totalItemCount = linearLayoutManager.getItemCount();
//            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//
//            Log.e(LOG,visibleItemCount+"reachBottom"+totalItemCount+"totalItemCount"+firstVisibleItemPosition);
//
//            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                    && firstVisibleItemPosition >= 0
//                    && totalItemCount >= limit) {
//                   loadMoreItems();
//            }
        }
    };

    public void loadMoreItems() {

        next = firebaseFirestore.collection("messages")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(limit);
        next.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("YourTag", "Listen failed.", e);
                    return;
                }
                Log.e(LOG, queryDocumentSnapshots.size() + "queryDocumentSnapshots.size()");
                lastVisible =
                        queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.exists()) {
                        MessageModal message = new MessageModal();
                        message.setMessage(doc.getString("message"));
                        message.setDeviceId(doc.getString("deviceId"));
                        Log.e(LOG, doc.getString("message") + "doc.getString(\"message\")");
                        messageModalList.add(message);

                    }
                }
                if (messageModalList.size() > 0) {
                    srl.setVisibility(View.VISIBLE);
                    messagesAdapter.notifyDataSetChanged();
                }
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.INVISIBLE);
                Log.e(LOG, messageModalList.size() + "========");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");

        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getMessages() {

        CollectionReference productsRef = firebaseFirestore.collection("messages");

        Query query = productsRef.orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(limit);
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        messageModalList.clear();
                        if (e != null) {
                            Log.w("YourTag", "Listen failed.", e);
                            return;
                        }
                        if (!queryDocumentSnapshots.isEmpty()) {
                            lastVisible =
                                    queryDocumentSnapshots.getDocuments()
                                            .get(queryDocumentSnapshots.size() - 1);

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.exists()) {
                                    MessageModal message = new MessageModal();
                                    message.setMessage(doc.getString("message"));
                                    message.setDeviceId(doc.getString("deviceId"));

                                    messageModalList.add(message);

                                }
                            }
                            if (messageModalList.size() > 0) {
                                srl.setVisibility(View.VISIBLE);
                                messagesAdapter.notifyDataSetChanged();
                            }
                            shimmer_view_container.stopShimmer();
                            shimmer_view_container.setVisibility(View.INVISIBLE);
                            Log.e(LOG, messageModalList.size() + "========");

                            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                        isScrolling = true;
                                    }
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    int visibleItemCount = linearLayoutManager.getChildCount();
                                    int totalItemCount = linearLayoutManager.getItemCount();

                                    if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                        isScrolling = false;
                                        Query nextQuery = productsRef.orderBy("timeStamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(limit);
                                        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                        if (doc.exists()) {
                                                            MessageModal message = new MessageModal();
                                                            message.setMessage(doc.getString("message"));
                                                            message.setDeviceId(doc.getString("deviceId"));

                                                            messageModalList.add(message);

                                                        }
                                                    }
                                                    messagesAdapter.notifyDataSetChanged();

                                                    lastVisible =
                                                            queryDocumentSnapshots.getDocuments()
                                                                    .get(queryDocumentSnapshots.size() - 1);

                                                    if (queryDocumentSnapshots.size() < limit) {
                                                        isLastItemReached = true;
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                            };
                            rv_video_list.addOnScrollListener(onScrollListener);
                        }
                        else{
                            shimmer_view_container.stopShimmer();
                            shimmer_view_container.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

//    public void getMessages() {
//        Query query = firebaseFirestore.collection("messages")
//                .orderBy("timeStamp",Query.Direction.DESCENDING)
//                .limit(limit);
//        query
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
//                                        @Nullable FirebaseFirestoreException e) {
//                        messageModalList.clear();
//                        if (e != null) {
//                            Log.w("YourTag", "Listen failed.", e);
//                            return;
//                        }
//                        lastVisible =
//                                queryDocumentSnapshots.getDocuments()
//                                        .get(queryDocumentSnapshots.size() -1);
//
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            if (doc.exists()){
//                                MessageModal message = new MessageModal();
//                                message.setMessage(doc.getString("message"));
//                                message.setDeviceId(doc.getString("deviceId"));
//
//                                messageModalList.add(message);
//
//                            }
//                        }
//                        if (messageModalList.size() > 0) {
//                            srl.setVisibility(View.VISIBLE);
//                            messagesAdapter.notifyDataSetChanged();
//                        }
//                        shimmer_view_container.stopShimmer();
//                        shimmer_view_container.setVisibility(View.INVISIBLE);
//                        Log.e(LOG, messageModalList.size() + "========");
//                    }
//                });
//
//    }
}
