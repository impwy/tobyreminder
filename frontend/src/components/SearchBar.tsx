"use client";

import { useState, useEffect, useRef } from "react";
import { Search, X } from "lucide-react";
import { useApp } from "@/lib/context";

export default function SearchBar() {
  const { setCurrentView, currentView } = useApp();
  const [query, setQuery] = useState("");
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    if (timerRef.current) clearTimeout(timerRef.current);
    if (!query) return;
    timerRef.current = setTimeout(() => {
      setCurrentView({ kind: "search", query });
    }, 300);
    return () => {
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, [query, setCurrentView]);

  useEffect(() => {
    if (currentView.kind !== "search") {
      setQuery("");
    }
  }, [currentView]);

  const clear = () => {
    setQuery("");
    setCurrentView({ kind: "smart", filter: "all" });
  };

  return (
    <div className="relative mx-3 mb-3">
      <Search
        size={14}
        className="absolute left-2.5 top-1/2 -translate-y-1/2 text-apple-gray"
      />
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search"
        className="w-full pl-8 pr-8 py-1.5 rounded-lg text-sm bg-apple-search-bg outline-none placeholder:text-apple-gray"
      />
      {query && (
        <button
          type="button"
          onClick={clear}
          className="absolute right-2 top-1/2 -translate-y-1/2 text-apple-gray cursor-pointer"
        >
          <X size={14} />
        </button>
      )}
    </div>
  );
}
