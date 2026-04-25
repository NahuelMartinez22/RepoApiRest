package com.martinez.dentist.professionals.controllers.schedule;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
public class ScheduleResponseDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleResponseDTO(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
