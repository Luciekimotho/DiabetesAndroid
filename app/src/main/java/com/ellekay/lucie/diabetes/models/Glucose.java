package com.ellekay.lucie.diabetes.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by lucie on 11/16/2017.
 */

public class Glucose extends RealmObject {
    @Index
    public Integer id;

    public Float glucoseLevel;

    public String timePeriod;

    public String timeOfDay;

    public String action;

    public String medication;

    public String notes;

    public String createdAt;

    public String updatedAt;

    public Integer user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(Float glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
