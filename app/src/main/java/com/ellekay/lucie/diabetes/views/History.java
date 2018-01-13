package com.ellekay.lucie.diabetes.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Profile;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity {

    private History mContext;
    private List<Readings> readingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReadingAdapter mAdapter;

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //mAdapter = new ReadingAdapter(readingList);
//      recyclerView.setAdapter(mAdapter);
        executeRealm(readingList);
        getReadingList();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(History.this, TakeReadings.class));
            }
        });
    }

    private void executeRealm(final List<Readings> glucoseReading){
        Log.d("Realm", "Size is: "+ glucoseReading.size());
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
    }
    private RealmResults<Glucose> getRealmResults() {
        RealmResults<Glucose> sortedGlucose = mRealm.where(Glucose.class).findAllSorted("rating", Sort.DESCENDING);
        Log.d("Realm","getRealmResults : " + sortedGlucose.size());
        return sortedGlucose;
    }
    private void getReadingList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();
                    Log.d("Retrofit",""+ readingList.toString());
                    mAdapter = new ReadingAdapter(readingList);
                    recyclerView.setAdapter(mAdapter);
                    Log.d("Retrofit","Response successful ");
                }else {
                    //error
                    Log.d("Retrofit","Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                //doesnt execute
                Log.d("Retrofit","after reading function" + t);
            }
        });

    }
}
