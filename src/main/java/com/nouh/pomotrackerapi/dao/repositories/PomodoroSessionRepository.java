package com.nouh.pomotrackerapi.dao.repositories;

import com.nouh.pomotrackerapi.dao.entities.PomodoroSession;
import com.nouh.pomotrackerapi.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {

    @Query("SELECT p FROM PomodoroSession p WHERE p.user = :user AND p.startTime >= :date")
    List<PomodoroSession> findSessionsForUserSince(
            @Param("user") User user,
            @Param("date") LocalDateTime date
    );

    List<PomodoroSession> findAllByUserAndStartTimeBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    List<PomodoroSession> findByUserAndStatus(
            User user,
            PomodoroSession.SessionStatus status
    );
    
    List<PomodoroSession> findByUser(User user);

}