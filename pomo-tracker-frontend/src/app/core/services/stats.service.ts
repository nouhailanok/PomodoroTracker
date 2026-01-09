import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { StatisticsDTO } from '../models/statistics';

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getTodayStats(): Observable<StatisticsDTO> {
    return this.http.get<StatisticsDTO>(`${this.apiUrl}/stats/today`);
  }

  getMonthlyStats(): Observable<StatisticsDTO> {
    return this.http.get<StatisticsDTO>(`${this.apiUrl}/stats/monthly`);
  }
}
