package com.nouh.pomotrackerapi.service;

import com.nouh.pomotrackerapi.dao.entities.Task;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(User user, String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setUser(user);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUser(user);
    }

    public Task markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    public Task findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long taskId, String title, Boolean completed, 
                          Integer estimatedPomodoros, 
                          java.time.LocalDateTime plannedStartDate,
                          java.time.LocalDateTime plannedEndDate) {
        Task task = findById(taskId);
        
        if (title != null) {
            task.setTitle(title);
        }
        if (completed != null) {
            task.setCompleted(completed);
        }
        if (estimatedPomodoros != null) {
            task.setEstimatedPomodoros(estimatedPomodoros);
        }
        if (plannedStartDate != null) {
            task.setPlannedStartDate(plannedStartDate);
        }
        if (plannedEndDate != null) {
            task.setPlannedEndDate(plannedEndDate);
        }
        
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        Task task = findById(taskId);
        taskRepository.delete(task);
    }
}