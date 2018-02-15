package com.ellekay.lucie.diabetes.db;

import android.content.Context;

import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.ReminderRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lucie on 1/30/2018.
 */

public class DatabaseHandler {
    private static RealmConfiguration mRealmConfig;
    private Context mContext;
    private Realm realm;

    public DatabaseHandler(Context context) {
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

    //Glucose readings
    public ArrayList<Glucose> getGlucoseReadings(Realm realm) {
        RealmResults<Glucose> results =
                realm.where(Glucose.class)
                        .findAllSorted("id", Sort.DESCENDING);
        ArrayList<Glucose> readingList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            readingList.add(results.get(i));
        }
        return readingList;
    }

    public List<Glucose> getGlucoseReadings() {
        RealmResults<Glucose> results =
                realm.where(Glucose.class)
                        .findAllSorted("created", Sort.DESCENDING);
        ArrayList<Glucose> readingList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            readingList.add(results.get(i));
        }
        return readingList;
    }


    //Reminders
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

    public void updateReminder(ReminderRealm reminder) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(reminder);
        realm.commitTransaction();
    }

    public void deleteReminder(ReminderRealm reminder) {
        realm.beginTransaction();
        reminder.deleteFromRealm();
        realm.commitTransaction();
    }

    public void deleteReminder(long id) {
        deleteReminder(getReminder(id));
    }

    public boolean areRemindersActive() {
        RealmResults<ReminderRealm> activeRemindersList =
                realm.where(ReminderRealm.class)
                        .equalTo("alarm", true)
                        .findAll();

        return activeRemindersList.size() > 0;
    }

    public ReminderRealm getReminder(long id) {
        return realm.where(ReminderRealm.class)
                .equalTo("id", id)
                .findFirst();
    }

    public List<ReminderRealm> getReminders() {
        RealmResults<ReminderRealm> results =
                realm.where(ReminderRealm.class)
                        .findAllSorted("alarmTime", Sort.DESCENDING);
        List<ReminderRealm> reminders = new ArrayList<>(results.size());
        for (int i = 0; i < results.size(); i++) {
            reminders.add(results.get(i));
        }
        return reminders;
    }

    public boolean addGlucoseReading(Glucose reading) {
        // generate record Id

        String id = generateIdFromDate(reading.getId());

        // Check for duplicates
        if (getGlucoseReadingById(Integer.parseInt(id)) != null) {
            return false;
        } else {
            realm.beginTransaction();
            reading.setId(Integer.valueOf(id));
            realm.copyToRealm(reading);
            realm.commitTransaction();
            return true;
        }
    }
    private String generateIdFromDate( int readingId) {

        return "" + readingId;
    }

    public boolean editGlucoseReading(int oldId, Glucose reading) {
        // First delete the old reading
        deleteGlucoseReading(getGlucoseReadingById(oldId));
        // then save the new one
        return addGlucoseReading(reading);
    }

    public void deleteGlucoseReading(Glucose reading) {
        realm.beginTransaction();
        reading.deleteFromRealm();
        realm.commitTransaction();
    }

    public Glucose getLastGlucoseReading() {
        RealmResults<Glucose> results =
                realm.where(Glucose.class)
                        .findAllSorted("created", Sort.DESCENDING);
        return results.get(0);
    }

    public Glucose getGlucoseReadingById(int id) {
        return realm.where(Glucose.class)
                .equalTo("id", id)
                .findFirst();
    }

}
