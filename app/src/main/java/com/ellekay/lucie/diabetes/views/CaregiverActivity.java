package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.CaregiverAdapter;
import com.ellekay.lucie.diabetes.adapters.DoctorAdapter;
import com.ellekay.lucie.diabetes.models.Caregiver;
import com.ellekay.lucie.diabetes.models.CaregiverRealm;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
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

public class CaregiverActivity extends AppCompatActivity {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Caregiver";

    private Context mContext;
    private List<Caregiver> caregiverList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final RealmResults<CaregiverRealm> caregiverRealms = getRealmResults();
         initiateApi(recyclerView);
        if (caregiverRealms.size() == 0){
            initiateApi(recyclerView);
        }else {
            setupRecyclerView(recyclerView, caregiverRealms);
        }

    }
    private void initiateApi(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getCaregivers().enqueue(new Callback<List<Caregiver>>() {
            @Override
            public void onResponse(Call<List<Caregiver>> call, Response<List<Caregiver>> response) {
                if (response.isSuccessful()){
                    response.body();
                    caregiverList = response.body();
                    executeRealmWrite(caregiverList);
                    Log.d(TAG,""+ caregiverList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Caregiver>> call, Throwable t) {
                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void executeRealmWrite(final List<Caregiver> caregiverReadings){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
           for (int i=0; i<caregiverReadings.size(); i++){
               CaregiverRealm caregiver = realm.createObject(CaregiverRealm.class);
               caregiver.setId(caregiverReadings.get(i).getId());
               caregiver.setName(caregiverReadings.get(i).getName());
               caregiver.setEmail(caregiverReadings.get(i).getEmail());
               caregiver.setPhone(caregiverReadings.get(i).getPhone());
               //doctor.setNotes(caregiverReadings.get(i).getNotes());
               Log.d(TAG, "Realm:"+ caregiver.toString());
           }

       }
   }, new Realm.Transaction.OnSuccess(){
       @Override
       public void onSuccess() {
           Log.d(TAG,"savedRealmObjects");
       }
   },new Realm.Transaction.OnError(){
       @Override
       public void onError(Throwable error) {
           Log.d(TAG,"Not saving error: " +error.toString());
       }
   }
        );
        Log.d(TAG,"Caregiver size is:" + caregiverReadings.size());
    }

    private RealmResults<CaregiverRealm> getRealmResults(){
        RealmResults<CaregiverRealm> sortedDoc = mRealm.where(CaregiverRealm.class)
                .findAllSorted("id", Sort.DESCENDING).distinct("id");
        return sortedDoc;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<CaregiverRealm> results){
        recyclerView.setAdapter(new CaregiverAdapter(results));
    }

}
