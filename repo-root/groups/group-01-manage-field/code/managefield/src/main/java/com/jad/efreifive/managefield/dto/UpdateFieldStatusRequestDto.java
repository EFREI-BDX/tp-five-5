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
public class UpdateFieldStatusRequestDto {

    @NotBlank(message = "status_id is required.")
    @JsonProperty("status_id")
    private String statusId;
}
