package com.ellekay.lucie.diabetes.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lucie on 1/10/2018.
 */

public class Doctor {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("notes")
    @Expose
    private String notes;

    @Override
    public String toString() {
        email = user.getEmail();
        name = user.getName();
        return "Doctor{" +
                "id = " + user.getId() +
                ", name = '" + name +'\''+
                ", email = '" + email +'\''+
                ", phone = '" + phone + '\'' +
                ", notes = '" + notes + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setId(User user) {
        this.user = user;
    }

    public int getId() {
        return user.getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
