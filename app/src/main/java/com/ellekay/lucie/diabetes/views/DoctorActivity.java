package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivity extends AppCompatActivity {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";

    private Context mContext;
    private List<Doctor> doctorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()){
                    response.body();
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

}
