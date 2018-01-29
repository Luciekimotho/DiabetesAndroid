package com.ellekay.lucie.diabetes.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.GlucoseAdapter;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.adapters.ReminderAdapter;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;
import com.ellekay.lucie.diabetes.models.ReminderRealm;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.ellekay.lucie.diabetes.views.NewReminder;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucie on 10/1/2017.
 */

public class ReminderFragment extends Fragment {
    private Context mContext;
    private List<Reminder> reminderList = new ArrayList<>();

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Reminders";

    private RecyclerView recyclerView;

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

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewReminder.class));
            }
        });

        mRealmConfig = new RealmConfiguration
                .Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final RealmResults<ReminderRealm> reminderRealmResults = getRealmResults();
        initiateApi(recyclerView);
        if (reminderRealmResults.size() == 0){
            initiateApi(recyclerView);
        }else {
            setupRecyclerView(recyclerView, reminderRealmResults);
        }

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


    private void initiateApi(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReminders().enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful()){
                    response.body();
                    reminderList = response.body();
                    executeRealmWrite(reminderList);
                    Log.d(TAG,""+ reminderList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void getReminderList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReminders().enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful()){
                    response.body();
                    reminderList = response.body();
                    Log.d(TAG,""+ reminderList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void executeRealmWrite(final List<Reminder> reminders){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
                                               for(int i =0; i < reminders.size(); i++){
                                                   ReminderRealm reminder = realm.createObject(ReminderRealm.class);
                                                   reminder.setId(reminders.get(i).getId());
                                                   reminder.setReminder(reminders.get(i).getReminder());
                                                   reminder.setAlarm(reminders.get(i).getAlarm());
                                                   reminder.setTime(reminders.get(i).getTime());
//                                                   glucose.setTimePeriod(glucoseReading.get(i).getTimePeriod());
                                                   reminder.setUser(reminders.get(i).getUser());
                                               }
                                           }

                                       }, new Realm.Transaction.OnSuccess(){

                                           @Override
                                           public void onSuccess() {
                                               Log.d("Realm","savedRealmObjects");
                                           }
                                       },new Realm.Transaction.OnError(){
                                           @Override
                                           public void onError(Throwable error) {
                                               Log.d("Realm","Error: " +error.getMessage());
                                           }
                                       }
        );
        Log.d(TAG,"Realm size is:" + reminders.size());
    }

    private RealmResults<ReminderRealm> getRealmResults() {
        RealmResults<ReminderRealm> reminderRealms = mRealm.where(ReminderRealm.class)
                .findAllSorted("id", Sort.DESCENDING).distinct("id");
        Log.d("Reminder","Realm size (sorted) : " + reminderRealms.toString());
        return reminderRealms;
    }

    private void deleteRealmObjects() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(ReminderRealm.class);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"realm objects deleted");
                initiateApi(recyclerView);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<ReminderRealm> results){
        recyclerView.setAdapter(new ReminderAdapter(results));
    }
}
