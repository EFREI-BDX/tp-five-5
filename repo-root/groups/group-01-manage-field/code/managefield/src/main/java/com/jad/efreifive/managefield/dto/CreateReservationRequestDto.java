package com.jad.efreifive.managefield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequestDto {

    @NotBlank(message = "status_id is required.")
    @JsonProperty("status_id")
    private String statusId;

    @NotBlank(message = "date is required.")
    private String date;

    @NotBlank(message = "start_time is required.")
    @JsonProperty("start_time")
    private String startTime;

    @NotBlank(message = "end_time is required.")
    @JsonProperty("end_time")
    private String endTime;
}
