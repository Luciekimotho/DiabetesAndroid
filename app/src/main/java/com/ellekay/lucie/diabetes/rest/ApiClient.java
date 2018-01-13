package com.ellekay.lucie.diabetes.rest;

/**
 * Created by lucie on 9/27/2017.
 */

import android.content.Context;

import com.ellekay.lucie.diabetes.models.Caregiver;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.Medication;
import com.ellekay.lucie.diabetes.models.Profile;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;

import com.ellekay.lucie.diabetes.models.Profile;
import com.ellekay.lucie.diabetes.models.Readings;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by lucie on 9/26/2017.
 */

public interface ApiClient {

    String API_URL = "https://diabetesapi.herokuapp.com/";

    @GET("users")
    Call<List<Profile>> getUsers();

    @GET("user/{id}")
    Call<Profile> getUser(@Path("id") String id);

    @FormUrlEncoded
    @POST("users/new")
    Call<Profile> createUser(@Field("email") String email,@Field("password") String password );

    @GET("readings")
    Call<List<Readings>> getReadings();

    @GET("reading/{id}")
    Call<Readings> getReading(@Path("id") String id);

    @GET("reminders")
    Call<List<Reminder>> getReminders();

    @GET("reminder/{id}")
    Call<Reminder> getReminder(@Path("id") String id);

    @GET("caregivers")
    Call<List<Caregiver>> getCaregivers();

    @GET("caregiver/{id}")
    Call<Caregiver> getCaregiver(@Path("id") String id);

    @GET("doctors")
    Call<List<Doctor>> getDoctors();

    @GET("doctor/{id}")
    Call<Doctor> getDoctor(@Path("id") String id);

//    @GET("medication")
//    Call<List<Medication>> getMedications();
//
//    @GET("medication/{id}")
//    Call<Medication> getMedication(@Path("id") String id);

    @FormUrlEncoded
    @POST("reading/")
    Call<Readings> newReading(@Field("glucoseLevel") Integer glucoseLevel, @Field("timePeriod") String timePeriod,
                              @Field("action") String action, @Field("medication") String medication,
                              @Field("notes") String notes, @Field("user") Integer user);

    class Factory{

        private static ApiClient service;
        public static ApiClient getInstance(Context context){

            if (service ==null){
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                service = retrofit.create(ApiClient.class);
                return service;

            }else {

                return service;
            }
        }
    }

}
