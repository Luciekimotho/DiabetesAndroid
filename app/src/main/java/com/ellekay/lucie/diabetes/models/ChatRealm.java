package com.ellekay.lucie.diabetes.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by lucie on 2/13/2018.
 */

public class ChatRealm extends RealmObject {

    @Index
    private Integer id;
    private Integer author;
    private String author_name;
    private Integer recepient;
    private String receptient_name;
    private String message;
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getRecepient() {
        return recepient;
    }

    public void setRecepient(Integer recepient) {
        this.recepient = recepient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
