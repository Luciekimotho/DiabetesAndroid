package com.ellekay.lucie.diabetes.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.DoctorRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class DoctorDetail extends AppCompatActivity {
    TextView tvPhone, tvEmail, tvName;
    public static final String ARG_NAME_ID = "item_name_id";

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;

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
        int id = (int) extras.getSerializable(ARG_NAME_ID);


        RealmQuery<DoctorRealm> doctor = mRealm.where(DoctorRealm.class).equalTo("id", id);

    }
}
