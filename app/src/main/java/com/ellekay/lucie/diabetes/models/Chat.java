package com.ellekay.lucie.diabetes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lucie on 2/13/2018.
 */

public class Chat {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("author")
    @Expose
    private Integer author;

    @SerializedName("author_name")
    @Expose
    private String author_name;

    @SerializedName("recepient")
    @Expose
    private Integer recepient;

    @SerializedName("recepient_name")
    @Expose
    private String receptient_name;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("created_at")
    @Expose
    private Date time;

    @Override
    public String toString() {
//        author_name = user.getName();
//        receptient_name = user.getName();

        return "Chat{" +
                "id = " + id +
                ", author = '" + author +'\''+
                ", recepient = '" + recepient +'\''+
                ", message = '" + message + '\'' +
                ", time = '" + time + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getReceptient_name() {
        return receptient_name;
    }

    public void setReceptient_name(String receptient_name) {
        this.receptient_name = receptient_name;
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
