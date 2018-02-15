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
import com.ellekay.lucie.diabetes.views.UserProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    EditText etname, etemail, etpassword;
    Button login;
    String name, email, password;
    Context mContext;
    String TAG = "signup";
    SessionManagement session;
    ProgressDialog progressDialog;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        session = new SessionManagement(getApplicationContext());

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
        name = etname.getText().toString();
        email = etemail.getText().toString();
        password = etpassword.getText().toString().trim();

        session.createPatientSession(name,email,password);

        Intent intent = new Intent(SignUp.this, UserProfile.class);
        intent.putExtra(UserProfile.KEY_UNAME, name );
        intent.putExtra(UserProfile.KEY_EMAIL, email);
        intent.putExtra(UserProfile.KEY_PASS, password);
        startActivity(intent);

        //startActivity(new Intent(SignUp.this,UserProfile.class));

        progressDialog = ProgressDialog.show(SignUp.this,
                "ProgressDialog",
                "Wait!");
        progressDialog.setCanceledOnTouchOutside(true);

    }

}
