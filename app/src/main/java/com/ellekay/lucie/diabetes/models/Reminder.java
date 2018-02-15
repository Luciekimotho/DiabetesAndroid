package com.ellekay.lucie.diabetes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lucie on 1/10/2018.
 */

public class Reminder {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("reminder")
    @Expose
    private String reminder;

    @SerializedName("time")
    @Expose
    private Date time;

    @SerializedName("alarm")
    @Expose
    private Boolean alarm;

    @SerializedName("user")
    @Expose
    private int user;

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", reminder='" + reminder + '\'' +
                ", alarm='" + alarm + '\'' +
                ", time='" + time + '\'' +
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
