package com.nouh.pomotrackerapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDTO {
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String color;
    private String type; // "PLANNED" or "COMPLETED"
}