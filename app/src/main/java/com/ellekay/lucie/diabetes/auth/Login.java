package com.ellekay.lucie.diabetes.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.views.NewReading;
import com.ellekay.lucie.diabetes.views.UserProfile;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    Context mContext;
    String TAG = "login";
    EditText etEmail, etPassword;
    String email, password;
    Integer id;
    Button signup;
    SessionManagement session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        session = new SessionManagement(getApplicationContext());

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

        progressDialog = ProgressDialog.show(Login.this,
                "ProgressDialog",
                "Wait!");
        progressDialog.setCanceledOnTouchOutside(true);

        final JsonObject json = new JsonObject();
        json.addProperty("username", email);
        json.addProperty("password", password);

        Ion.with(getApplicationContext())
                .load("https://backend-diabetes.herokuapp.com/api-token-auth/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (result != null){

                            if (result.has("token")){
                                String token = result.get("token").toString();
                                id = result.get("id").getAsInt();
                                Log.d(TAG, "res: "+ token);

                                session.createLoginSession(email, id);
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),UserProfile.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this,
                                        "Login failed! ",
                                        Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Log.d(TAG, "Error: "+e.toString());
                        }
                    }
                });

    }

}
