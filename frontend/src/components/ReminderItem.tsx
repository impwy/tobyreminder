"use client";

import { Flag } from "lucide-react";
import Checkbox from "./Checkbox";
import { useApp } from "@/lib/context";
import { reminderApi } from "@/lib/api";
import type { Reminder } from "@/lib/types";

interface ReminderItemProps {
  reminder: Reminder;
  color: string;
}

export default function ReminderItem({ reminder, color }: ReminderItemProps) {
  const { selectedReminderId, setSelectedReminderId, refreshAll } = useApp();
  const isSelected = selectedReminderId === reminder.id;

  const handleToggle = async () => {
    await reminderApi.toggleComplete(reminder.id);
    await refreshAll();
  };

  const formatDate = (dateStr: string | null) => {
    if (!dateStr) return null;
    const d = new Date(dateStr);
    const today = new Date();
    const isToday = d.toDateString() === today.toDateString();
    if (isToday) return "Today";
    return d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
  };

  return (
    <div
      onClick={() => setSelectedReminderId(reminder.id)}
      className={`flex items-start gap-3 px-4 py-2.5 border-b border-apple-separator cursor-pointer transition-colors ${
        isSelected ? "bg-apple-blue/5" : "hover:bg-apple-bg"
      }`}
    >
      <div className="pt-0.5">
        <Checkbox
          checked={reminder.completed}
          color={color}
          onChange={handleToggle}
        />
      </div>

      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-1.5">
          <span
            className={`text-[15px] leading-snug truncate ${
              reminder.completed
                ? "line-through text-apple-text-secondary"
                : ""
            }`}
          >
            {reminder.title}
          </span>
          {reminder.flagged && (
            <Flag
              size={12}
              className="flex-shrink-0 text-apple-orange fill-apple-orange"
            />
          )}
        </div>
        {(reminder.notes || reminder.dueDate) && (
          <div className="flex items-center gap-2 mt-0.5 text-xs text-apple-text-secondary">
            {reminder.notes && (
              <span className="truncate">{reminder.notes}</span>
            )}
            {reminder.dueDate && (
              <span className="flex-shrink-0">
                {formatDate(reminder.dueDate)}
              </span>
            )}
          </div>
        )}
        {reminder.subtasks && reminder.subtasks.length > 0 && (
          <div className="mt-0.5 text-xs text-apple-text-secondary">
            {reminder.subtasks.filter((s) => s.completed).length}/
            {reminder.subtasks.length} subtasks
          </div>
        )}
      </div>
    </div>
  );
}
