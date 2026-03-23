"use client";

import { useApp } from "@/lib/context";
import ReminderItem from "./ReminderItem";
import NewReminderInput from "./NewReminderInput";

const SMART_LABELS: Record<string, { title: string; color: string }> = {
  today: { title: "Today", color: "#007AFF" },
  scheduled: { title: "Scheduled", color: "#FF3B30" },
  all: { title: "All", color: "#1C1C1E" },
  flagged: { title: "Flagged", color: "#FF9500" },
  completed: { title: "Completed", color: "#8E8E93" },
};

export default function ReminderListView() {
  const { reminders, lists, currentView, loading } = useApp();

  let title = "";
  let color = "#007AFF";
  let listId: number | null = null;

  if (currentView.kind === "smart") {
    const info = SMART_LABELS[currentView.filter];
    title = info.title;
    color = info.color;
  } else if (currentView.kind === "list") {
    const list = lists.find((l) => l.id === currentView.listId);
    title = list?.name ?? "List";
    color = list?.color ?? "#007AFF";
    listId = currentView.listId;
  } else if (currentView.kind === "search") {
    title = `Search: "${currentView.query}"`;
    color = "#8E8E93";
  }

  if (loading) {
    return (
      <div className="flex-1 p-6">
        <div className="skeleton h-9 w-40 mb-6" />
        {[1, 2, 3].map((i) => (
          <div key={i} className="skeleton h-12 w-full mb-2" />
        ))}
      </div>
    );
  }

  return (
    <div className="flex-1 flex flex-col min-w-0">
      {/* Header */}
      <div className="px-5 pt-6 pb-3">
        <h1
          className="text-[34px] font-bold leading-tight"
          style={{ color }}
        >
          {title}
        </h1>
      </div>

      {/* Reminder list */}
      <div className="flex-1 overflow-y-auto">
        {reminders.length === 0 ? (
          <div className="px-5 py-10 text-center text-apple-text-secondary text-sm">
            No Reminders
          </div>
        ) : (
          reminders.map((r) => (
            <ReminderItem
              key={r.id}
              reminder={r}
              color={r.listColor || color}
            />
          ))
        )}

        {/* New Reminder input — only for custom lists */}
        {listId && <NewReminderInput listId={listId} color={color} />}
      </div>
    </div>
  );
}
