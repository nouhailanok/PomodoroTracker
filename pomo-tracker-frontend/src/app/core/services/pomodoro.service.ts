import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PomodoroSession, StartPomodoroRequest } from '../models/pomodoro-session';

@Injectable({
  providedIn: 'root'
})
export class PomodoroService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllSessions(): Observable<PomodoroSession[]> {
    return this.http.get<PomodoroSession[]>(`${this.apiUrl}/pomodoro`);
  }

  getSessionById(id: number): Observable<PomodoroSession> {
    return this.http.get<PomodoroSession>(`${this.apiUrl}/pomodoro/${id}`);
  }

  startPomodoro(taskId: number): Observable<PomodoroSession> {
    const request: StartPomodoroRequest = { taskId };
    return this.http.post<PomodoroSession>(`${this.apiUrl}/pomodoro/start`, request);
  }

  interruptPomodoro(sessionId: number): Observable<PomodoroSession> {
    return this.http.post<PomodoroSession>(`${this.apiUrl}/pomodoro/${sessionId}/interrupt`, {});
  }

  resumePomodoro(sessionId: number): Observable<PomodoroSession> {
    return this.http.post<PomodoroSession>(`${this.apiUrl}/pomodoro/${sessionId}/resume`, {});
  }

  completePomodoro(sessionId: number): Observable<PomodoroSession> {
    return this.http.post<PomodoroSession>(`${this.apiUrl}/pomodoro/${sessionId}/complete`, {});
  }
}
