package com.martinez.dentist.professionals.controllers.schedule;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
public class ScheduleRequestDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleRequestDTO() {}

}
