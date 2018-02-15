package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.ChatRealm;
import com.ellekay.lucie.diabetes.auth.SessionManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Created by lucie on 2/13/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter {
    private RealmResults<ChatRealm> mRealmObjects;
    Context mContext;
    String TAG = "Adapter";
    ChatRealm chatRealm;

    SessionManagement session;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessagesAdapter(RealmResults<ChatRealm> mRealmObjects) {
        this.mRealmObjects = mRealmObjects;
    }

    @Override
    public int getItemViewType(int position) {
        chatRealm = mRealmObjects.get(position);

        if (chatRealm.getAuthor().equals(13)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        chatRealm = mRealmObjects.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chatRealm);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chatRealm);
        }
    }

    @Override
    public int getItemCount() {
        return mRealmObjects.size();
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private RealmResults<ChatRealm> mRealmObjects;
        TextView messageText, timeText, nameText;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }

        void bind(ChatRealm chatObj) {

            Date date = chatObj.getTime();
            String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            Log.d(TAG, date.toString());

            try {
                Date newDate = sdf.parse(date.toString());
                String format2 = "dd/MM/yyyy HH:mm";
                SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
                timeText.setText(sdf2.format(newDate));
               // holder.date.setText(sdf2.format(newDate));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Retrofit", ""+e);
            }
            messageText.setText(chatObj.getMessage());
            nameText.setText(chatObj.getAuthor());
        }

    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private RealmResults<ChatRealm> mRealmObjects;
        TextView messageText, timeText;

        public SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatRealm chatObj) {
            messageText.setText(chatObj.getMessage());
            Date date = chatObj.getTime();
            String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            Log.d(TAG, date.toString());
            try {
                Date newDate = sdf.parse(date.toString());
                String format2 = "dd/MM/yyyy HH:mm";
                SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
                timeText.setText(sdf2.format(newDate));
                // holder.date.setText(sdf2.format(newDate));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Retrofit", ""+e);
            }
        }

    }
}
