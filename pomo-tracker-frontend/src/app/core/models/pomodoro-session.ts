export interface PomodoroSession {
  id?: number;
  startTime: string;
  durationInMinutes: number;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'INTERRUPTED';
  task?: {
    id: number;
    title: string;
    completed: boolean;
  };
}

export interface StartPomodoroRequest {
  taskId: number;
}
