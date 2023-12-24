package com.example.tripster.model;

import android.text.Editable;

public class User {

    private Long userID;
    private String email;

    private String password;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getPersonID() {
        return personID;
    }

    public void setPersonID(Long personID) {
        this.personID = personID;
    }

    private UserType userType;

    private String token;

    private Long personID;
    public User(long id, String email, UserType type,long personID) {
        this.userID = id;
        this.email = email;
        this.userType = type;
        this.personID = personID;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String jwt, Long person_id) {
        this.userID = id;
        this.token = jwt;
        this.personID = person_id;
    }



    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
