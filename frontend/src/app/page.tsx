"use client";

import { useState } from "react";
import { Menu } from "lucide-react";
import { AppProvider, useApp } from "@/lib/context";
import Sidebar from "@/components/Sidebar";
import ReminderListView from "@/components/ReminderListView";
import ReminderDetail from "@/components/ReminderDetail";
import ListFormModal from "@/components/ListFormModal";
import type { ReminderList } from "@/lib/types";

function AppContent() {
  const {
    selectedReminderId,
    setSelectedReminderId,
    lists,
    currentView,
    setCurrentView,
    refreshAll,
  } = useApp();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [showListForm, setShowListForm] = useState(false);
  const [editList, setEditList] = useState<ReminderList | null>(null);

  const handleEditList = () => {
    if (currentView.kind === "list") {
      const list = lists.find((l) => l.id === currentView.listId);
      if (list) {
        setEditList(list);
        setShowListForm(true);
      }
    }
  };

  const handleDeleteList = async () => {
    if (currentView.kind === "list") {
      const { listApi } = await import("@/lib/api");
      await listApi.delete(currentView.listId);
      setCurrentView({ kind: "smart", filter: "all" });
      await refreshAll();
    }
  };

  return (
    <div className="h-full flex">
      <Sidebar
        open={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        onAddList={() => {
          setEditList(null);
          setShowListForm(true);
        }}
      />

      <div className="flex-1 flex flex-col min-w-0">
        {/* Mobile header */}
        <div className="lg:hidden flex items-center gap-3 px-4 py-3 border-b border-apple-separator bg-apple-card">
          <button
            type="button"
            onClick={() => setSidebarOpen(true)}
            className="text-apple-blue cursor-pointer"
          >
            <Menu size={22} />
          </button>
          <span className="font-semibold text-sm">Toby Reminder</span>
        </div>

        <div className="flex-1 flex overflow-hidden">
          <div className="flex-1 flex flex-col bg-apple-card min-w-0">
            {/* List action bar for custom lists */}
            {currentView.kind === "list" && (
              <div className="flex items-center justify-end gap-2 px-4 pt-2 text-xs">
                <button
                  type="button"
                  onClick={handleEditList}
                  className="text-apple-blue hover:underline cursor-pointer"
                >
                  Edit
                </button>
                <button
                  type="button"
                  onClick={handleDeleteList}
                  className="text-apple-red hover:underline cursor-pointer"
                >
                  Delete
                </button>
              </div>
            )}
            <ReminderListView />
          </div>

          {/* Detail panel — desktop inline, tablet/mobile overlay */}
          {selectedReminderId && (
            <>
              <div className="hidden lg:block">
                <ReminderDetail />
              </div>
              <div className="lg:hidden fixed inset-0 z-40 flex justify-end">
                <div
                  className="flex-1 bg-black/30"
                  onClick={() => setSelectedReminderId(null)}
                />
                <ReminderDetail />
              </div>
            </>
          )}
        </div>
      </div>

      {showListForm && (
        <ListFormModal
          editList={editList}
          onClose={() => setShowListForm(false)}
        />
      )}
    </div>
  );
}

export default function Home() {
  return (
    <AppProvider>
      <AppContent />
    </AppProvider>
  );
}
