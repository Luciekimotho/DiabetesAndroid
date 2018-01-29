package com.ellekay.lucie.diabetes.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by lucie on 1/23/2018.
 */

public class ReminderRealm extends RealmObject {

    @Index
    public int id;
    public String reminder;
    public String time;
    public Boolean alarm;
    public int user;

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", reminder='" + reminder + '\'' +
                ", time='" + time + '\'' +
                ", alarm='" + alarm + '\'' +
                ", user=" + user +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
