package com.ellekay.lucie.diabetes.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
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

import android.widget.Switch;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ReadingAdapter;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.GlucoseGraphObject;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.ellekay.lucie.diabetes.views.DoctorActivity;
import com.ellekay.lucie.diabetes.views.GraphActivity;
import com.ellekay.lucie.diabetes.views.Overview;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.util.Collections;
import java.util.Comparator;
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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    List<GlucoseGraphObject> glucoseGraphObjects;
    private List<String> xValues = new ArrayList<>();
    boolean isNewGraphEnabled;
    Switch alarmSwitch;
    long lastInsertedId;
    Glucose lastEntry;

    String lastGlucose;
    Date lastTime, formattedDate;


    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Diabetes";
    LineChart chart;
    Button testBtn;
    View v;

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
        v = inflater.inflate(R.layout.overview_fragment, container, false);
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

        final XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTextColor(getResources().getColor(R.color.glucosio_text_light));
        xAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTextColor(getResources().getColor(R.color.glucosio_text_light));
        leftAxis.setStartAtZero(false);
        leftAxis.disableGridDashedLine();
        leftAxis.setDrawGridLines(false);
        //leftAxis.addLimitLine(ll1);
        //leftAxis.addLimitLine(ll2);
        //leftAxis.setDrawLimitLinesBehindData(true);


        glucoseGraphObjects = generateGlucoseGraphPoints();

        chart.getAxisRight().setEnabled(false);
        chart.setBackgroundColor(Color.parseColor("#FFFFFF"));
        chart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));

        setData2();
        setData();

//        lastEntry = getLastEntry();
//        lastTime = lastEntry.getTimeOfDay();
//        lastGlucose = lastEntry.getGlucoseLevel().toString();
//
//        String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
//        try {
//            formattedDate = sdf.parse(lastTime.toString());
//            String format2 = "dd/MM/yy";
//            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
//            sdf2.format(formattedDate);
//            lastcheck.setText("Last check: "+sdf2.format(formattedDate) +"             " +lastGlucose+ " mg/dL");
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Log.e("Retrofit", ""+e);
//        }

        //lastcheck.setText("Last check: "+formattedDate +"" +lastGlucose+ " mg/dL");
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
        Log.d(TAG,"Realm size : " + sortedGlucose.size());
