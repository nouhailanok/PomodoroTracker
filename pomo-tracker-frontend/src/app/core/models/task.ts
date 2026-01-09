export interface Task {
  id?: number;
  title: string;
  completed: boolean;
  estimatedPomodoros: number;
  plannedStartDate?: string;
  plannedEndDate?: string;
}
