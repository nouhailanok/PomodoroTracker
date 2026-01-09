package com.nouh.pomotrackerapi.service;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.PomodoroSessionRepository;
import com.nouh.pomotrackerapi.dto.StatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final PomodoroSessionRepository pomodoroSessionRepository;

    public StatisticsDTO getTodayStats(User user) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        List<PomodoroSession> completedSessions = pomodoroSessionRepository
                .findSessionsForUserSince(user, startOfDay)
                .stream()
                .filter(session -> session.getStatus() == PomodoroSession.SessionStatus.COMPLETED)
                .toList();

        int completedCount = completedSessions.size();
        int totalMinutes = completedSessions.stream()
                .mapToInt(PomodoroSession::getDurationInMinutes)
                .sum();

        return new StatisticsDTO(completedCount, totalMinutes);
    }

    public StatisticsDTO getMonthlyStats(User user) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfMonth = today.plusDays(1).atStartOfDay(); // jusqu'à demain minuit

        List<PomodoroSession> completedSessions = pomodoroSessionRepository
                .findAllByUserAndStartTimeBetween(user, startOfMonth, endOfMonth)
                .stream()
                .filter(session -> session.getStatus() == PomodoroSession.SessionStatus.COMPLETED)
                .toList();

        int completedCount = completedSessions.size();
        int totalMinutes = completedSessions.stream()
                .mapToInt(PomodoroSession::getDurationInMinutes)
                .sum();

        return new StatisticsDTO(completedCount, totalMinutes);
    }

}