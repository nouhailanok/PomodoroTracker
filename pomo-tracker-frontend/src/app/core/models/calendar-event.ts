export interface CalendarEventDTO {
  title: string;
  start: string;
  end: string;
  color: string;
  type: 'PLANNED' | 'COMPLETED' | 'INTERRUPTED';
}
