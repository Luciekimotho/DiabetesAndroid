package com.ellekay.lucie.diabetes.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by lucie on 1/21/2018.
 */

public class DoctorRealm extends RealmObject {
    @Index
    public int id;
    public String name;
    public String email;
    public String phone;
    public String notes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
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