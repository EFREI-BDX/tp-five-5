package com.jad.efreifive.managefield.controller;

import com.jad.efreifive.managefield.dto.FieldDto;
import com.jad.efreifive.managefield.dto.UpdateFieldStatusRequestDto;
import com.jad.efreifive.managefield.mapper.FieldMapper;
import com.jad.efreifive.managefield.service.FieldService;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;
    private final FieldMapper fieldMapper;

    @GetMapping("/available")
    public List<FieldDto> listAvailableFields(
        @RequestParam("date") String date,
        @RequestParam("start_time") String startTime,
        @RequestParam("end_time") String endTime
    ) {
        TimeSlot requestedTimeSlot = TimeSlot.of(date, startTime, endTime);
        return fieldService.listAvailableFields(requestedTimeSlot).stream()
            .map(fieldMapper::toDto)
            .toList();
    }

    @GetMapping("/{field_id}")
    public FieldDto getField(@PathVariable("field_id") String fieldId) {
        return fieldMapper.toDto(fieldService.getField(Id.of(fieldId)));
    }

    @PatchMapping("/{field_id}/status")
    public FieldDto updateFieldStatus(
        @PathVariable("field_id") String fieldId,
        @Valid @RequestBody UpdateFieldStatusRequestDto request
    ) {
        return fieldMapper.toDto(fieldService.updateFieldStatus(Id.of(fieldId), Id.of(request.getStatusId())));
    }
}
