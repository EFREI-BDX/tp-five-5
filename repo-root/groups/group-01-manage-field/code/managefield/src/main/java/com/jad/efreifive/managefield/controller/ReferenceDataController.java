package com.jad.efreifive.managefield.controller;

import com.jad.efreifive.managefield.dto.FieldStatusDto;
import com.jad.efreifive.managefield.dto.ReservationStatusDto;
import com.jad.efreifive.managefield.mapper.ReferenceDataMapper;
import com.jad.efreifive.managefield.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;
    private final ReferenceDataMapper referenceDataMapper;

    @GetMapping("/field-statuses")
    public List<FieldStatusDto> listFieldStatuses() {
        return referenceDataService.listFieldStatuses().stream()
            .map(referenceDataMapper::toDto)
            .toList();
    }

    @GetMapping("/reservation-statuses")
    public List<ReservationStatusDto> listReservationStatuses() {
        return referenceDataService.listReservationStatuses().stream()
            .map(referenceDataMapper::toDto)
            .toList();
    }
}
