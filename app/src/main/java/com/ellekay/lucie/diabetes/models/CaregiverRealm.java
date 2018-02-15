package com.ellekay.lucie.diabetes.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by lucie on 1/30/2018.
 */

public class CaregiverRealm extends RealmObject {
    @Index
    private int id;
    private String name;
    private String email;
    private String phone;
    private String relation;


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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
