package com.ellekay.lucie.diabetes.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.db.DatabaseResults;
import com.ellekay.lucie.diabetes.fragments.ReminderFragment;
import com.ellekay.lucie.diabetes.models.Reminder;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReminder extends AppCompatActivity {
    Button addRem;
    EditText etRem, etTime, etDate;
    Switch swAlarm;
    String reminder, time;
    Boolean alarm;
    Context mContext;
    String TAG = "Reminder";
    int userId;
    Date date, editedDate;
    String stringDate;
    DatabaseResults db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        db = new DatabaseResults(getApplicationContext());
        addRem = (Button) findViewById(R.id.btn_add_rem);
        etRem = (EditText) findViewById(R.id.et_reminder);
        etTime = (EditText) findViewById(R.id.et_time);
        etDate = (EditText) findViewById(R.id.et_date);
        swAlarm = (Switch) findViewById(R.id.switch_alarm);

        Calendar mcurrentTime = Calendar.getInstance();
        date = mcurrentTime.getTime();

        String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Log.d(TAG, date.toString());

        try {
            Date newDate = sdf.parse(date.toString());
            String format2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
            Log.d(TAG,"Formatted date: "+ sdf2.format(newDate));
            stringDate = sdf2.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Retrofit", ""+e);
        }

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                Date date = mcurrentTime.getTime();
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(NewReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //Log.d(TAG,"Time time"+ date);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                Date date2 = calendar.getTime();
                DatePickerDialog datePickerDialog;

                datePickerDialog = new DatePickerDialog(NewReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        etDate.setText( selectedYear + "/" + selectedMonth);
                    }
                }, year, month, day);//Yes 24 hour time
                datePickerDialog.setTitle("Select Time");
                datePickerDialog.show();
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
                Log.d(TAG, reminder + date + alarm.toString());

                ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
                apiClient.newReminder(reminder, stringDate, alarm, userId).enqueue(new Callback<Reminder>() {
                    @Override
                    public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "New reminder: record added");
                            Log.d(TAG,response.toString());
                        }else {
                            Log.d(TAG,"New reminder not msg: "+ response.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<Reminder> call, Throwable t) {
                        Log.d(TAG,"Reminder: after reading function" + t.getCause());
                    }
                });
                //db.addNewRem(reminder, time, alarm, userId);

                Intent intent = new Intent(NewReminder.this, Home.class);
                intent.putExtra("fragmentNumber", 3);
                startActivity(intent);
            }
        });
    }
}
