package com.ellekay.lucie.diabetes.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.ellekay.lucie.diabetes.Application;
import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.GlucoseAdapter;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;

import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;

import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.ellekay.lucie.diabetes.views.History;
import com.ellekay.lucie.diabetes.views.TakeReadings;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lucie on 10/1/2017.
 */

public class HistoryFragement extends Fragment {
    private Context mContext;
    private List<Readings> readingList = new ArrayList<>();

    private List<Reminder> reminderList = new ArrayList<>();
    private List<Doctor> doctorList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ReadingAdapter mAdapter;
    FloatingActionButton fab;
    ReadingTask readingTask;

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";

    public static HistoryFragement newInstance(){
        HistoryFragement fragement = new HistoryFragement();
        return fragement;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        mContext = getContext();

        mRealmConfig = new RealmConfiguration
                .Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TakeReadings.class));
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getReadingListRetrofit();
        final RealmResults<Glucose> glucoseRealmResults = getRealmResults();
        Log.d(TAG, "Realm size list: "+ glucoseRealmResults.size());
        if (glucoseRealmResults.size() == 0){
            getReadingListRetrofit();
            mAdapter = new ReadingAdapter(readingList);
            recyclerView.setAdapter(mAdapter);
        }else {
            setupRecyclerViewRealm(recyclerView, glucoseRealmResults);
        }
        getReminderList();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getReadingListRetrofit(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();

                    Log.d(TAG,"Retrofit"+ readingList.toString());
                    executeRealm(readingList);

                    executeRealm(readingList);

                    Log.d(TAG,"Retrofit"+ readingList.toString());
                    mAdapter = new ReadingAdapter(readingList);
                    recyclerView.setAdapter(mAdapter);

                    Log.d(TAG,"Retrofit: Response successful ");
                }else {
                    //error
                    Log.d(TAG,"Retrofit: Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                //doesn't execute
                Log.d(TAG,"Retrofit: after reading function" + t);
            }
        });
    }

    private void getReminderList(){
        ApiClient apiClient2 = ApiClient.Factory.getInstance(mContext);
        apiClient2.getReminders().enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful()){
                    reminderList = response.body();
                    Log.d(TAG, "Reminder list: "+reminderList.toString());
                    Log.d(TAG, "Reminder list: Response successful ");
                }else {
                    Log.d(TAG, "Reminder list: Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {
                Log.d(TAG, "Reminder list error: " + t);
            }
        });
    }


    public class ReadingTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... params) {

            getReadingListRetrofit();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"ProgressDialog","Wait!");
            //Toast.makeText(getActivity(),"Progress Start",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(getActivity(),"Progress Ended", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    private void executeRealm(final List<Readings> glucoseReading){
        Log.d(TAG,"Realm size is:" + glucoseReading.size());
        mRealm.executeTransactionAsync(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               for(int i =0; i < glucoseReading.size(); i++){
                   Glucose glucose = realm.createObject(Glucose.class);
                   glucose.setId(glucoseReading.get(i).getId());
                   glucose.setGlucoseLevel(glucoseReading.get(i).getGlucoseLevel());
                   glucose.setTimeOfDay(glucoseReading.get(i).getTimeOfDay());
                   glucose.setTimePeriod(glucoseReading.get(i).getTimePeriod());
                   glucose.setAction(glucoseReading.get(i).getAction());
                   glucose.setMedication(glucoseReading.get(i).getMedication());
                   glucose.setNotes(glucoseReading.get(i).getNotes());
                   glucose.setCreatedAt(glucoseReading.get(i).getCreatedAt());
                   glucose.setUpdatedAt(glucoseReading.get(i).getUpdatedAt());
                   glucose.setUser(glucoseReading.get(i).getUser());

               }
           }

           }, new Realm.Transaction.OnSuccess(){

               @Override
               public void onSuccess() {

                  // Log.d("Realm","savedRealmObjects");

                   Log.d("Realm","savedRealmObjects");

               }
           },new Realm.Transaction.OnError(){
               @Override
               public void onError(Throwable error) {
                   Log.d("Realm","Error: " +error.getMessage());
               }
           }
        );
    }

    private RealmResults<Glucose> getRealmResults() {

        RealmResults<Glucose> sortedGlucose = mRealm.where(Glucose.class).findAllSorted("id", Sort.DESCENDING).distinct("id");
        //Log.d("Realm","Realm size (sorted) : " + sortedGlucose.size());
        return sortedGlucose;
    }

    private void setupRecyclerViewRealm(final RecyclerView recyclerView, RealmResults<Glucose> results){
        // progessBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new GlucoseAdapter(results));
    }
    private void initiateRealmApi(final RecyclerView recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    response.body();
                    readingList = response.body();
                    executeRealm(readingList);

                    Log.d("Realm",""+ readingList.toString());
                    mAdapter = new ReadingAdapter(readingList);
                    recyclerView.setAdapter(mAdapter);

                    Log.d(TAG,"Realm: Response successful ");
                }else {
                    //error
                    Log.d("Realm","Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                //doesnt execute
                Log.d("Retrofit","after reading function" + t);
            }
        });
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<Glucose> results) {
       // progessBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new GlucoseAdapter(results));
    }

}


