package com.ellekay.lucie.diabetes.views;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.fragments.HistoryFragement;
import com.ellekay.lucie.diabetes.fragments.OverviewFragment;
import com.ellekay.lucie.diabetes.fragments.TrendsFragment;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        String title;
        Fragment fragment = null;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    //mTextMessage.setText(R.string.overview);
                    fragment = OverviewFragment.newInstance();
                    title = "Overview";

                    break;
                case R.id.navigation_reminders:
                    //mTextMessage.setText(R.string.trends);
                    fragment = TrendsFragment.newInstance();
                    title = "Reminders";
                    break;
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_activity_history);
                    fragment = HistoryFragement.newInstance();
                    title = "History";
                    break;
            }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
                // set the toolbar title
                getSupportActionBar().setTitle(title);

            return true;
        }
    };

//    //Manually displaying the first fragment - one time only
//    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content, OverviewFragment.newInstance());
//        transaction.commit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, OverviewFragment.newInstance());
        getSupportActionBar().setTitle("Overview");
        transaction.commit();

    }

}
