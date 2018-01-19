package com.ellekay.lucie.diabetes.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.ellekay.lucie.diabetes.views.GraphActivity;
import com.ellekay.lucie.diabetes.views.Overview;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.views.Overview;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucie on 10/1/2017.
 */

public class OverviewFragment extends Fragment {
    private Context mContext;
    private List<Readings> readingList = new ArrayList<>();
    private ReadingAdapter mAdapter;
    RealmResults<Glucose> glucoseRealmResults;

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";
    LineChart chart;


    public static OverviewFragment newInstance(){
        OverviewFragment fragement = new OverviewFragment();
        return fragement;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_fragment, container, false);
        chart = (LineChart) v.findViewById(R.id.chart);

        TextView lastcheck = (TextView) v.findViewById(R.id.tv_lastcheck);

        mRealmConfig = new RealmConfiguration
                .Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        glucoseRealmResults = getRealmResults();

        if (glucoseRealmResults.size() == 0){
            getReadingList();
        }else {
            readingList = initiateRealmApi();
        }

        setData();



        Spinner sp_time = (Spinner) v.findViewById(R.id.timespinner);
        List<String> timePeriods = new ArrayList<>();
        timePeriods.add("Day");
        timePeriods.add("Week");
        timePeriods.add("Month");

        ArrayAdapter<String> timePeriodAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, timePeriods);
        timePeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_time.setAdapter(timePeriodAdapter);

        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timePeriod = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + timePeriod, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lastcheck.setText("Last check: "+ "" + " mg/dL");
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getReadingList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();
                    executeRealm(readingList);

                    Log.d(TAG,"Retrofit"+ readingList.toString());
                    mAdapter = new ReadingAdapter(readingList);
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

    private void executeRealm(final List<Readings> glucoseReading){
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
        RealmResults<Glucose> sortedGlucose = mRealm.where(Glucose.class).findAllSorted("id", Sort.ASCENDING).distinct("id");
        Log.d("Realm","Realm size : " + sortedGlucose.size());
        return sortedGlucose;
    }

    private List<Readings> initiateRealmApi(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    response.body();
                    readingList = response.body();
                    executeRealm(readingList);
                }else {
                    Log.d("Realm","Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                Log.d("Retrofit","after reading function" + t);
            }
        });
        return readingList;
    }

    private void setData() {
        Date newDate = new Date();
        List<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        String label;

        for (int i = 0; i<glucoseRealmResults.size(); i++){
            String date = glucoseRealmResults.get(i).getTimeOfDay();

            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

            try {
                newDate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Retrofit", ""+e);
            }

            Log.d(TAG,"Dates"+ String.valueOf(newDate.getTime()));

            label = DateFormat.format("dd/MM", newDate).toString();
            newDate.toString();
            entries.add(new Entry(newDate.getTime(), glucoseRealmResults.get(i).getGlucoseLevel().intValue()));
            labels.add(label);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Glucose levels over time"); // add entries to dataset
        Log.d(TAG,"Data set"+ String.valueOf(dataSet));
        //chart.setDescription("Glucose readings");

        dataSet.setFillAlpha(110);
        dataSet.setColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(labels, dataSets);

        // set data
        chart.setData(data);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
    }

}
