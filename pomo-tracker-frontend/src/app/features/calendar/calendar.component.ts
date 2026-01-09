import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import { CalendarService } from '../../core/services/calendar.service';
import { CalendarEventDTO } from '../../core/models/calendar-event';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, RouterLink, RouterLinkActive],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css'
})
export class CalendarComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin],
    events: [],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth'
    },
    locale: 'fr',
    firstDay: 1, // Lundi
    eventColor: '#667eea',
    eventTextColor: '#fff'
  };

  constructor(private calendarService: CalendarService) {}

  ngOnInit() {
    this.loadEvents();
  }

  loadEvents() {
    // Charger les événements pour l'année en cours
    const now = new Date();
    const yearStart = new Date(now.getFullYear(), 0, 1);
    const yearEnd = new Date(now.getFullYear(), 11, 31);

    const startStr = this.formatDate(yearStart);
    const endStr = this.formatDate(yearEnd);

    this.calendarService.getEvents(startStr, endStr).subscribe({
      next: (events) => {
        this.calendarOptions.events = this.mapEventsToFullCalendar(events);
      },
      error: (err) => {
        console.error('Error loading calendar events:', err);
      }
    });
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  mapEventsToFullCalendar(events: CalendarEventDTO[]): EventInput[] {
    return events.map(event => ({
      title: event.title,
      start: event.start,
      end: event.end,
      backgroundColor: event.color,
      borderColor: event.color,
      textColor: '#fff'
    }));
  }
}
