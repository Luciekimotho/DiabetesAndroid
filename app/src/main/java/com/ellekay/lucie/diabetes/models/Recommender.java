package com.ellekay.lucie.diabetes.models;

import java.util.Date;

/**
 * Created by lucie on 2/1/2018.
 */

public class Recommender {
    private int id;
    private Date time;
    private float glucose;
    private float targetCalories;
    private int treatment;

    @Override
    public String toString() {
        return "Recommendation{" +
                "id = " + id +
                ", time = '" + time +'\''+
                ", glucose = '" + glucose +'\''+
                ", target calories = '" + targetCalories + '\'' +
                ", treatment = '" + treatment + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getGlucose() {
        return glucose;
    }

    public void setGlucose(float glucose) {
        this.glucose = glucose;
    }

    public float getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(float targetCalories) {
        this.targetCalories = targetCalories;
    }

    public int getTreatment() {
        return treatment;
    }

    public void setTreatment(int treatment) {
        this.treatment = treatment;
    }


}
