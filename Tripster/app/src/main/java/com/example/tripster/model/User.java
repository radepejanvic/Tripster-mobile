package com.example.tripster.model;

import android.text.Editable;

public class User {

    private Long userID;
    private String email;

    private String password;

    private UserType userType;
    private String name;
    private String surname;
    private String phone;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String number;
    private String token;
    private Long personID;

    public User(){}

    public User( String email, String password, UserType userType, String name,
                 String surname, String phone, String country, String city,
                 String zipCode, String street, String number) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
    }

    public User(long id, String email, UserType type, long personID) {
        this.userID = id;
        this.email = email;
        this.userType = type;
        this.personID = personID;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
