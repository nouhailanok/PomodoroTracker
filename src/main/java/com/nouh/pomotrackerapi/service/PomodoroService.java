package com.nouh.pomotrackerapi.service;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.Task;
import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.PomodoroSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PomodoroService {

    private final PomodoroSessionRepository pomodoroSessionRepository;
    private final TaskService taskService;

    public PomodoroSession startPomodoro(User user, Long taskId) {
        Task task = taskService.findById(taskId);

        // Vérifier que la tâche appartient à l'utilisateur
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Task does not belong to user");
        }

        PomodoroSession session = new PomodoroSession();
        session.setUser(user);
        session.setTask(task);
        session.setStartTime(LocalDateTime.now());
        session.setDurationInMinutes(0);
        session.setStatus(PomodoroSession.SessionStatus.IN_PROGRESS);

        return pomodoroSessionRepository.save(session);
    }

    public PomodoroSession resumePomodoro(Long sessionId) {
        PomodoroSession session = pomodoroSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != PomodoroSession.SessionStatus.INTERRUPTED) {
            throw new RuntimeException("Only interrupted sessions can be resumed");
        }

        session.setStartTime(LocalDateTime.now()); // redémarre le chronomètre
        session.setStatus(PomodoroSession.SessionStatus.IN_PROGRESS);
        return pomodoroSessionRepository.save(session);
    }

    public PomodoroSession completePomodoro(Long sessionId) {
        PomodoroSession session = pomodoroSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Vérifier que la session est en cours
        if (session.getStatus() != PomodoroSession.SessionStatus.IN_PROGRESS) {
            throw new RuntimeException("Session is not in progress");
        }

        // Vérifier que 25 minutes sont passées
        long elapsedThisRun = Duration.between(session.getStartTime(), LocalDateTime.now()).toMinutes();
        long total = session.getDurationInMinutes() + elapsedThisRun;


        if (total < 25) {
            throw new RuntimeException("Cannot complete session: only " + total + " minutes have passed. Need 25 minutes.");
        }
        
        session.setDurationInMinutes((int) Math.min(total, 25));
        session.setStatus(PomodoroSession.SessionStatus.COMPLETED);
        return pomodoroSessionRepository.save(session);
    }

    public PomodoroSession interruptPomodoro(Long sessionId) {
        PomodoroSession session = pomodoroSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Vérifier que la session est en cours
        if (session.getStatus() != PomodoroSession.SessionStatus.IN_PROGRESS) {
            throw new RuntimeException("Session is not in progress");
        }

        // Calculer automatiquement la durée réelle entre le début et maintenant
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(session.getStartTime(), now);
        long minutesPassed = duration.toMinutes();

        // Vérifier que le temps passé n'est pas négatif
        if (minutesPassed < 0) {
            throw new RuntimeException("Invalid duration: negative time");
        }

        long totalMinutes = session.getDurationInMinutes() + minutesPassed;

        // Si plus de 25 minutes se sont écoulées, on limite à 25
        int finalDuration = (int) Math.min(totalMinutes, 25);

        // Mettre à jour la durée réelle et le statut
        session.setDurationInMinutes(finalDuration);
        session.setStatus(PomodoroSession.SessionStatus.INTERRUPTED);
        
        return pomodoroSessionRepository.save(session);
    }

    

    public PomodoroSession findById(Long sessionId) {
        return pomodoroSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
    @Transactional(readOnly = true)
    public List<PomodoroSession> getSessionsForUser(User user) {
        return pomodoroSessionRepository.findByUser(user);
    }
}