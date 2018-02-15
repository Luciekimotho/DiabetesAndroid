package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.CaregiverRealm;
import com.ellekay.lucie.diabetes.models.ChatRealm;

import io.realm.RealmResults;

/**
 * Created by lucie on 2/13/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private RealmResults<ChatRealm> mRealmObjects;
    ChatRealm chatRealm;
    Context mContext;
    String TAG = "Adapter";

    public ChatAdapter(RealmResults<ChatRealm> mRealmObjects) {
        this.mRealmObjects = mRealmObjects;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.caregiver_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, mRealmObjects.toString());
        holder.mRealmObject = mRealmObjects.get(position);
        holder.name.setText(mRealmObjects.get(position).getRecepient());
        holder.phone.setText(mRealmObjects.get(position).getMessage());
        //holder.relation.setText(mRealmObjects.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, relation, phone;
        public ChatRealm mRealmObject;
        public final View mView;

        public MyViewHolder(View v) {
            super(v);
            mView = v;
            name = (TextView) v.findViewById(R.id.tv_caregiver_name);
            phone = (TextView) v.findViewById(R.id.tv_caregiver_phone);
            relation = (TextView) v.findViewById(R.id.tv_caregiver_relation);
        }
    }
}
