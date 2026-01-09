package com.nouh.pomotrackerapi.service;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.Task;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.PomodoroSessionRepository;
import com.nouh.pomotrackerapi.dao.repositories.TaskRepository;
import com.nouh.pomotrackerapi.dto.CalendarEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final TaskRepository taskRepository;
    private final PomodoroSessionRepository pomodoroSessionRepository;

    public List<CalendarEventDTO> getCalendarEvents(User user, LocalDate rangeStart, LocalDate rangeEnd) {
        List<CalendarEventDTO> events = new ArrayList<>();

        // Convertir LocalDate en LocalDateTime pour les requêtes
        LocalDateTime startDateTime = rangeStart.atStartOfDay();
        LocalDateTime endDateTime = rangeEnd.atTime(LocalTime.MAX);

        // Tâches planifiées
        List<Task> plannedTasks = taskRepository.findAllByUserAndPlannedStartDateBetween(
                user, startDateTime, endDateTime
        );

        for (Task task : plannedTasks) {
            if (task.getPlannedStartDate() != null && task.getPlannedEndDate() != null) {
                CalendarEventDTO event = new CalendarEventDTO();
                event.setTitle(task.getTitle() + " (Planifié)");
                event.setStart(task.getPlannedStartDate());
                event.setEnd(task.getPlannedEndDate());
                event.setColor("blue");
                event.setType("PLANNED");
                events.add(event);
            }
        }

        // Sessions PomodoroComponent réalisées (COMPLETED et INTERRUPTED)
        // On cherche toutes les sessions qui ont commencé dans la période OU qui se sont terminées dans la période
        List<PomodoroSession> allSessions = pomodoroSessionRepository
                .findAllByUserAndStartTimeBetween(user, startDateTime, endDateTime);

        for (PomodoroSession session : allSessions) {
            // Inclure les sessions complétées ET interrompues
            if (session.getStatus() == PomodoroSession.SessionStatus.COMPLETED || 
                session.getStatus() == PomodoroSession.SessionStatus.INTERRUPTED) {
                
                CalendarEventDTO event = new CalendarEventDTO();
                event.setTitle(session.getTask().getTitle() + " (Pomodoro)");
                event.setStart(session.getStartTime());
                
                // Calculer la fin en fonction de la durée accumulée
                LocalDateTime endTime = session.getStartTime().plusMinutes(session.getDurationInMinutes());
                event.setEnd(endTime);
                
                // Couleur différente selon le statut
                if (session.getStatus() == PomodoroSession.SessionStatus.COMPLETED) {
                    event.setColor("green");
                    event.setType("COMPLETED");
                } else {
                    event.setColor("orange");
                    event.setType("INTERRUPTED");
                }
                
                events.add(event);
            }
        }

        // Aussi chercher les sessions qui ont commencé avant mais se sont terminées dans la période
        // (pour les sessions longues qui s'étendent sur plusieurs jours)
        List<PomodoroSession> sessionsEndingInRange = pomodoroSessionRepository
                .findByUser(user)
                .stream()
                .filter(session -> {
                    if (session.getStatus() == PomodoroSession.SessionStatus.COMPLETED || 
                        session.getStatus() == PomodoroSession.SessionStatus.INTERRUPTED) {
                        LocalDateTime sessionEnd = session.getStartTime().plusMinutes(session.getDurationInMinutes());
                        return sessionEnd.isAfter(startDateTime) && sessionEnd.isBefore(endDateTime);
                    }
                    return false;
                })
                .toList();

        for (PomodoroSession session : sessionsEndingInRange) {
            // Vérifier qu'on ne l'a pas déjà ajoutée
            boolean alreadyAdded = events.stream()
                    .anyMatch(e -> e.getStart().equals(session.getStartTime()) && 
                                e.getTitle().contains(session.getTask().getTitle()));
            
            if (!alreadyAdded) {
                CalendarEventDTO event = new CalendarEventDTO();
                event.setTitle(session.getTask().getTitle() + " (Pomodoro)");
                event.setStart(session.getStartTime());
                event.setEnd(session.getStartTime().plusMinutes(session.getDurationInMinutes()));
                
                if (session.getStatus() == PomodoroSession.SessionStatus.COMPLETED) {
                    event.setColor("green");
                    event.setType("COMPLETED");
                } else {
                    event.setColor("orange");
                    event.setType("INTERRUPTED");
                }
                
                events.add(event);
            }
        }

        return events;
    }
}