package com.example.tripster.model;

import com.example.tripster.model.enums.AccommodationStatus;
import com.example.tripster.model.enums.AccommodationType;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Accommodation {
    private Long id;

    private String name;

    private Long ownerId;

    private String country;

    private String city;

    private String zipCode;

    private String street;

    private String number;

    private String shortDescription;

    private String description;

    private Set<Long> amenities;

    private int minCap;

    private int maxCap;

    private int cancelDuration;

    private AccommodationType type;

    private boolean automaticReservation;

    private AccommodationStatus status;

    private Set<Day> calendar;

    private Double rating;

    private Long numOfReviews;

    private String photo;

    private boolean pricePerNight;

    private Long ownerUserId;
}
