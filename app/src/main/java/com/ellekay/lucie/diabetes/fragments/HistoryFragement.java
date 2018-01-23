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
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
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

/**
 * Created by lucie on 10/1/2017.
 */

public class HistoryFragement extends Fragment {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";

    private Context mContext;
    private List<Readings> readingList = new ArrayList<>();

    private RecyclerView recyclerView;
    FloatingActionButton fab;

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

        final RealmResults<Glucose> glucoseRealmResults = getRealmResults();

        if (glucoseRealmResults.size() == 0){
            initiateApi(recyclerView);
        }else {
            setupRecyclerView(recyclerView, glucoseRealmResults);
            Log.d(TAG, "Realm");
        }

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


    private void initiateApi(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();
                    executeRealmWrite(readingList);
                }else {
                    Log.d(TAG,"Retrofit: Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                Log.d(TAG,"Retrofit: after reading function" + t);
            }
        });
    }

//    public class ReadingTask extends AsyncTask<Void, Void, Void> {
//        ProgressDialog progressDialog;
//        @Override
//        protected Void doInBackground(Void... params) {
//            //getReadingListRetrofit();
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(getActivity(),"ProgressDialog","Wait!");
//            //Toast.makeText(getActivity(),"Progress Start",Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            //Toast.makeText(getActivity(),"Progress Ended", Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
//        }
//    }

    private void executeRealmWrite(final List<Readings> glucoseReading){
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
                   Log.d("Realm","savedRealmObjects");
               }
           },new Realm.Transaction.OnError(){
               @Override
               public void onError(Throwable error) {
                   Log.d("Realm","Error: " +error.getMessage());
               }
           }
        );
        Log.d(TAG,"Realm size is:" + glucoseReading.size());
    }

    private RealmResults<Glucose> getRealmResults() {
        RealmResults<Glucose> sortedGlucose = mRealm.where(Glucose.class)
                .findAllSorted("id", Sort.DESCENDING).distinct("id");
        Log.d("Realm","Realm size (sorted) : " + sortedGlucose.size());
        return sortedGlucose;
    }

    private void deleteRealmObjects() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Glucose.class);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"realm objects deleted");
                initiateApi(recyclerView);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<Glucose> results){
        recyclerView.setAdapter(new GlucoseAdapter(results));
    }


}


