package com.nouh.pomotrackerapi.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
    private int completedCount;
    private int totalMinutes;
}