//        lastInsertedId = sortedGlucose.last().getId();
//        lastEntry = mRealm.where(Glucose.class)
//               .equalTo("id", lastInsertedId)
//               .findFirst();
//        Log.d(TAG, "LAST entry: "+ lastEntry);
        return sortedGlucose;
    }

    private  Glucose getLastEntry(){
        RealmResults<Glucose> sortedGlucose = mRealm.where(Glucose.class).findAllSorted("id", Sort.ASCENDING).distinct("id");
        //lastInsertedId = sortedGlucose.last().getId();
        Glucose glastEntry = mRealm.where(Glucose.class)
                .equalTo("id", lastInsertedId)
                .findFirst();
        Log.d(TAG, "LAST entry: "+ glastEntry);
        return glastEntry;
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

    private void setData2() {
        Date newDate = new Date();
        List<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        String label;

        for (int i = 0; i<glucoseRealmResults.size(); i++){
            Date date = glucoseRealmResults.get(i).getTimeOfDay();

            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

            try {
                newDate = sdf.parse(date.toString());
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

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(labels, dataSets);
        // set data
        chart.setData(data);

    }

    private LineData generateData(){
        List<String> xVals = new ArrayList<>();
        List<Entry> yVals = new ArrayList<>();
        float val;

        List<Integer> glucoseReadings = getGlucoseReadings();
        for (int i = 0; i < glucoseReadings.size(); i++) {
             val = glucoseReadings.get(i);
            //yVals.add(new Entry(i, val));   //Recommended one
            yVals.add(new Entry(val,i));
        }
        for (int i=0; i<getGraphGlucoseDateTime().size(); i++){
            String date = getGraphGlucoseDateTime().get(i);
            xVals.add(date);
        }
        xValues = xVals;

        LineData data = new LineData(xVals, generateLineDataSet(yVals, ContextCompat.getColor(getContext(), R.color.hypo)));
        return data;
    }

    private LineDataSet generateLineDataSet(List<Entry> vals, int color) {
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(vals, "");
        List<Integer> colors = new ArrayList<>();

        set1.setCircleColor(color);

        set1.setColor(color);
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setDrawCircleHole(true);
        set1.disableDashedLine();
        set1.setFillAlpha(255);
        set1.setDrawFilled(true);
        set1.setValueTextSize(0);
        set1.setValueTextColor(Color.parseColor("#FFFFFF"));
//        set1.setFillDrawable(getResources().getDrawable(R.drawable.graph_gradient));
        set1.setHighLightColor(ContextCompat.getColor(getContext(), R.color.hypo));
        set1.setCubicIntensity(0.2f);

        return set1;
    }

    public List<Integer> getGlucoseReadings() {
        ArrayList<Integer> glucoseReadings = new ArrayList<>();
        for (int i = 0; i < glucoseGraphObjects.size(); i++) {
            glucoseReadings.add(glucoseGraphObjects.get(i).getReading());
        }

        return glucoseReadings;
    }

    public ArrayList<String> getGraphGlucoseDateTime() {
        ArrayList<String> glucoseDatetime = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yy");

        for (int i = 0; i < glucoseGraphObjects.size(); i++) {
            glucoseDatetime.add(dateTimeFormatter.print(glucoseGraphObjects.get(i).getCreated()));
        }
        return glucoseDatetime;
    }

    private List<GlucoseGraphObject> generateGlucoseGraphPoints() {
        final ArrayList<GlucoseGraphObject> finalGraphObjects = new ArrayList<>();
        DateTime minDateTime = DateTime.now().minusMonths(1).minusDays(15);
        final List<Glucose> glucoseReadings = getRealmResults();

        /*Collections.sort(glucoseRealmResults, new Comparator<Glucose>() {
            public int compare(Glucose o1, Glucose o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });*/

        DateTime startDate = glucoseReadings.size() > 0 ?
                minDateTime : DateTime.now();
        // Transfer values from database to ArrayList as GlucoseGraphObjects
        for (int i = 0; i < glucoseReadings.size(); i++) {
            final Glucose reading = glucoseReadings.get(i);
            final DateTime createdDate = new DateTime(reading.getCreatedAt());
            //add zero values between current value and last added value
            addZeroReadings(finalGraphObjects, startDate, createdDate);
            //add new value
            int read = reading.getGlucoseLevel().intValue();
            finalGraphObjects.add(
                    new GlucoseGraphObject(createdDate, reading.getGlucoseLevel().intValue())
            );
            //update start date
            startDate = createdDate;
        }
        //add last zeros till now
        addZeroReadings(finalGraphObjects, startDate, DateTime.now());

        return finalGraphObjects;
    }

    private void addZeroReadings(final ArrayList<GlucoseGraphObject> graphObjects,
                                 final DateTime firstDate,
                                 final DateTime lastDate) {
        int daysBetween = Days.daysBetween(firstDate, lastDate).getDays();
        for (int i = 1; i < daysBetween; i++) {
            graphObjects.add(new GlucoseGraphObject(firstDate.plusDays(i), 0));
        }
    }

    private void setData() {
        LineData data = new LineData();
        data = generateData();
        chart.setData(data);

        chart.setPinchZoom(true);
        chart.setHardwareAccelerationEnabled(true);
        //chart.setNoDataTextColor(getResources().getColor(R.color.glucosio_text));
        chart.animateY(1000, Easing.EasingOption.EaseOutCubic);
        chart.invalidate();
        chart.notifyDataSetChanged();
        chart.fitScreen();
        chart.setDescription(null);
        chart.setVisibleXRangeMaximum(20);
        //chart.moveViewToX(data.getXValCount());
    }

    private void exportGraphToGallery() {
        long timestamp = System.currentTimeMillis() / 1000;
        boolean saved = chart.saveToGallery("diabetes_" + timestamp, 50);
        if (saved) {
            Snackbar.make(v, R.string.export, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(v, R.string.not_export, Snackbar.LENGTH_SHORT).show();
        }
    }

}
