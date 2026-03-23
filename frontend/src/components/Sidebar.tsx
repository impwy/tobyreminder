"use client";

import {
  Calendar,
  CalendarDays,
  Inbox,
  Flag,
  CheckCircle2,
  Plus,
  Menu,
} from "lucide-react";
import { useApp } from "@/lib/context";
import SmartListCard from "./SmartListCard";
import SearchBar from "./SearchBar";

interface SidebarProps {
  open: boolean;
  onClose: () => void;
  onAddList: () => void;
}

export default function Sidebar({ open, onClose, onAddList }: SidebarProps) {
  const { lists, counts, currentView, setCurrentView, setSelectedReminderId } =
    useApp();

  const iconSize = 15;
  const iconColor = "white";

  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div
          className="fixed inset-0 bg-black/30 z-30 lg:hidden"
          onClick={onClose}
        />
      )}

      <aside
        className={`fixed lg:static z-40 top-0 left-0 h-full w-[280px] bg-apple-bg border-r border-apple-separator flex flex-col transition-transform duration-300 ${
          open ? "translate-x-0" : "-translate-x-full lg:translate-x-0"
        }`}
      >
        <div className="pt-4 pb-1">
          <SearchBar />
        </div>

        {/* Smart list cards */}
        <div className="grid grid-cols-2 gap-2 px-3 mb-4">
          <SmartListCard
            icon={<Calendar size={iconSize} color={iconColor} />}
            label="Today"
            count={counts.today}
            color="#007AFF"
            view={{ kind: "smart", filter: "today" }}
          />
          <SmartListCard
            icon={<CalendarDays size={iconSize} color={iconColor} />}
            label="Scheduled"
            count={counts.scheduled}
            color="#FF3B30"
            view={{ kind: "smart", filter: "scheduled" }}
          />
          <SmartListCard
            icon={<Inbox size={iconSize} color={iconColor} />}
            label="All"
            count={counts.all}
            color="#1C1C1E"
            view={{ kind: "smart", filter: "all" }}
          />
          <SmartListCard
            icon={<Flag size={iconSize} color={iconColor} />}
            label="Flagged"
            count={counts.flagged}
            color="#FF9500"
            view={{ kind: "smart", filter: "flagged" }}
          />
          <SmartListCard
            icon={<CheckCircle2 size={iconSize} color={iconColor} />}
            label="Completed"
            count={counts.completed}
            color="#8E8E93"
            view={{ kind: "smart", filter: "completed" }}
          />
        </div>

        {/* My Lists */}
        <div className="flex-1 overflow-y-auto px-3">
          <h3 className="text-xs font-semibold text-apple-text-secondary uppercase tracking-wider mb-2 px-2">
            My Lists
          </h3>
          <ul className="space-y-0.5">
            {lists.map((list) => {
              const isActive =
                currentView.kind === "list" &&
                currentView.listId === list.id;
              return (
                <li key={list.id}>
                  <button
                    type="button"
                    onClick={() => {
                      setCurrentView({ kind: "list", listId: list.id });
                      setSelectedReminderId(null);
                      onClose();
                    }}
                    className={`w-full flex items-center gap-2.5 px-2 py-1.5 rounded-lg text-sm transition-colors cursor-pointer ${
                      isActive
                        ? "bg-apple-blue/10 text-apple-blue"
                        : "hover:bg-apple-separator/50"
                    }`}
                  >
                    <span
                      className="w-3 h-3 rounded-full flex-shrink-0"
                      style={{ backgroundColor: list.color }}
                    />
                    <span className="flex-1 text-left truncate">
                      {list.name}
                    </span>
                  </button>
                </li>
              );
            })}
          </ul>
        </div>

        {/* Add List button */}
        <div className="p-3 border-t border-apple-separator">
          <button
            type="button"
            onClick={onAddList}
            className="flex items-center gap-2 text-sm text-apple-blue hover:text-apple-blue/80 transition-colors cursor-pointer"
          >
            <Plus size={16} />
            <span>Add List</span>
          </button>
        </div>
      </aside>
    </>
  );
}
