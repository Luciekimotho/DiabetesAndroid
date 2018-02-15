package com.ellekay.lucie.diabetes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ellekay.lucie.diabetes.models.ChatRealm;

import io.realm.RealmResults;

/**
 * Created by lucie on 2/13/2018.
 */

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    private RealmResults<ChatRealm> mRealmObjects;
    public ReceivedMessageHolder(View itemView) {
        super(itemView);
    }

    void bind(RealmResults<ChatRealm> mRealmObjects){

    }
}
