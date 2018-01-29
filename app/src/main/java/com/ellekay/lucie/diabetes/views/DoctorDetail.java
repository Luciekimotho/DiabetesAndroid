package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetail extends AppCompatActivity {
    TextView tvPhone, tvEmail, tvName;
    public static final String ARG_NAME_ID = "item_name_id";
    Doctor doctor;

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;

    String TAG = "Doctor";

    Context mContext;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        tvName = (TextView) findViewById(R.id.tv_doctor_name);
        tvPhone= (TextView) findViewById(R.id.tv_doctor_phone);
        tvEmail = (TextView) findViewById(R.id.tv_doctor_email);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        extras = getIntent().getExtras();
//        int id = (int) extras.getSerializable(ARG_NAME_ID);
        int id = 2;
        Log.d(TAG, "Id clicked is: "+id);


        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctor(id).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,""+ response.body());
                    doctor = response.body();
                    Log.d(TAG, doctor.getName().toString());
                    tvName.setText(doctor.getName().toString());
                    tvEmail.setText(doctor.getEmail().toString());
                    tvPhone.setText(doctor.getPhone().toString());
                    doctor.getNotes().toString();

                }else {
                    Log.d(TAG, "Error "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.d(TAG, ""+t.getMessage());

            }
        });




    }
}
