package com.ellekay.lucie.diabetes.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.auth.Login;
import com.ellekay.lucie.diabetes.fragments.HistoryFragement;
import com.ellekay.lucie.diabetes.fragments.OverviewFragment;
import com.ellekay.lucie.diabetes.fragments.ReminderFragment;

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
                    fragment = ReminderFragment.newInstance();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_doctor:
                startActivity(new Intent(this,DoctorActivity.class));
                return true;
            case R.id.navigation_caregiver:
                startActivity(new Intent(this, CaregiverActivity.class));
                return true;
            case R.id.navigation_logout:
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
