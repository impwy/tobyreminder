"use client";

import type { ReactNode } from "react";
import { useApp, type ViewType } from "@/lib/context";

interface SmartListCardProps {
  icon: ReactNode;
  label: string;
  count: number;
  color: string;
  view: ViewType;
}

export default function SmartListCard({
  icon,
  label,
  count,
  color,
  view,
}: SmartListCardProps) {
  const { currentView, setCurrentView, setSelectedReminderId } = useApp();

  const isActive =
    currentView.kind === "smart" &&
    view.kind === "smart" &&
    currentView.filter === view.filter;

  return (
    <button
      type="button"
      onClick={() => {
        setCurrentView(view);
        setSelectedReminderId(null);
      }}
      className={`bg-apple-card rounded-xl p-3 text-left transition-all hover:shadow-md cursor-pointer ${
        isActive ? "ring-2 ring-apple-blue" : ""
      }`}
    >
      <div className="flex items-center justify-between mb-2">
        <div
          className="w-7 h-7 rounded-full flex items-center justify-center"
          style={{ backgroundColor: color }}
        >
          {icon}
        </div>
        <span className="text-xl font-bold text-apple-text">{count}</span>
      </div>
      <p className="text-xs font-medium text-apple-text-secondary">{label}</p>
    </button>
  );
}
