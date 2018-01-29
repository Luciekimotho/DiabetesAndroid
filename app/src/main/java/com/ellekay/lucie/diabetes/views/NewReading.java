package com.ellekay.lucie.diabetes.views;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReading extends AppCompatActivity {

    EditText et_glucoreading, et_time, et_timeperiod, et_action, et_medication, et_notes;
    Spinner sp_time;
    public Integer glucoReading;
    public String date, timePeriod, action, medication, notes;
    public Integer userId;
    Button btnMeasure;
    MeasureTask myprogresstask;
    private NewReading mContext;
    TextView profList;
    String TAG = "Reading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_readings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profList = (TextView) findViewById(R.id.profilelist);

        et_glucoreading = (EditText)findViewById(R.id.et_glucosereading);
        et_time = (EditText) findViewById(R.id.et_time);
        //et_timeperiod = (EditText) findViewById(R.id.et_timeperiod);
//        et_action = (EditText)findViewById(R.id.et_action);
//        et_medication = (EditText)findViewById(R.id.et_medication);
        et_notes = (EditText) findViewById(R.id.et_notes);

        sp_time = (Spinner) findViewById(R.id.spinner);

        et_notes.setMinLines(2);

        btnMeasure = (Button) findViewById(R.id.btn_measure);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewReading.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myprogresstask = new MeasureTask();
                myprogresstask.execute();
            }
        });


        List<String> timePeriods = new ArrayList<>();
        timePeriods.add("Before breakfast");
        timePeriods.add("After breakfast");
        timePeriods.add("Before lunch");
        timePeriods.add("After lunch");
        timePeriods.add("Before supper");
        timePeriods.add("After supper");
        timePeriods.add("Bedtime");

        ArrayAdapter<String> timePeriodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timePeriods);
        timePeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_time.setAdapter(timePeriodAdapter);

        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timePeriod = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + timePeriod, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void takeMeasurement() {
        date = et_time.getText().toString();
        //timePeriod = et_timeperiod.getText().toString();
        glucoReading = Integer.valueOf(et_glucoreading.getText().toString());
//        action = et_action.getText().toString();
//        medication = et_action.getText().toString();
        notes = et_notes.getText().toString();
        userId = 1;

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.newReading(glucoReading, timePeriod, notes, userId).enqueue(new Callback<Readings>() {
            @Override
            public void onResponse(Call<Readings> call, Response<Readings> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"Retrofit Response successful: record added");
                }else {
                    //error
                    Log.d(TAG,"Retrofit Response not successful: "+ response.toString());
                }
            }
            @Override
            public void onFailure(Call<Readings> call, Throwable t) {
                Log.d(TAG,"Reading: after reading function" + t);
            }
        });
    }

    public class MeasureTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... params) {
            takeMeasurement();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewReading.this,
                    "ProgressDialog",
                    "Wait!");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });

            Toast.makeText(NewReading.this,
                    "Progress Start",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(NewReading.this,
                    "Progress Ended",
                    Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

            startActivity(new Intent(NewReading.this, Home.class));
        }
    }

}
