package com.ellekay.lucie.diabetes.views;

/**
 * Created by lucie on 9/27/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.PrefManager;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    PrefManager prefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }
        Log.d("Sharedpref", String.valueOf(prefManager.isFirstTimeLaunch()));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.background3)
                .image(R.drawable.gluco)
                .title("Measure and monitor your glucose levels")
                .description("")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.background3)
                .image(R.drawable.food)
                .title("Set reminders and get notifications ")
                .description("Receive daily reminders")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.background3)
                .image(R.drawable.meds)
                .title("Manage your diabetes better.")
                .description("")
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
//        Intent i = new Intent(IntroActivity.this, SignUp.class);
//        startActivity(i);
       launchHomeScreen();
    }



    public void launchHomeScreen(){
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, Home.class));
        finish();
    }
}

