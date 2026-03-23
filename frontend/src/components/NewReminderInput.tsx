"use client";

import { useState, useRef } from "react";
import { Plus } from "lucide-react";
import { reminderApi } from "@/lib/api";
import { useApp } from "@/lib/context";

interface NewReminderInputProps {
  listId: number;
  color: string;
}

export default function NewReminderInput({
  listId,
  color,
}: NewReminderInputProps) {
  const [editing, setEditing] = useState(false);
  const [title, setTitle] = useState("");
  const inputRef = useRef<HTMLInputElement>(null);
  const submittingRef = useRef(false);
  const { refreshAll } = useApp();

  const handleSubmit = async () => {
    if (submittingRef.current) return;
    const trimmed = title.trim();
    if (!trimmed) {
      setEditing(false);
      return;
    }
    submittingRef.current = true;
    try {
      await reminderApi.create({
        title: trimmed,
        listId,
        priority: "NONE",
        flagged: false,
      });
      setTitle("");
      await refreshAll();
      inputRef.current?.focus();
    } catch {
      setEditing(false);
      setTitle("");
    } finally {
      submittingRef.current = false;
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") handleSubmit();
    if (e.key === "Escape") {
      setTitle("");
      setEditing(false);
    }
  };

  if (!editing) {
    return (
      <button
        type="button"
        onClick={() => setEditing(true)}
        className="flex items-center gap-1.5 px-4 py-2.5 text-sm cursor-pointer transition-colors hover:bg-apple-bg"
        style={{ color }}
      >
        <Plus size={16} />
        <span>New Reminder</span>
      </button>
    );
  }

  return (
    <div className="flex items-center gap-3 px-4 py-2.5 border-b border-apple-separator">
      <div
        className="w-[22px] h-[22px] rounded-full border-2 flex-shrink-0"
        style={{ borderColor: "#C7C7CC" }}
      />
      <input
        ref={inputRef}
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={handleKeyDown}
        onBlur={handleSubmit}
        placeholder="New Reminder"
        autoFocus
        className="flex-1 text-[15px] outline-none bg-transparent"
      />
    </div>
  );
}
