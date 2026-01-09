package com.nouh.pomotrackerapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    private String title;
    private Boolean completed;
    private Integer estimatedPomodoros;
    private LocalDateTime plannedStartDate;
    private LocalDateTime plannedEndDate;
}