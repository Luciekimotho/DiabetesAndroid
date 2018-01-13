package com.ellekay.lucie.diabetes.models;

/**
 * Created by lucie on 9/27/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Readings {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("glucoseLevel")
    @Expose
    private Float glucoseLevel;
    @SerializedName("timePeriod")
    @Expose
    private String timePeriod;
    @SerializedName("timeOfDay")
    @Expose
    private String timeOfDay;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("medication")
    @Expose
    private String medication;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private Integer user;

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

    @Override
    public String toString() {
        return "Readings{" +
                "id=" + id +
                ", glucoseLevel='" + glucoseLevel + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                ", timeOfDay='" + timeOfDay + '\'' +
                ", action='" + action + '\'' +
                ", medication='" + medication + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                '}';
    }
}