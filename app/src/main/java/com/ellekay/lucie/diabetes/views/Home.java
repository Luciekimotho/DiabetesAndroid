package com.ellekay.lucie.diabetes.views;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.auth.Login;
import com.ellekay.lucie.diabetes.auth.SessionManagement;
import com.ellekay.lucie.diabetes.fragments.HistoryFragement;
import com.ellekay.lucie.diabetes.fragments.OverviewFragment;
import com.ellekay.lucie.diabetes.fragments.ReminderFragment;

import java.util.HashMap;

public class Home extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    BottomNavigationView navigation;
    View navheader;
    TextView username, tvemail;
    String user, email;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
//        name = user.get(SessionManagement.KEY_NAME);
//        id = Integer.valueOf(user.get(SessionManagement.KEY_ID));
//        Log.d(TAG,"Logged in user: "+ name + "id: "+id);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navheader = navigationView.getHeaderView(0);
        username = (TextView) navheader.findViewById(R.id.name);
        tvemail = (TextView) navheader.findViewById(R.id.email);
        username.setText("User");
        tvemail.setText("Email address");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getIntExtra("fragmentNumber", 0) == 3){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, ReminderFragment.newInstance());
            transaction.commit();
        }else if (getIntent().getIntExtra("fragmentNumber", 0) == 2){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, HistoryFragement.newInstance());
            transaction.commit();
        }else {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, OverviewFragment.newInstance());
            //getSupportActionBar().setTitle("Overview");
            transaction.commit();
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        mHandler = new Handler();
        setUpDrawerContent(navigationView);

        Log.d("Reminder intent", ""+ getIntent().getIntExtra("fragmentNumber",0) );
    }

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

    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_myaccount:
                        startActivity(new Intent(Home.this,UserProfile.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_doctors:
                        startActivity(new Intent(Home.this,DoctorActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_export:
                        break;
                    case R.id.nav_emergency:
                        startActivity(new Intent(Home.this,CaregiverActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_settings:
                        break;
                    case R.id.nav_logout:
                        startActivity(new Intent(Home.this,Login.class));
                        drawer.closeDrawers();
                        return true;
                }
                return true;
            }
        });

    }
}
