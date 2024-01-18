package com.example.tripster.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    private Long id;

    private Long userId;

    private boolean reservationNotification;

    private boolean reviewNotification;

    private boolean accommodationReviewNotification;

}
