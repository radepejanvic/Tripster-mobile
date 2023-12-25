package com.example.tripster.model;

public class Token {
    private String personID;
    private String userID;
    private String token;

    public long getPersonID() {
        return Long.parseLong(personID);
    }

    public long getUserID() {
        return Long.parseLong(userID);
    }

    public String getToken() {
        return token;
    }

}
