package com.nouh.pomotrackerapi.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StartPomodoroRequest {
    private Long taskId;
}