import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CalendarEventDTO } from '../models/calendar-event';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getEvents(start: string, end: string): Observable<CalendarEventDTO[]> {
    const params = new HttpParams()
      .set('start', start)
      .set('end', end);

    return this.http.get<CalendarEventDTO[]>(`${this.apiUrl}/calendar/events`, { params });
  }
}
