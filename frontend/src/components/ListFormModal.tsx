"use client";

import { useState, useEffect } from "react";
import { X } from "lucide-react";
import type { ReminderList } from "@/lib/types";
import { listApi } from "@/lib/api";
import { useApp } from "@/lib/context";

const COLORS = [
  "#FF3B30", "#FF9500", "#FFCC00", "#34C759",
  "#007AFF", "#AF52DE", "#FF2D55", "#A2845E",
  "#8E8E93", "#1C1C1E",
];

const ICONS = [
  "list.bullet", "cart", "briefcase", "person",
  "house", "heart", "star", "book",
  "gift", "graduationcap",
];

interface ListFormModalProps {
  editList?: ReminderList | null;
  onClose: () => void;
}

export default function ListFormModal({ editList, onClose }: ListFormModalProps) {
  const { refreshAll } = useApp();
  const [name, setName] = useState("");
  const [color, setColor] = useState(COLORS[4]);
  const [icon, setIcon] = useState(ICONS[0]);

  useEffect(() => {
    if (editList) {
      setName(editList.name);
      setColor(editList.color || COLORS[4]);
      setIcon(editList.icon || ICONS[0]);
    }
  }, [editList]);

  const handleSubmit = async () => {
    const trimmed = name.trim();
    if (!trimmed) return;
    if (editList) {
      await listApi.update(editList.id, { name: trimmed, color, icon });
    } else {
      await listApi.create({ name: trimmed, color, icon });
    }
    await refreshAll();
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/40 z-50 flex items-center justify-center">
      <div className="bg-apple-card rounded-2xl shadow-xl w-[340px] overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between px-4 py-3 border-b border-apple-separator">
          <button
            type="button"
            onClick={onClose}
            className="text-apple-blue text-sm cursor-pointer"
          >
            Cancel
          </button>
          <h3 className="text-sm font-semibold">
            {editList ? "Edit List" : "New List"}
          </h3>
          <button
            type="button"
            onClick={handleSubmit}
            disabled={!name.trim()}
            className="text-apple-blue text-sm font-semibold disabled:opacity-40 cursor-pointer"
          >
            {editList ? "Save" : "Create"}
          </button>
        </div>

        <div className="p-4 space-y-4">
          {/* Preview */}
          <div className="flex justify-center">
            <div
              className="w-20 h-20 rounded-full flex items-center justify-center text-white text-3xl"
              style={{ backgroundColor: color }}
            >
              {icon.charAt(0).toUpperCase()}
            </div>
          </div>

          {/* Name */}
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="List Name"
            autoFocus
            className="w-full text-center text-lg font-medium py-2 px-3 rounded-xl bg-apple-bg outline-none"
          />

          {/* Color palette */}
          <div>
            <p className="text-xs text-apple-text-secondary mb-2">Color</p>
            <div className="flex flex-wrap gap-2">
              {COLORS.map((c) => (
                <button
                  key={c}
                  type="button"
                  onClick={() => setColor(c)}
                  className={`w-7 h-7 rounded-full cursor-pointer transition-transform ${
                    color === c ? "ring-2 ring-offset-2 ring-apple-blue scale-110" : ""
                  }`}
                  style={{ backgroundColor: c }}
                />
              ))}
            </div>
          </div>

          {/* Icon select */}
          <div>
            <p className="text-xs text-apple-text-secondary mb-2">Icon</p>
            <div className="flex flex-wrap gap-2">
              {ICONS.map((ic) => (
                <button
                  key={ic}
                  type="button"
                  onClick={() => setIcon(ic)}
                  className={`w-9 h-9 rounded-lg text-xs flex items-center justify-center cursor-pointer transition-colors ${
                    icon === ic
                      ? "bg-apple-blue text-white"
                      : "bg-apple-bg text-apple-text hover:bg-apple-separator"
                  }`}
                >
                  {ic.slice(0, 3)}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
