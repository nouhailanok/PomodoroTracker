package com.nouh.pomotrackerapi.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InterruptPomodoroRequest {
    private int durationInMinutes; // Le temps réellement passé
}