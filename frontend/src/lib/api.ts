import type {
  Reminder,
  ReminderList,
  SmartListCounts,
  Subtask,
  Priority,
} from "./types";

const BASE = "/api";

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE}${url}`, {
    ...init,
    headers: { "Content-Type": "application/json", ...init?.headers },
  });
  if (!res.ok) {
    throw new Error(`API error ${res.status}: ${res.statusText}`);
  }
  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

// ── ReminderList ──

export const listApi = {
  findAll: () => request<ReminderList[]>("/lists"),
  create: (data: {
    name: string;
    color: string;
    icon: string;
    displayOrder?: number;
  }) => request<ReminderList>("/lists", { method: "POST", body: JSON.stringify(data) }),
  update: (id: number, data: {
    name: string;
    color: string;
    icon: string;
    displayOrder?: number;
  }) => request<ReminderList>(`/lists/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  delete: (id: number) => request<void>(`/lists/${id}`, { method: "DELETE" }),
};

// ── Reminder ──

export const reminderApi = {
  findAll: () => request<Reminder[]>("/reminders"),
  findByListId: (listId: number) => request<Reminder[]>(`/reminders?listId=${listId}`),
  findToday: () => request<Reminder[]>("/reminders?today=true"),
  findScheduled: () => request<Reminder[]>("/reminders?scheduled=true"),
  findFlagged: () => request<Reminder[]>("/reminders?flagged=true"),
  findCompleted: () => request<Reminder[]>("/reminders?completed=true"),
  findById: (id: number) => request<Reminder>(`/reminders/${id}`),
  search: (q: string) => request<Reminder[]>(`/reminders/search?q=${encodeURIComponent(q)}`),
  counts: () => request<SmartListCounts>("/reminders/counts"),
  create: (data: {
    title: string;
    notes?: string;
    dueDate?: string;
    priority?: Priority;
    flagged?: boolean;
    displayOrder?: number;
    listId: number;
  }) => request<Reminder>("/reminders", { method: "POST", body: JSON.stringify(data) }),
  update: (id: number, data: {
    title: string;
    notes?: string | null;
    dueDate?: string | null;
    priority?: Priority;
    flagged?: boolean;
    displayOrder?: number;
    listId: number;
  }) => request<Reminder>(`/reminders/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  toggleComplete: (id: number) => request<Reminder>(`/reminders/${id}/complete`, { method: "PATCH" }),
  delete: (id: number) => request<void>(`/reminders/${id}`, { method: "DELETE" }),
};

// ── Subtask ──

export const subtaskApi = {
  findByReminderId: (reminderId: number) =>
    request<Subtask[]>(`/reminders/${reminderId}/subtasks`),
  create: (reminderId: number, data: { title: string; displayOrder?: number }) =>
    request<Subtask>(`/reminders/${reminderId}/subtasks`, {
      method: "POST",
      body: JSON.stringify(data),
    }),
  update: (id: number, data: { title: string; displayOrder?: number }) =>
    request<Subtask>(`/subtasks/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  toggleComplete: (id: number) =>
    request<Subtask>(`/subtasks/${id}/complete`, { method: "PATCH" }),
  delete: (id: number) => request<void>(`/subtasks/${id}`, { method: "DELETE" }),
};
