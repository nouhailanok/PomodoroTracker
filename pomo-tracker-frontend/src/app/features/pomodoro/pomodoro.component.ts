import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../core/services/task.service';
import { PomodoroService } from '../../core/services/pomodoro.service';
import { Task } from '../../core/models/task';
import { PomodoroSession } from '../../core/models/pomodoro-session';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-pomodoro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive], // Ajoute RouterLink et RouterLinkActive
  templateUrl: './pomodoro.component.html',
  styleUrl: './pomodoro.component.css'
})
export class PomodoroComponent implements OnInit, OnDestroy {
  tasks: Task[] = [];
  activeSession: PomodoroSession | null = null;

  // Minuteur
  totalSeconds: number = 25 * 60; // 25 minutes en secondes
  elapsedSeconds: number = 0;
  displayTime: string = '25:00';
  isRunning: boolean = false;
  interval: any;

  selectedTaskId: number | null = null;

  constructor(
    private taskService: TaskService,
    private pomodoroService: PomodoroService
  ) {}

  ngOnInit() {
    this.loadTasks();
    this.loadActiveSession();
  }

  ngOnDestroy() {
    this.clearInterval();
  }

  loadTasks() {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        // Filtrer seulement les tâches non complétées
        this.tasks = tasks.filter(t => !t.completed);
      },
      error: (err) => {
        console.error('Error loading tasks:', err);
      }
    });
  }

  loadActiveSession() {
    this.pomodoroService.getAllSessions().subscribe({
      next: (sessions) => {
        // Trouver la session en cours
        const active = sessions.find(s => s.status === 'IN_PROGRESS');
        if (active) {
          this.activeSession = active;
          this.selectedTaskId = active.task?.id || null;
          this.startTimerFromSession(active);
        }
      },
      error: (err) => {
        console.error('Error loading sessions:', err);
      }
    });
  }

  startPomodoro() {
    if (!this.selectedTaskId) {
      alert('Veuillez sélectionner une tâche');
      return;
    }

    this.pomodoroService.startPomodoro(this.selectedTaskId).subscribe({
      next: (session) => {
        this.activeSession = session;
        this.totalSeconds = 25 * 60;
        this.elapsedSeconds = 0;
        this.startTimer();
      },
      error: (err) => {
        console.error('Error starting pomodoro:', err);
        alert(err.error?.message || 'Erreur lors du démarrage du Pomodoro');
      }
    });
  }

  interruptPomodoro() {
    if (!this.activeSession?.id) return;

    this.pomodoroService.interruptPomodoro(this.activeSession.id).subscribe({
      next: (session) => {
        this.stopTimer();
        this.activeSession = session;
        
      },
      error: (err) => {
        console.error('Error interrupting pomodoro:', err);
        alert(err.error?.message || 'Erreur lors de l\'interruption');
      }
    });
  }

  resumePomodoro() {
    if (!this.activeSession?.id) return;

    this.pomodoroService.resumePomodoro(this.activeSession.id).subscribe({
      next: (session) => {
        this.activeSession = session;
        // Temps déjà accumulé
        const accumulatedSeconds = session.durationInMinutes * 60;

        this.totalSeconds = 25 * 60;
        this.elapsedSeconds = accumulatedSeconds;

        this.startTimer();
        // // Calculer le temps restant (25 min - temps déjà accumulé)
        // const remainingMinutes = 25 - session.durationInMinutes;
        // this.totalSeconds = remainingMinutes * 60;
        // this.elapsedSeconds = 0;
        // this.startTimer();
      },
      error: (err) => {
        console.error('Error resuming pomodoro:', err);
        alert(err.error?.message || 'Erreur lors de la reprise');
      }
    });
  }

  completePomodoro() {
    if (!this.activeSession?.id) return;

    this.pomodoroService.completePomodoro(this.activeSession.id).subscribe({
      next: () => {
        this.stopTimer();
        this.activeSession = null;
        this.resetTimer();
        this.loadTasks();
        alert('🎉 Pomodoro complété ! Excellent travail !');
      },
      error: (err) => {
        console.error('Error completing pomodoro:', err);
        alert(err.error?.message || 'Erreur lors de la complétion');
      }
    });
  }

  startTimer() {
    this.clearInterval(); 
    this.isRunning = true;
    this.interval = setInterval(() => {
      this.elapsedSeconds++;
      const remainingSeconds = this.totalSeconds - this.elapsedSeconds;

      if (remainingSeconds <= 0) {
        // Le timer est terminé, compléter automatiquement
        this.completePomodoro();
        return;
      }

      this.updateDisplayTime(remainingSeconds);
    }, 1000);
  }

  startTimerFromSession(session: PomodoroSession) {
    // Calculer le temps écoulé depuis le startTime
    const startTime = new Date(session.startTime).getTime();
    const now = new Date().getTime();
    const elapsedMs = now - startTime;
    const elapsedSecondsFromStart = Math.floor(elapsedMs / 1000);

    // Le temps total accumulé jusqu'à présent
    const totalAccumulatedSeconds = session.durationInMinutes * 60 + elapsedSecondsFromStart;
    const remainingSeconds = (25 * 60) - totalAccumulatedSeconds;

    if (remainingSeconds <= 0) {
      // Le Pomodoro est déjà terminé
      this.completePomodoro();
      return;
    }

    this.totalSeconds = 25 * 60;
    this.elapsedSeconds = totalAccumulatedSeconds;
    this.startTimer();
  }

  stopTimer() {
    this.isRunning = false;
    this.clearInterval();
  }

  resetTimer() {
    this.totalSeconds = 25 * 60;
    this.elapsedSeconds = 0;
    this.displayTime = '25:00';
  }

  clearInterval() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  updateDisplayTime(remainingSeconds: number) {
    const minutes = Math.floor(remainingSeconds / 60);
    const seconds = remainingSeconds % 60;
    this.displayTime = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }

  getProgressPercentage(): number {
    if (this.totalSeconds === 0) return 0;
    return (this.elapsedSeconds / this.totalSeconds) * 100;
  }
}
