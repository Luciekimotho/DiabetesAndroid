package com.ellekay.lucie.diabetes.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Overview extends AppCompatActivity {
    private Overview mContext;
    private List<Readings> readingList = new ArrayList<>();
    Button testBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
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
        testBtn = (Button) findViewById(R.id.test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Overview.this,DoctorActivity.class));
            }
        });

        getReadingList();
    }

    private void getReadingList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();
                    Log.d("Retrofit",""+ readingList.toString());
                    Log.d("Retrofit","Response successful ");
                }else {
                    //error
                    Log.d("Retrofit","Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                //doesnt execute
                Log.d("Retrofit","aftr reading function" + t);
            }
        });

    }




}
