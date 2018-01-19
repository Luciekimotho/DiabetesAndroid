package com.ellekay.lucie.diabetes.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucie on 10/1/2017.
 */

public class ReminderFragment extends Fragment {
    private Context mContext;
    private List<Readings> readingList = new ArrayList<>();
    private ReadingAdapter mAdapter;
    private List<Reminder> reminderList = new ArrayList<>();

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Reminders";

    public static ReminderFragment newInstance(){
        ReminderFragment fragement = new ReminderFragment();
        return fragement;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_fragment, container, false);

        getReminderList();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getReminderList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReminders().enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful()) {
                    reminderList = response.body();

                }else {
                    Log.d(TAG,"Reminder: Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {
                //doesn't execute
                Log.d(TAG,"Reminder: Failure" + t);
            }
        });
    }
}