package com.example.tripster.model.view;

import com.example.tripster.adapters.LocalDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interval {

    @JsonAdapter(LocalDateAdapter.class)
    @SerializedName("start")
    private LocalDate start;

    @JsonAdapter(LocalDateAdapter.class)
    @SerializedName("end")
    private LocalDate end;

    private double price;

}
