import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { TaskService } from '../../core/services/task.service';
import { StatsService } from '../../core/services/stats.service';
import { Task } from '../../core/models/task';
import { StatisticsDTO } from '../../core/models/statistics';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterLinkActive],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  tasks: Task[] = [];
  stats: StatisticsDTO = { completedCount: 0, totalMinutes: 0 };
  username: string | null = '';
  taskForm: FormGroup;
  showTaskForm: boolean = false;

  constructor(
    private authService: AuthService,
    private taskService: TaskService,
    private statsService: StatsService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.loadTasks();
    this.loadStats();
  }

  loadTasks() {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
      },
      error: (err) => {
        console.error('Error loading tasks:', err);
      }
    });
  }

  loadStats() {
    this.statsService.getTodayStats().subscribe({
      next: (stats) => {
        this.stats = stats;
      },
      error: (err) => {
        console.error('Error loading stats:', err);
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleTaskForm() {
    this.showTaskForm = !this.showTaskForm;
  }

  createTask() {
    if (this.taskForm.valid) {
      const title = this.taskForm.get('title')?.value;
      this.taskService.createTask(title).subscribe({
        next: () => {
          this.loadTasks();
          this.taskForm.reset();
          this.showTaskForm = false;
        },
        error: (err) => {
          console.error('Error creating task:', err);
          alert('Erreur lors de la création de la tâche');
        }
      });
    }
  }

  markAsCompleted(id: number) {
    this.taskService.markTaskAsCompleted(id).subscribe({
      next: () => {
        this.loadTasks();
        this.loadStats();
      },
      error: (err) => {
        console.error('Error completing task:', err);
      }
    });
  }

  deleteTask(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette tâche ?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTasks();
        },
        error: (err) => {
          console.error('Error deleting task:', err);
        }
      });
    }
  }
}
