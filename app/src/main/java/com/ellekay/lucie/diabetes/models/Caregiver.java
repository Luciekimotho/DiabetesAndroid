package com.ellekay.lucie.diabetes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lucie on 1/10/2018.
 */

public class Caregiver {

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

    @SerializedName("relation")
    @Expose
    private String relation;

    @Override
    public String toString() {
        email = user.getEmail();
        name = user.getName();
        return "Caregiver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
