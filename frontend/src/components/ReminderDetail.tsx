"use client";

import { useEffect, useState } from "react";
import { X, Trash2 } from "lucide-react";
import { useApp } from "@/lib/context";
import { reminderApi, subtaskApi } from "@/lib/api";
import type { Reminder, Priority, Subtask } from "@/lib/types";
import SubtaskItem from "./SubtaskItem";
import Checkbox from "./Checkbox";

export default function ReminderDetail() {
  const {
    selectedReminderId,
    setSelectedReminderId,
    lists,
    refreshAll,
  } = useApp();
  const [reminder, setReminder] = useState<Reminder | null>(null);
  const [title, setTitle] = useState("");
  const [notes, setNotes] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [dueEnabled, setDueEnabled] = useState(false);
  const [priority, setPriority] = useState<Priority>("NONE");
  const [flagged, setFlagged] = useState(false);
  const [listId, setListId] = useState<number>(0);
  const [subtasks, setSubtasks] = useState<Subtask[]>([]);
  const [newSubtaskTitle, setNewSubtaskTitle] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!selectedReminderId) {
      setReminder(null);
      return;
    }
    reminderApi.findById(selectedReminderId).then((r) => {
      setReminder(r);
      setTitle(r.title);
      setNotes(r.notes ?? "");
      setDueDate(r.dueDate ? r.dueDate.slice(0, 16) : "");
      setDueEnabled(!!r.dueDate);
      setPriority(r.priority);
      setFlagged(r.flagged);
      setListId(r.listId);
      setSubtasks(r.subtasks ?? []);
    });
  }, [selectedReminderId]);

  if (!reminder) return null;

  const save = async () => {
    setSaving(true);
    await reminderApi.update(reminder.id, {
      title,
      notes: notes || null,
      dueDate: dueEnabled && dueDate ? dueDate : null,
      priority,
      flagged,
      listId,
    });
    await refreshAll();
    setSaving(false);
  };

  const handleDelete = async () => {
    await reminderApi.delete(reminder.id);
    setSelectedReminderId(null);
    await refreshAll();
  };

  const handleAddSubtask = async () => {
    const trimmed = newSubtaskTitle.trim();
    if (!trimmed) return;
    const created = await subtaskApi.create(reminder.id, {
      title: trimmed,
      displayOrder: subtasks.length + 1,
    });
    setSubtasks([...subtasks, created]);
    setNewSubtaskTitle("");
    await refreshAll();
  };

  const handleToggleSubtask = async (id: number) => {
    const updated = await subtaskApi.toggleComplete(id);
    setSubtasks(subtasks.map((s) => (s.id === id ? updated : s)));
    await refreshAll();
  };

  const handleDeleteSubtask = async (id: number) => {
    await subtaskApi.delete(id);
    setSubtasks(subtasks.filter((s) => s.id !== id));
    await refreshAll();
  };

  const handleUpdateSubtask = async (id: number, newTitle: string) => {
    const updated = await subtaskApi.update(id, { title: newTitle });
    setSubtasks(subtasks.map((s) => (s.id === id ? updated : s)));
  };

  return (
    <div className="w-[340px] lg:w-[360px] border-l border-apple-separator bg-apple-card flex flex-col detail-slide-in">
      {/* Header */}
      <div className="flex items-center justify-between px-4 py-3 border-b border-apple-separator">
        <h2 className="text-sm font-semibold">Details</h2>
        <button
          type="button"
          onClick={() => setSelectedReminderId(null)}
          className="text-apple-gray hover:text-apple-text cursor-pointer"
        >
          <X size={18} />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {/* Title & Notes */}
        <div className="bg-apple-bg rounded-[10px] p-3 space-y-2">
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            onBlur={save}
            className="w-full text-base font-medium outline-none bg-transparent"
            placeholder="Title"
          />
          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            onBlur={save}
            className="w-full text-sm text-apple-text-secondary outline-none bg-transparent resize-none"
            placeholder="Notes"
            rows={3}
          />
        </div>

        {/* Date */}
        <div className="bg-apple-bg rounded-[10px] p-3">
          <div className="flex items-center justify-between">
            <label className="text-sm">Date</label>
            <button
              type="button"
              onClick={() => {
                setDueEnabled(!dueEnabled);
                if (dueEnabled) {
                  setDueDate("");
                  // Auto-save when toggling off
                  setTimeout(save, 0);
                }
              }}
              className={`w-10 h-6 rounded-full transition-colors cursor-pointer ${
                dueEnabled ? "bg-apple-green" : "bg-apple-separator"
              }`}
            >
              <div
                className={`w-5 h-5 bg-white rounded-full shadow transition-transform ${
                  dueEnabled ? "translate-x-[18px]" : "translate-x-[2px]"
                }`}
              />
            </button>
          </div>
          {dueEnabled && (
            <input
              type="datetime-local"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              onBlur={save}
              className="mt-2 w-full text-sm outline-none bg-transparent text-apple-blue"
            />
          )}
        </div>

        {/* Priority */}
        <div className="bg-apple-bg rounded-[10px] p-3">
          <label className="text-sm block mb-2">Priority</label>
          <div className="flex gap-1">
            {(["NONE", "LOW", "MEDIUM", "HIGH"] as Priority[]).map((p) => (
              <button
                key={p}
                type="button"
                onClick={() => {
                  setPriority(p);
                  setTimeout(save, 0);
                }}
                className={`flex-1 py-1.5 text-xs rounded-lg transition-colors cursor-pointer ${
                  priority === p
                    ? "bg-apple-blue text-white"
                    : "bg-apple-card text-apple-text hover:bg-apple-separator"
                }`}
              >
                {p === "NONE" ? "None" : p.charAt(0) + p.slice(1).toLowerCase()}
              </button>
            ))}
          </div>
        </div>

        {/* Flag */}
        <div className="bg-apple-bg rounded-[10px] p-3 flex items-center justify-between">
          <label className="text-sm">Flag</label>
          <button
            type="button"
            onClick={() => {
              setFlagged(!flagged);
              setTimeout(save, 0);
            }}
            className={`w-10 h-6 rounded-full transition-colors cursor-pointer ${
              flagged ? "bg-apple-orange" : "bg-apple-separator"
            }`}
          >
            <div
              className={`w-5 h-5 bg-white rounded-full shadow transition-transform ${
                flagged ? "translate-x-[18px]" : "translate-x-[2px]"
              }`}
            />
          </button>
        </div>

        {/* List select */}
        <div className="bg-apple-bg rounded-[10px] p-3">
          <label className="text-sm block mb-2">List</label>
          <select
            value={listId}
            onChange={(e) => {
              setListId(Number(e.target.value));
              setTimeout(save, 0);
            }}
            className="w-full text-sm p-2 rounded-lg bg-apple-card outline-none border border-apple-separator"
          >
            {lists.map((l) => (
              <option key={l.id} value={l.id}>
                {l.name}
              </option>
            ))}
          </select>
        </div>

        {/* Subtasks */}
        <div className="bg-apple-bg rounded-[10px] p-3">
          <label className="text-sm block mb-2">
            Subtasks ({subtasks.filter((s) => s.completed).length}/
            {subtasks.length})
          </label>
          <div className="space-y-1">
            {subtasks.map((s) => (
              <SubtaskItem
                key={s.id}
                subtask={s}
                onToggle={() => handleToggleSubtask(s.id)}
                onDelete={() => handleDeleteSubtask(s.id)}
                onUpdate={(newTitle) => handleUpdateSubtask(s.id, newTitle)}
              />
            ))}
          </div>
          <div className="flex items-center gap-2 mt-2">
            <input
              type="text"
              value={newSubtaskTitle}
              onChange={(e) => setNewSubtaskTitle(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") handleAddSubtask();
              }}
              placeholder="Add Subtask"
              className="flex-1 text-sm py-1 px-2 rounded-lg bg-apple-card outline-none border border-apple-separator"
            />
          </div>
        </div>

        {/* Delete button */}
        <button
          type="button"
          onClick={handleDelete}
          className="w-full flex items-center justify-center gap-2 py-2.5 text-sm text-apple-red hover:bg-apple-red/10 rounded-[10px] transition-colors cursor-pointer"
        >
          <Trash2 size={16} />
          <span>Delete Reminder</span>
        </button>
      </div>
    </div>
  );
}
