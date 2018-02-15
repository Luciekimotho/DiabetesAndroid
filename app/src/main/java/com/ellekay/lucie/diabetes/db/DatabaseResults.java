package com.ellekay.lucie.diabetes.db;

import android.content.Context;
import android.util.Log;
import com.ellekay.lucie.diabetes.models.Caregiver;
import com.ellekay.lucie.diabetes.models.CaregiverRealm;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Patient;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.models.Reminder;
import com.ellekay.lucie.diabetes.models.ReminderRealm;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucie on 2/4/2018.
 */

public class DatabaseResults {
    private static RealmConfiguration mRealmConfig;
    private Context mContext;
    private Realm realm;

    public Patient patient;
    public List<Patient> patientList = new ArrayList<>();
    public Readings reading;
    public List<Readings> readingList = new ArrayList<>();
    public Doctor doctor;
    public List<Doctor> doctorList = new ArrayList<>();
    public List<Caregiver> caregiverList = new ArrayList<>();
    public List<Reminder> reminderList = new ArrayList<>();
    final List<Reminder> finalRem = new ArrayList<>();

    Integer glucoReading, userId;
    String timePeriod, notes, time, reminder;
    Boolean alarm;

    String TAG = "Database handler";

    public DatabaseResults(Context context) {
        this.mContext = context;
        this.realm = getNewRealmInstance();
    }

    public Realm getNewRealmInstance() {
        if (mRealmConfig == null) {
            mRealmConfig = new RealmConfiguration
                    .Builder(mContext)
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }
        return Realm.getInstance(mRealmConfig); // Automatically run migration if needed
    }
    public Realm getRealmInstance() {
        return realm;
    }

