package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.ellekay.lucie.diabetes.adapters.DoctorAdapter;
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

public class DoctorActivity extends AppCompatActivity {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Doctor";

    private Context mContext;
    private List<Doctor> doctorsList = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final RealmResults<DoctorRealm> doctorRealms = getRealmResults();
        initiateApi(recyclerView);
        if (doctorRealms.size() == 0){
            initiateApi(recyclerView);
        }else {
            setupRecyclerView(recyclerView, doctorRealms);
        }
    }

    private void initiateApi(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()){
                    response.body();
                    doctorsList = response.body();
                    executeRealmWrite(doctorsList);

                    Log.d(TAG,""+ doctorsList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void executeRealmWrite(final List<Doctor> doctorReadings){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0; i<doctorReadings.size(); i++){
                    DoctorRealm doctor = realm.createObject(DoctorRealm.class);
                    doctor.setId(doctorReadings.get(i).getId());
                    doctor.setName(doctorReadings.get(i).getName());
                    doctor.setEmail(doctorReadings.get(i).getEmail());
                    doctor.setPhone(doctorReadings.get(i).getPhone());
                    doctor.setNotes(doctorReadings.get(i).getNotes());
                    Log.d(TAG, doctor.toString());
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
        Log.d(TAG,"Doctor size is:" + doctorReadings.size());
    }

    private RealmResults<DoctorRealm> getRealmResults(){
        RealmResults<DoctorRealm> sortedDoc = mRealm.where(DoctorRealm.class)
                .findAll();
        return sortedDoc;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<DoctorRealm> results){
        recyclerView.setAdapter(new DoctorAdapter(results));
    }


}

