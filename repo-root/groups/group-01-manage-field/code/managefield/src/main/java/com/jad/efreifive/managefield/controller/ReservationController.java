package com.jad.efreifive.managefield.controller;

import com.jad.efreifive.managefield.dto.CreateReservationRequestDto;
import com.jad.efreifive.managefield.dto.ReservationDto;
import com.jad.efreifive.managefield.dto.UpdateReservationStatusRequestDto;
import com.jad.efreifive.managefield.mapper.ReservationMapper;
import com.jad.efreifive.managefield.service.ReservationService;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/fields/{field_id}/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(
        @PathVariable("field_id") String fieldId,
        @Valid @RequestBody CreateReservationRequestDto request
    ) {
        return reservationMapper.toDto(
            reservationService.createReservation(
                Id.of(fieldId),
                Id.of(request.getStatusId()),
                TimeSlot.of(request.getDate(), request.getStartTime(), request.getEndTime())
            )
        );
    }

    @PatchMapping("/{reservation_id}/status")
    public ReservationDto updateReservationStatus(
        @PathVariable("field_id") String fieldId,
        @PathVariable("reservation_id") String reservationId,
        @Valid @RequestBody UpdateReservationStatusRequestDto request
    ) {
        return reservationMapper.toDto(
            reservationService.updateReservationStatus(
                Id.of(fieldId),
                Id.of(reservationId),
                Id.of(request.getStatusId())
            )
        );
    }
}
