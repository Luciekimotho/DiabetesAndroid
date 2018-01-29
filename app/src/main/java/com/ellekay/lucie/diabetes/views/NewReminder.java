package com.ellekay.lucie.diabetes.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Reminder;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReminder extends AppCompatActivity {
    Button addRem;
    EditText etRem, etTime;
    Switch swAlarm;
    String reminder, time;
    Boolean alarm;
    Context mContext;
    String TAG = "Reminder";
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        addRem = (Button) findViewById(R.id.btn_add_rem);

        etRem = (EditText) findViewById(R.id.et_reminder);
        etTime = (EditText) findViewById(R.id.et_time);

        swAlarm = (Switch) findViewById(R.id.switch_alarm);

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        swAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    alarm = Boolean.TRUE;
                }else {
                    alarm = Boolean.FALSE;
                }
            }
        });


        addRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = 1;
                reminder = etRem.getText().toString();
                time = etTime.getText().toString();

                ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
                apiClient.newReminder(reminder,time, alarm,userId).enqueue(new Callback<Reminder>() {
                    @Override
                    public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "New reminder: record added");
                            Log.d(TAG,response.toString());
                        }else {
                            Log.d(TAG,"New reminder not succesful: "+ response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Reminder> call, Throwable t) {
                        Log.d(TAG,"Reminder: after reading function" + t);

                    }
                });
            }
        });
    }
}
