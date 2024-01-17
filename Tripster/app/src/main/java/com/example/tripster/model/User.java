package com.example.tripster.model;

import com.example.tripster.model.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private Long id;


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

    public User(long id, String email, UserType type, long personID,String jwt) {
        this.userID = id;
        this.email = email;
        this.userType = type;
        this.id = personID;
        this.token = jwt;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
