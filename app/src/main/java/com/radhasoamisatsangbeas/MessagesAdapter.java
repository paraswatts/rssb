package com.radhasoamisatsangbeas;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private static OnItemClickListener mListener;

    private List<MessageModal> items;
    private int itemLayout;
    Context context;
    String deviceId;
    public MessagesAdapter(List<MessageModal> items, Context context, String deviceId) {
        this.items = items;
        this.context=context;
        this.deviceId = deviceId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        MessageModal item = items.get(position);
        Resources r = context.getResources();
        int px8 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );
        int px4 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                r.getDisplayMetrics()
        );
        int px64 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                64,
                r.getDisplayMetrics()
        );
        Log.e("item.getMessage()",item.getMessage()+"====");
        holder.message_text.setText(item.getMessage());

        holder.iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Message", item.getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        holder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                        item.getMessage()
                );
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
         if(position == items.size()-1){
             if(deviceId.equals(item.getDeviceId())) {
                 cardLayoutParams.setMargins(px64, px8, px8, px8);

             }else{
                 Log.e("else ","devide id not moatch");
                 cardLayoutParams.setMargins(px8, px8, px64, px8);

             }
            holder.feed_card.setLayoutParams(cardLayoutParams);
        }
        else{
             if(deviceId.equals(item.getDeviceId())) {
                 cardLayoutParams.setMargins(px64, px8, px8, 0);

             }else{
                 Log.e("else ","devide id not moatch");

                 cardLayoutParams.setMargins(px8, px8, px64, 0);

             }
            holder.feed_card.setLayoutParams(cardLayoutParams);

        }


    }

    @Override public int getItemCount() {
        Log.e("items",items.size()+"items");
        return items.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView message_text;
        RelativeLayout feed_card;
        ImageView iv_copy,iv_share;
        public ViewHolder(View itemView) {
            super(itemView);
            message_text= itemView.findViewById(R.id.message_text);
            feed_card= itemView.findViewById(R.id.feed_card);
            iv_copy= itemView.findViewById(R.id.iv_copy);
            iv_share= itemView.findViewById(R.id.iv_share);

        }
    }
}