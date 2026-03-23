"use client";

import { useState } from "react";
import { X } from "lucide-react";
import Checkbox from "./Checkbox";
import type { Subtask } from "@/lib/types";

interface SubtaskItemProps {
  subtask: Subtask;
  onToggle: () => void;
  onDelete: () => void;
  onUpdate: (title: string) => void;
}

export default function SubtaskItem({
  subtask,
  onToggle,
  onDelete,
  onUpdate,
}: SubtaskItemProps) {
  const [editing, setEditing] = useState(false);
  const [title, setTitle] = useState(subtask.title);

  const handleBlur = () => {
    setEditing(false);
    if (title.trim() && title !== subtask.title) {
      onUpdate(title.trim());
    } else {
      setTitle(subtask.title);
    }
  };

  return (
    <div className="flex items-center gap-2 group py-0.5">
      <Checkbox
        checked={subtask.completed}
        size="sm"
        color="#007AFF"
        onChange={onToggle}
      />
      {editing ? (
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          onBlur={handleBlur}
          onKeyDown={(e) => {
            if (e.key === "Enter") handleBlur();
            if (e.key === "Escape") {
              setTitle(subtask.title);
              setEditing(false);
            }
          }}
          autoFocus
          className="flex-1 text-sm outline-none bg-transparent"
        />
      ) : (
        <span
          onClick={() => setEditing(true)}
          className={`flex-1 text-sm cursor-pointer ${
            subtask.completed ? "line-through text-apple-text-secondary" : ""
          }`}
        >
          {subtask.title}
        </span>
      )}
      <button
        type="button"
        onClick={onDelete}
        className="opacity-0 group-hover:opacity-100 text-apple-gray hover:text-apple-red transition-all cursor-pointer"
      >
        <X size={14} />
      </button>
    </div>
  );
}
