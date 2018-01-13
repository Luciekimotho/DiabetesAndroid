package com.ellekay.lucie.diabetes.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.ellekay.lucie.diabetes.views.Overview;
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

    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";

    Date firstDatex = new Date();
    Date endDatex = new Date();


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

        GraphView graph = (GraphView) v.findViewById(R.id.graph);
        TextView lastcheck = (TextView) v.findViewById(R.id.tv_lastcheck);

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

        mRealmConfig = new RealmConfiguration
                .Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        final RealmResults<Glucose> glucoseRealmResults = getRealmResults();

        if (glucoseRealmResults.size() == 0){
            getReadingList();
            Log.d("Diabetes","Retrofit");
        }else {
            readingList = initiateRealmApi();
            Log.d(TAG, ""+ readingList.size());
            Log.d("Diabetes","Realm");
        }
        String firstDate = null, endDate = null;
        String minReading = null, maxReading = null;
        String label;
        DataPoint[] dataPoints = new DataPoint[glucoseRealmResults.size()];
        Date newDate = new Date();

        for (int i = 0; i<glucoseRealmResults.size(); i++){
            String date = glucoseRealmResults.get(i).getTimeOfDay();
            Log.d(TAG, "raw date is: "+date);

            firstDate = glucoseRealmResults.get(0).getTimeOfDay();
            endDate = glucoseRealmResults.get(glucoseRealmResults.size()-1).getTimeOfDay();

            minReading = glucoseRealmResults.get(0).getGlucoseLevel().toString();
            maxReading = glucoseRealmResults.get(glucoseRealmResults.size()-1).getGlucoseLevel().toString();

            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

            try {
                newDate = sdf.parse(date);
                firstDatex = sdf.parse(firstDate);
                endDatex = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Retrofit", ""+e);
            }

            Log.d(TAG, ""+ glucoseRealmResults.get(i).getGlucoseLevel() );
            Log.d(TAG, ""+ glucoseRealmResults.get(i).getId() );
            Log.d(TAG, "date: " +newDate.getTime());
            label = DateFormat.format("dd/MM hh:mm", newDate).toString();

            dataPoints[i] = new DataPoint(newDate.getTime(), glucoseRealmResults.get(i).getGlucoseLevel());
        }
        Log.d(TAG, "Realm size graph: "+ glucoseRealmResults.size());

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(2); // only 4 because of the space

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(firstDatex.getTime());
        graph.getViewport().setMaxX(endDatex.getTime());
        Log.d(TAG, "First date: "+firstDatex);
        graph.getViewport().setXAxisBoundsManual(true);

        lastcheck.setText("Last check: "+maxReading + " mg/dL");

//
//        GraphView graph = (GraphView) v.findViewById(R.id.graph);
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//
//        });
//        graph.addSeries(series);
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
        Log.d(TAG,"Realm size is:" + glucoseReading.size());
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

                    Log.d("Realm",""+ readingList.toString());
                    mAdapter = new ReadingAdapter(readingList);

                    Log.d(TAG,"Realm: Response successful ");
                }else {
                    //error
                    Log.d("Realm","Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                //doesnt execute
                Log.d("Retrofit","after reading function" + t);
            }
        });
        return readingList;
    }

}
