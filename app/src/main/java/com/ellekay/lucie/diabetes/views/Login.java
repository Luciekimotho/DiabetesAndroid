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
import android.widget.EditText;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.User;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Context mContext;
    String TAG = "login";
    EditText etEmail, etPassword;
    String email, password;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        signup = (Button) findViewById(R.id.btn_signin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
    }

    public void logIn(View view){
        email = etEmail.getText().toString();
        password = etPassword.getText().toString().trim();

//        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
//        apiClient.login(email, password).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d(TAG,"Login successful");
//                startActivity(new Intent(getApplicationContext(),UserProfile.class));
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG,"Login not succesful"+t.getMessage());
//            }
//        });

        startActivity(new Intent(getApplicationContext(),UserProfile.class));
    }

}
