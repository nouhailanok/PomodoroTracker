package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.UserRepository;
import com.nouh.pomotrackerapi.dto.InterruptPomodoroRequest;
import com.nouh.pomotrackerapi.dto.StartPomodoroRequest;
import com.nouh.pomotrackerapi.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/api/pomodoro")
@RequiredArgsConstructor
public class PomodoroController {

    private final PomodoroService pomodoroService;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<PomodoroSession>> getSessions(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(pomodoroService.getSessionsForUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PomodoroSession> getSession(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pomodoroService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/start")
    public ResponseEntity<PomodoroSession> startPomodoro(
            @RequestBody StartPomodoroRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(pomodoroService.startPomodoro(user, request.getTaskId()));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completePomodoro(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pomodoroService.completePomodoro(id));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Session not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Session not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/interrupt")
    public ResponseEntity<?> interruptPomodoro(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pomodoroService.interruptPomodoro(id));
        } catch (RuntimeException e) {
            if ("Session not found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Session not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<?> resumePomodoro(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pomodoroService.resumePomodoro(id));
        } catch (RuntimeException e) {
            if ("Session not found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Session not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Classe interne pour les réponses d'erreur
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
/*
package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dto.StartPomodoroRequest;
import com.nouh.pomotrackerapi.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pomodoro")
@RequiredArgsConstructor
public class PomodoroController {

    private final PomodoroService pomodoroService;

    @PostMapping("/start")
    public ResponseEntity<PomodoroSession> startPomodoro(
            @RequestBody StartPomodoroRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(pomodoroService.startPomodoro(user, request.getTaskId()));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<PomodoroSession> completePomodoro(@PathVariable Long id) {
        return ResponseEntity.ok(pomodoroService.completePomodoro(id));
    }
}*/
