export interface ReminderList {
  id: number;
  name: string;
  color: string;
  icon: string;
  displayOrder: number;
  createdAt: string;
  updatedAt: string;
}

export type Priority = "NONE" | "LOW" | "MEDIUM" | "HIGH";

export interface Subtask {
  id: number;
  title: string;
  completed: boolean;
  displayOrder: number;
  reminderId: number;
  createdAt: string;
  updatedAt: string;
}

export interface Reminder {
  id: number;
  title: string;
  notes: string | null;
  dueDate: string | null;
  priority: Priority;
  flagged: boolean;
  completed: boolean;
  completedAt: string | null;
  displayOrder: number;
  listId: number;
  listName: string;
  listColor: string;
  subtasks: Subtask[];
  createdAt: string;
  updatedAt: string;
}

export interface SmartListCounts {
  today: number;
  scheduled: number;
  all: number;
  flagged: number;
  completed: number;
}
