package com.ellekay.lucie.diabetes.models;

/**
 * Created by lucie on 9/27/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;


    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("user")
    @Expose
    private int user;

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", location='" + location + '\'' +
                ", sex='" + sex + '\'' +
                ", user=" + user +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}