    /*Patients*/
    public Patient getPatientResults(int pid){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getPatient(String.valueOf(pid)).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    patient = response.body();
                }else {
                    Log.d(TAG,"Error: "+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.d(TAG,"Failure: "+t.toString());
            }
        });
        return patient;
    }

    public List<Patient> getAllPatientsResults(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getPatients().enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {
                    patientList = response.body();
                    Log.d("Patients", patientList.toString());
                }else {
                    Log.d(TAG,"Error: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.d(TAG,"Failure: "+t.toString());
            }
        });
        return patientList;

    }

    /*Glucose readings*/
    public List<Readings> getAllReadings(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReadings().enqueue(new Callback<List<Readings>>() {
            @Override
            public void onResponse(Call<List<Readings>> call, Response<List<Readings>> response) {
                if (response.isSuccessful()) {
                    readingList = response.body();
                    readingsRealmWrite(readingList);

                }else {
                    Log.d(TAG,"Retrofit: Response not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Readings>> call, Throwable t) {
                Log.d(TAG,"Retrofit: after reading function" + t);
            }
        });
        return readingList;
    }

    public void newReading(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.newReading(glucoReading, timePeriod, notes, userId).enqueue(new Callback<Readings>() {
            @Override
            public void onResponse(Call<Readings> call, Response<Readings> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"Response successful: record added");
                }else {
                    //error
                    Log.d(TAG,"Error: "+ response.toString());
                }
            }
            @Override
            public void onFailure(Call<Readings> call, Throwable t) {
                Log.d(TAG,"Failure: " + t);
            }
        });
    }

    public void readingsRealmWrite(final List<Readings> glucoseReading){
        realm = getNewRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
               @Override
               public void execute(Realm realm) {
                   for(int i =0; i < glucoseReading.size(); i++){
                       Glucose glucose = realm.createObject(Glucose.class);
                       glucose.setId(glucoseReading.get(i).getId());
                       glucose.setGlucoseLevel(glucoseReading.get(i).getGlucoseLevel());
                       glucose.setTimeOfDay(glucoseReading.get(i).getTimeOfDay());
                       glucose.setTimePeriod(glucoseReading.get(i).getTimePeriod());
                       glucose.setAction(glucoseReading.get(i).getAction());
                       glucose.setMedication(glucoseReading.get(i).getMedication());
                       glucose.setNotes(glucoseReading.get(i).getNotes());
                       glucose.setCreatedAt(glucoseReading.get(i).getCreatedAt());
                       glucose.setUpdatedAt(glucoseReading.get(i).getUpdatedAt());
                       glucose.setUser(glucoseReading.get(i).getUser());
                   }
               }

           }, new Realm.Transaction.OnSuccess(){

               @Override
               public void onSuccess() {
                   Log.d("Realm","savedRealmObjects");
               }
           },new Realm.Transaction.OnError(){
               @Override
               public void onError(Throwable error) {
                   Log.d("Realm","Error: " +error.getMessage());
               }
           }
        );
        Log.d(TAG,"Realm size is:" + glucoseReading.size());
    }

    /*Doctors*/
    public List<Doctor> getDoctorResults(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()){
                    response.body();
                    doctorList = response.body();
                    //executeRealmWrite(doctorList);

                    Log.d(TAG,""+ doctorList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
        return doctorList;
    }

    private void doctorRealmWrite(final List<Doctor> doctorReadings){
        realm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
               for (int i=0; i<doctorReadings.size(); i++){
                   DoctorRealm doctor = realm.createObject(DoctorRealm.class);
                   doctor.setId(doctorReadings.get(i).getId());
                   doctor.setName(doctorReadings.get(i).getName());
                   doctor.setEmail(doctorReadings.get(i).getEmail());
                   doctor.setPhone(doctorReadings.get(i).getPhone());
                   doctor.setNotes(doctorReadings.get(i).getNotes());
                   Log.d(TAG, doctor.toString());
               }

           }
       }, new Realm.Transaction.OnSuccess(){
           @Override
           public void onSuccess() {
               Log.d(TAG,"savedRealmObjects");
           }
       },new Realm.Transaction.OnError(){
           @Override
           public void onError(Throwable error) {
               Log.d(TAG,"Not saving error: " +error.toString());
           }
       }
        );
        Log.d(TAG,"Doctor size is:" + doctorReadings.size());
    }

    /*Caregivers*/
    public List<Caregiver> getCaregiverResults(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getCaregivers().enqueue(new Callback<List<Caregiver>>() {
            @Override
            public void onResponse(Call<List<Caregiver>> call, Response<List<Caregiver>> response) {
                if (response.isSuccessful()){
                    response.body();
                    caregiverList = response.body();
                    //executeRealmWrite(caregiverList);
                    Log.d(TAG,""+ caregiverList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Caregiver>> call, Throwable t) {
                Log.d(TAG, ""+t.getMessage());
            }
        });
        return caregiverList;
    }

    private void caregiverRealmWrite(final List<Caregiver> caregiverReadings){
        realm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
                                               for (int i=0; i<caregiverReadings.size(); i++){
                                                   CaregiverRealm caregiver = realm.createObject(CaregiverRealm.class);
                                                   caregiver.setId(caregiverReadings.get(i).getId());
                                                   caregiver.setName(caregiverReadings.get(i).getName());
                                                   caregiver.setEmail(caregiverReadings.get(i).getEmail());
                                                   caregiver.setPhone(caregiverReadings.get(i).getPhone());
                                                   //doctor.setNotes(caregiverReadings.get(i).getNotes());
                                                   Log.d(TAG, caregiver.toString());
                                               }

                                           }
                                       }, new Realm.Transaction.OnSuccess(){
                                           @Override
                                           public void onSuccess() {
                                               Log.d(TAG,"savedRealmObjects");
                                           }
                                       },new Realm.Transaction.OnError(){
                                           @Override
                                           public void onError(Throwable error) {
                                               Log.d(TAG,"Not saving error: " +error.toString());
                                           }
                                       }
        );
        Log.d(TAG,"Caregiver size is:" + caregiverReadings.size());
    }

    /*Reminders*/
    public List<Reminder> getReminderList(){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getReminders().enqueue(new Callback<List<Reminder>>() {
            @Override
            public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                if (response.isSuccessful()){
                    response.body();
                    reminderList = response.body();
                    Log.d(TAG,""+ reminderList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Reminder>> call, Throwable t) {
                Log.d(TAG, ""+t.getMessage());
            }
        });
        Log.d(TAG, "before return: "+readingList);
        return reminderList;
    }

//    public void addNewRem(String reminder, String time, Boolean alarm, Integer userId){
//        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
//        apiClient.newReminder(reminder,time, alarm,userId).enqueue(new Callback<Reminder>() {
//            @Override
//            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "New reminder: record added");
//                    Log.d(TAG,response.toString());
//                }else {
//                    Log.d(TAG,"New reminder not succesful: "+ response.raw());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Reminder> call, Throwable t) {
//                Log.d(TAG,"Reminder: after reading function" + t);
//            }
//        });
//    }

    private void reminderRealmExecute(final List<Reminder> reminders){
        realm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
               for(int i =0; i < reminders.size(); i++){
                   ReminderRealm reminder = realm.createObject(ReminderRealm.class);
                   reminder.setId(reminders.get(i).getId());
                   reminder.setReminder(reminders.get(i).getReminder());
                   reminder.setAlarm(reminders.get(i).getAlarm());
                   reminder.setTime(reminders.get(i).getTime());
                   reminder.setUser(reminders.get(i).getUser());
               }
           }

       }, new Realm.Transaction.OnSuccess(){

           @Override
           public void onSuccess() {
               Log.d("Realm","savedRealmObjects");
           }
       },new Realm.Transaction.OnError(){
           @Override
           public void onError(Throwable error) {
               Log.d("Realm","Error: " +error.getMessage());
           }
       }
        );
        Log.d(TAG,"Realm size is:" + reminders.size());
    }

    public ReminderRealm getReminder(long id) {
        return realm.where(ReminderRealm.class)
                .equalTo("id", id)
                .findFirst();
    }

    public List<ReminderRealm> getReminders() {
        RealmResults<ReminderRealm> results =
                realm.where(ReminderRealm.class)
                        .findAllSorted("id", Sort.DESCENDING);
        List<ReminderRealm> reminders = new ArrayList<>(results.size());
        for (int i = 0; i < results.size(); i++) {
            reminders.add(results.get(i));
        }
        return reminders;
    }

    public boolean addReminder(ReminderRealm reminder) {
        // Check for duplicates first
        if (getReminder(reminder.getId()) == null) {
            realm.beginTransaction();
            realm.copyToRealm(reminder);
            realm.commitTransaction();
            return true;
        }

        return false;
    }

}
