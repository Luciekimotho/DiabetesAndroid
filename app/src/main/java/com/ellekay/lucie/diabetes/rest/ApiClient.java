package com.ellekay.lucie.diabetes.rest;

/**
 * Created by lucie on 9/27/2017.
 */

import android.content.Context;

import com.ellekay.lucie.diabetes.models.Caregiver;
import com.ellekay.lucie.diabetes.models.Chat;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.Patient;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;

import com.ellekay.lucie.diabetes.models.User;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by lucie on 9/26/2017.
 */

public interface ApiClient {

    //String API_URL = "https://diabetesapi.herokuapp.com/";
    String API_URL = "https://backend-diabetes.herokuapp.com/";


    @FormUrlEncoded
    @POST("user/")
    Call<User> createUser(@Field("name") String name,@Field("email") String email, @Field("password") String password );

    @GET("user/{id}")
    Call<User> getUser(@Path("id") Integer id);

    @GET("users")
    Call<List<User>> getUsers();

    @FormUrlEncoded
    @POST("patient/")
    Call<Patient> createPatient(@Field("username") String username,@Field("email") String email, @Field("password") String password,
                                @Field("dateOfBirth") String dateOfBirth,@Field("height") Integer height, @Field("weight") Integer weight,
                                @Field("location") String location,@Field("sex") String sex );

    @PATCH("patient/4/")
    Call<Patient> updatePatient(@Body JsonObject json );

    @GET("patients")
    Call<List<Patient>> getPatients();

    @GET("patient/{id}")
    Call<Patient> getPatient(@Path("id") String id);


    @GET("readings")
    Call<List<Readings>> getReadings();

    @GET("reading/{id}")
    Call<Readings> getReading(@Path("id") int id);

    @FormUrlEncoded
    @POST("reading/")
    Call<Readings> newReading(@Field("glucoseLevel") Integer glucoseLevel, @Field("timePeriod") String timePeriod,
                              @Field("notes") String notes, @Field("user") Integer user);

    @GET("doctors")
    Call<List<Doctor>> getDoctors();

    @GET("doctor/{id}")
    Call<Doctor> getDoctor(@Path("id") int id);

    @GET("reminders")
    Call<List<Reminder>> getReminders();

    @GET("reminder/{id}")
    Call<Reminder> getReminder(@Path("id") int id);

    @FormUrlEncoded
    @POST("reminder/")
    Call<Reminder> newReminder(@Field("reminder") String reminder,@Field("time") String time,
                               @Field("alarm") Boolean alarm, @Field("user") Integer user);

    @GET("chats")
    Call<List<Chat>> getChats();

    @GET("chat/{id}")
    Call<Chat> getChat(@Path("id") int id);

    @FormUrlEncoded
    @POST("chat/")
    Call<Chat> newChat(@Field("message") String message, @Field("time") String time,
                       @Field("author") Integer author, @Field("recepient") Integer recepient);

    @GET("caregivers")
    Call<List<Caregiver>> getCaregivers();

    @GET("caregiver/{id}")
    Call<Caregiver> getCaregiver(@Path("id") String id);

    @FormUrlEncoded
    @POST("caregiver/")
    Call<Caregiver> newCaregiver(@Field("relation") String relation, @Field("phone") String phone,
                                 @Field("user") Integer user);

    @FormUrlEncoded
    @POST("doctor/")
    Call<Doctor> newDoctor(@Field("phone") String phone, @Field("user") Integer user);

//    @GET("medication")
//    Call<List<Medication>> getMedications();
//
//    @GET("medication/{id}")
//    Call<Medication> getMedication(@Path("id") String id);

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
