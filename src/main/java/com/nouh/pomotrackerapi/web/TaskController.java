package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.Task;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.UserRepository;
import com.nouh.pomotrackerapi.dto.UpdateTaskRequest;
import com.nouh.pomotrackerapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    // @GetMapping
    // public ResponseEntity<List<Task>> getTasks(@RequestParam Long userId) {
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new RuntimeException("User not found"));
    //     return ResponseEntity.ok(taskService.getTasksForUser(user));
    // }
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(taskService.getTasksForUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }


    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String title = request.get("title");
        return ResponseEntity.ok(taskService.createTask(user, title));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(
                id,
                request.getTitle(),
                request.getCompleted(),
                request.getEstimatedPomodoros(),
                request.getPlannedStartDate(),
                request.getPlannedEndDate()
        ));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markTaskAsCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

/*
package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.Task;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTasksForUser(user));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User user) {
        String title = request.get("title");
        return ResponseEntity.ok(taskService.createTask(user, title));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markTaskAsCompleted(id));
    }
}*/
