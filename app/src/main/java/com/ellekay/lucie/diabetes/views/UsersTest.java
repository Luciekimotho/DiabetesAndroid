package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.auth.Login;
import com.ellekay.lucie.diabetes.models.Chat;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Patient;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Recommender;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersTest extends AppCompatActivity  {
    private UsersTest mContext;

    private List<Readings> readingList = new ArrayList<>();
    Patient patient;
    int userId;
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    public int treatment;


    //private Context mContext;

    String TAG = "Recommender";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertest);

        Button one = (Button) findViewById(R.id.one_user);
        Button all = (Button) findViewById(R.id.all_users);

        final TextView patientid = (TextView) findViewById(R.id.userId);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView profList = (TextView) findViewById(R.id.profilelist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.createPatient("Sharon", "sha@malio.com", "12345","1997-11-15T05:00:32Z", 45,78, "Kitui","Female").enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "New patient created: "+ response.body());
                }else {
                    Log.d(TAG, "Patient error: "+ response.message());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.d(TAG, "Patient error: "+ t.toString());
            }
        });

        apiClient.newChat("Come see me","1997-11-15T05:00:32Z", 12,4).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "New chat created: "+ response.body());
                }else {
                    Log.d(TAG, "chat error: "+ response.message());
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Log.d(TAG, "chat fail: "+ t.toString());
            }
        });

        getAllReadings();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = Integer.parseInt(patientid.getText().toString());
                Log.d(TAG, "user id is: "+userId);
                oneUser(userId);
                getPatientReading();
                recommendations(userId);
            }
        });


    }

    public Patient oneUser(int pid){
        final TextView profList = (TextView) findViewById(R.id.profilelist);

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getPatient(String.valueOf(pid)).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    patient = response.body();
                    profList.setText(patient.toString());
                    Log.d(TAG,patient.toString());
                }else {
                    //error
                    Log.d("Retrofit","Response not successful");
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {

            }
        });
        return patient;
    }

    public void allUsers(View view){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getPatients().enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {
                    List<Patient> patientList = response.body();
                    //profList.setText(patientList.toString());
                    Log.d("Patients", patientList.toString());
                    Log.d("Retrofit","Response successful");

                }else {
                    //error
                    Log.d("Retrofit","Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {

            }
        });

    }

    public void toReadings(View view){
        startActivity(new Intent(UsersTest.this, NewReading.class));
    }

    private void getAllReadings(){
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

    private void executeRealmWrite(final List<Readings> glucoseReading){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
               for(int i =0; i < glucoseReading.size(); i++){
                   Glucose glucose = realm.createObject(Glucose.class);
                   glucose.setId(glucoseReading.get(i).getId());
                   glucose.setGlucoseLevel(glucoseReading.get(i).getGlucoseLevel());
                   //glucose.setTimeOfDay(glucoseReading.get(i).getTimeOfDay());
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

    private RealmResults<Glucose> getPatientReading() {
        RealmResults<Glucose> readings = mRealm.where(Glucose.class)
                .equalTo("user", userId).findAll().distinct("id");
        Log.d(TAG,"Patient id: "+userId +" Readings: " + readings.toString());
        return readings;
    }

    private int recommendations(int userId){
        int id, hours, height, weight, uid, divid;
        String time, timePeriod, division;
        float glucose, targetCalories;
        Date date;
        DateTime formattedDate;

        Recommender recommender;
        Patient patient = oneUser(userId);
        RealmResults<Glucose> glucoseResults = getPatientReading();

        for (int i =0; i <glucoseResults.size(); i++){

            glucose = glucoseResults.get(i).getGlucoseLevel();
            date = glucoseResults.get(i).getTimeOfDay();
            timePeriod = glucoseResults.get(i).getTimePeriod();

            uid = glucoseResults.get(i).getUser();

            formattedDate = new DateTime(date);

            Log.d(TAG, date + timePeriod);
            hours = formattedDate.getHourOfDay();
            Log.d(TAG,"Hours: "+ hours);
            hours=18;
            if (hours <= 7 || (hours >=9 && hours <12) || (hours >=14 && hours <=18) || (hours >=20 && hours <=23) ){
                if(glucose > 126){
                     treatment = 1;
                }else if(glucose < 70){
                    treatment = 3;
                }else if(glucose > 180){
                    treatment = 4;
                }

                divid = 1;
                Log.d(TAG,"Hours_1: "+ treatment);
            }
            else if((hours >=22 && hours <= 24)) {
                divid = 2;
                if(glucose < 120){
                    treatment = 2;
                }else if(glucose < 70){
                    treatment = 3;
                }else if(glucose > 180){
                    treatment = 4;
                }

                Log.d(TAG,"Hours_2: "+ treatment);
            }else if((hours >=9 && hours <12) || (hours >=15 && hours <17) || (hours >=20 && hours <=23) ) {
                divid = 3;
                if(glucose < 140){
                    treatment = 1;
                }else if(glucose <100){
                    treatment = 2;
                }else if(glucose < 70){
                    treatment = 3;
                }else if(glucose > 180){
                    treatment = 4;
                }

                Log.d(TAG,"Hours_3: "+ treatment);
            }else {
                if(glucose < 70){
                    treatment = 3;
                }else if(glucose > 180){
                    treatment = 4;
                }

                Log.d(TAG, "Hours_4: " + treatment);
            }
    }

        Log.d(TAG,"Hours_5: "+ treatment);
        return treatment;
    }

}
