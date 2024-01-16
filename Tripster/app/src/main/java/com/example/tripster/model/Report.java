package com.example.tripster.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Report {

    private Long id;

    private String reason;

    private Long reporterId;

    private Long reporteeId;

    private Long reviewId;

}
