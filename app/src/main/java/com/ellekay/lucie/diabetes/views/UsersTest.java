package com.ellekay.lucie.diabetes.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Profile;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersTest extends AppCompatActivity  {
    private UsersTest mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertest);

        Button one = (Button) findViewById(R.id.one_user);
        Button all = (Button) findViewById(R.id.all_users);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView profList = (TextView) findViewById(R.id.profilelist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
                apiClient.getUsers().enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                        if (response.isSuccessful()) {
                            List<Profile> profileList = response.body();
                            profList.setText(profileList.toString());
                            Log.d("Retrofit","Response successful");

                        }else {
                            //error
                            Log.d("Retrofit","Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Profile>> call, Throwable t) {

                    }
                });
            }
        });

    }

    public void oneUser(View view){
        final TextView profList = (TextView) findViewById(R.id.profilelist);
        TextView userid = (TextView) findViewById(R.id.userId);

        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getUser(userid.getText().toString().trim()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    Profile profile = response.body();
                    profList.setText(profile.toString());
                    Log.d("Retrofit","Response successful");

                }else {
                    //error
                    Log.d("Retrofit","Response not successful");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }

    public void toReadings(View view){
        startActivity(new Intent(UsersTest.this, NewReading.class));
    }


}
