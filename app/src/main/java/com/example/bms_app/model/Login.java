package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("errorMessage")
    @Expose
    private ErrorMessage errorMessage;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Login{" +
                "user=" + user +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
