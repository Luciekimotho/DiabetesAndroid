package com.ellekay.lucie.diabetes.views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.auth.SessionManagement;
import com.ellekay.lucie.diabetes.auth.SignUp;
import com.ellekay.lucie.diabetes.models.Patient;
import com.ellekay.lucie.diabetes.models.PatientsResponse;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.User;
import com.ellekay.lucie.diabetes.rest.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {
    SessionManagement session;
    String TAG = "Profile";
    EditText et_dob, et_height, et_weight, et_sex, et_location;
    String uname, email, password;
    String dob, sex, location;
    Integer height, weight;
    Context mContext;
    private User user = new User();
    String name;
    Integer id;
    Date date2;
    private List<Patient> patientList = new ArrayList<>();
    ProgressDialog progressDialog;
    Bundle extras;

    public static final String KEY_UNAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManagement(getApplicationContext());
        //session.checkLogin();
        //session.checkProfileFilled();

        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.KEY_NAME);
        id = Integer.valueOf(user.get(SessionManagement.KEY_ID));

        extras = getIntent().getExtras();

        uname = String.valueOf(extras.getSerializable(KEY_UNAME));
        email = (String) extras.getSerializable(KEY_EMAIL);
        password = (String) extras.getSerializable(KEY_PASS);

        Log.d(TAG,"Logged in user: "+ name + "id: "+id + uname + email + password);

        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_location = (EditText) findViewById(R.id.et_location);

        et_dob = (EditText) findViewById(R.id.et_dob);
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                Date date2 = calendar.getTime();
                DatePickerDialog datePickerDialog;

                datePickerDialog = new DatePickerDialog(UserProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        et_dob.setText( selectedYear + "/" + selectedMonth);
                    }
                }, year, month, day);//Yes 24 hour time
                datePickerDialog.setTitle("Select Time");
                datePickerDialog.show();
            }
        });

        getPatient();
    }

//    public void getUserDetails(){
//        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
//        apiClient.getUser(id).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()){
//                    user = response.body();
//                    Log.d(TAG, "User:  "+response.body());
//                    update(user);
//                }else {
//                    Log.d(TAG, "Error "+response.toString());
//                }
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, ""+t.getMessage());
//            }
//        });
//    }

    public void userProfileSubmit(View view){
        progressDialog = ProgressDialog.show(UserProfile.this,
                "ProgressDialog",
                "Wait!");
        progressDialog.setCanceledOnTouchOutside(true);

        height = Integer.valueOf(et_height.getText().toString());
        weight = Integer.valueOf(et_weight.getText().toString());
        location = et_location.getText().toString();
        sex = "Female";

        final JsonObject jsonUser = new JsonObject();
        final JsonObject json = new JsonObject();
        jsonUser.addProperty("username", uname);
        jsonUser.addProperty("email", email);
        jsonUser.addProperty("password", password);

        json.add("user", new Gson().toJsonTree(jsonUser));
        json.addProperty("dateOfBirth", "1997-11-15T05:00:32Z");
        json.addProperty("height", height);
        json.addProperty("weight", weight);
        json.addProperty("sex", "FEMALE");
        json.addProperty("location", location);

        Log.d(TAG, "Json result"+ json);

        Ion.with(getApplicationContext())
                .load("https://backend-diabetes.herokuapp.com/patient/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(TAG, "Profile create: "+ result);
                        // do stuff with the result or error
                        if (result != null){
                            progressDialog.dismiss();
                            startActivity(new Intent(UserProfile.this, Home.class));
                        }else {
                            Log.d(TAG, "Error: "+e.toString());
                        }
                    }
                });

        final Calendar calendar = Calendar.getInstance();
        date2 = calendar.getTime();
        String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            Date newDate = sdf.parse(date2.toString());
            String format2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
            Log.d(TAG,"Formatted date: "+ sdf2.format(newDate));
            dob = sdf2.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Retrofit", ""+e);
        }
        height = Integer.valueOf(et_height.getText().toString());
        weight = Integer.valueOf(et_weight.getText().toString());
        location = et_location.getText().toString();
        sex = "Female";
    }

    private void update(User user){
        Patient patient = new Patient();
        patient.setId(4);
        patient.setUser(user);
        patient.setDateOfBirth(dob);
        patient.setHeight(height);
        patient.setWeight(weight);
        patient.setSex(sex);
        patient.setLocation(location);
        Log.d(TAG, "Patient is: "+patient);

        final JsonObject json = new JsonObject();
        json.addProperty("dateOfBirth", dob);
        json.addProperty("height", height);
        json.addProperty("weight", weight);
        json.addProperty("sex", sex);
        json.addProperty("location", location);
        Log.d(TAG, "json details is: "+json);

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        Log.d(TAG, ""+user + date2 + height + weight + sex + location);
        apiClient.updatePatient(json).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Patient updated");
                    Log.d(TAG,response.toString());
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }else {
                    Log.d(TAG,"Patient update error: "+ response.message());
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.d(TAG,"Patient update fail: " + t.getCause());
            }
        });
    }

    private void getPatient(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getPatients().enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,"Get patients" +response.body());
                    patientList = response.body();
                }else {
                    Log.d(TAG,"Get all patients" +response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.d(TAG,"Get patients fail" +t.toString());
            }
        });
//        apiClient.getPatient(String.valueOf(id)).enqueue(new Callback<Patient>() {
//            @Override
//            public void onResponse(Call<Patient> call, Response<Patient> response) {
//                if (response.isSuccessful()) {
////                    patient = response.body();
////                    profList.setText(patient.toString());
//                    Log.d(TAG,"" +response.body());
//                }else {
//                    //error
//                    Log.d("Retrofit","Response not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Patient> call, Throwable t) {
//
//            }
//        });
    }

//    public void getUsers(){
//        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
//        apiClient.getUsers().enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                if (response.isSuccessful()){
//
//                    Log.d(TAG, "Users:  "+response.body());
//                }else {
//                    Log.d(TAG, "Error "+response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//
//            }
//        });
//    }

}
