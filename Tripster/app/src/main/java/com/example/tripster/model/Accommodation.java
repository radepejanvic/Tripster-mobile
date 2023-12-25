package com.example.tripster.model;

import com.example.tripster.model.enums.AccommodationStatus;
import com.example.tripster.model.enums.AccommodationType;

import java.util.Set;

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

    public Accommodation() {}

    public Accommodation(String name, Long ownerId, String country,
                         String city, String zipCode, String street, String number,
                         String shortDescription, String description, int minCap, int maxCap, int cancelDuration,
                         AccommodationType type, boolean automaticReservation, boolean pricePerNight) {
        this.name = name;
        this.ownerId = ownerId;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.shortDescription = shortDescription;
        this.description = description;
        this.minCap = minCap;
        this.maxCap = maxCap;
        this.cancelDuration = cancelDuration;
        this.type = type;
        this.automaticReservation = automaticReservation;
        this.pricePerNight = pricePerNight;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Long> amenities) {
        this.amenities = amenities;
    }

    public int getMinCap() {
        return minCap;
    }

    public void setMinCap(int minCap) {
        this.minCap = minCap;
    }

    public int getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(int maxCap) {
        this.maxCap = maxCap;
    }

    public int getCancelDuration() {
        return cancelDuration;
    }

    public void setCancelDuration(int cancelDuration) {
        this.cancelDuration = cancelDuration;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public boolean isAutomaticReservation() {
        return automaticReservation;
    }

    public void setAutomaticReservation(boolean automaticReservation) {
        this.automaticReservation = automaticReservation;
    }

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }

    public Set<Day> getCalendar() {
        return calendar;
    }

    public void setCalendar(Set<Day> calendar) {
        this.calendar = calendar;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(Long numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public boolean isPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(boolean pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", amenities=" + amenities +
                ", minCap=" + minCap +
                ", maxCap=" + maxCap +
                ", cancelDuration=" + cancelDuration +
                ", type=" + type +
                ", automaticReservation=" + automaticReservation +
                ", status=" + status +
                ", calendar=" + calendar +
                ", rating=" + rating +
                ", numOfReviews=" + numOfReviews +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
