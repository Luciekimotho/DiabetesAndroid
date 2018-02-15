package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Chat;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Doctor";

    private Context mContext;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        getChats();
    }

    private void getChats(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getChats().enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful()){
                    response.body();
                    chatList = response.body();
                    Log.d(TAG,""+ chatList);
                }else {
                    Log.d(TAG, "Error "+response.toString());
                }
            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }
}
