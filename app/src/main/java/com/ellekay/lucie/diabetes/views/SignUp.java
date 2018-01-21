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

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.User;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    EditText etname, etemail, etpassword;
    Button login;
    String name, email, password;
    Context mContext;
    String TAG = "signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        etname = (EditText) findViewById(R.id.et_name);
        etemail = (EditText) findViewById(R.id.et_email);
        etpassword = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.btnLogInActivity);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    public void signUp(View view){
//        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
//        apiClient.createUser(name, email, password).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d(TAG,"create user successful");
//                startActivity(new Intent(getApplicationContext(),Login.class));
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG,"create user not successful");
//            }
//        });
        startActivity(new Intent(getApplicationContext(),UserProfile.class));


    }

}
