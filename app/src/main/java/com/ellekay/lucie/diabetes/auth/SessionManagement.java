package com.ellekay.lucie.diabetes.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ellekay.lucie.diabetes.auth.Login;
import com.ellekay.lucie.diabetes.views.Home;
import com.ellekay.lucie.diabetes.views.UserProfile;

import java.util.HashMap;

/**
 * Created by lucie on 2/12/2018.
 */

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SessionPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_CREATE_PATIENT = "IsNewPatient";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_UNAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "password";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, Integer id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_ID, id.toString());
        editor.commit();
    }

    public void createPatientSession(String name, String email, String password){
        editor.putBoolean(IS_CREATE_PATIENT, true);

        editor.putString(KEY_UNAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASS, password);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//        // user id
       user.put(KEY_ID, pref.getString(KEY_ID, null));

        // return user
        return user;
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void checkProfileFilled(){
        // Check login status
        if(!this.isCreatePatient()){
            Intent i = new Intent(_context, UserProfile.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }else {
            Intent i = new Intent(_context, Home.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isCreatePatient(){
        return pref.getBoolean(IS_CREATE_PATIENT, false);
    }
}
