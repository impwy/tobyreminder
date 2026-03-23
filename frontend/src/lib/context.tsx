"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";
import type { Reminder, ReminderList, SmartListCounts } from "./types";
import { listApi, reminderApi } from "./api";

export type ViewType =
  | { kind: "smart"; filter: "today" | "scheduled" | "all" | "flagged" | "completed" }
  | { kind: "list"; listId: number }
  | { kind: "search"; query: string };

interface AppState {
  lists: ReminderList[];
  reminders: Reminder[];
  counts: SmartListCounts;
  currentView: ViewType;
  selectedReminderId: number | null;
  loading: boolean;
  setCurrentView: (view: ViewType) => void;
  setSelectedReminderId: (id: number | null) => void;
  refreshLists: () => Promise<void>;
  refreshReminders: () => Promise<void>;
  refreshCounts: () => Promise<void>;
  refreshAll: () => Promise<void>;
}

const AppContext = createContext<AppState | null>(null);

export function AppProvider({ children }: { children: ReactNode }) {
  const [lists, setLists] = useState<ReminderList[]>([]);
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [counts, setCounts] = useState<SmartListCounts>({
    today: 0,
    scheduled: 0,
    all: 0,
    flagged: 0,
    completed: 0,
  });
  const [currentView, setCurrentView] = useState<ViewType>({
    kind: "smart",
    filter: "all",
  });
  const [selectedReminderId, setSelectedReminderId] = useState<number | null>(
    null
  );
  const [loading, setLoading] = useState(true);

  const refreshLists = useCallback(async () => {
    const data = await listApi.findAll();
    setLists(data);
  }, []);

  const refreshCounts = useCallback(async () => {
    const data = await reminderApi.counts();
    setCounts(data);
  }, []);

  const refreshReminders = useCallback(async () => {
    let data: Reminder[];
    switch (currentView.kind) {
      case "smart":
        switch (currentView.filter) {
          case "today":
            data = await reminderApi.findToday();
            break;
          case "scheduled":
            data = await reminderApi.findScheduled();
            break;
          case "flagged":
            data = await reminderApi.findFlagged();
            break;
          case "completed":
            data = await reminderApi.findCompleted();
            break;
          default:
            data = await reminderApi.findAll();
        }
        break;
      case "list":
        data = await reminderApi.findByListId(currentView.listId);
        break;
      case "search":
        data = currentView.query
          ? await reminderApi.search(currentView.query)
          : [];
        break;
    }
    setReminders(data);
  }, [currentView]);

  const refreshAll = useCallback(async () => {
    setLoading(true);
    await Promise.all([refreshLists(), refreshReminders(), refreshCounts()]);
    setLoading(false);
  }, [refreshLists, refreshReminders, refreshCounts]);

  useEffect(() => {
    refreshAll();
  }, [refreshAll]);

  useEffect(() => {
    refreshReminders();
  }, [currentView, refreshReminders]);

  return (
    <AppContext.Provider
      value={{
        lists,
        reminders,
        counts,
        currentView,
        selectedReminderId,
        loading,
        setCurrentView,
        setSelectedReminderId,
        refreshLists,
        refreshReminders,
        refreshCounts,
        refreshAll,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useApp() {
  const ctx = useContext(AppContext);
  if (!ctx) throw new Error("useApp must be inside AppProvider");
  return ctx;
}
