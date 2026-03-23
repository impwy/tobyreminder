"use client";

import { useState } from "react";

interface CheckboxProps {
  checked: boolean;
  color?: string;
  size?: "sm" | "md";
  onChange: () => void;
}

export default function Checkbox({
  checked,
  color = "#007AFF",
  size = "md",
  onChange,
}: CheckboxProps) {
  const [animating, setAnimating] = useState(false);
  const dim = size === "sm" ? 18 : 22;

  const handleClick = () => {
    setAnimating(true);
    setTimeout(() => {
      onChange();
      setAnimating(false);
    }, 300);
  };

  return (
    <button
      type="button"
      onClick={handleClick}
      className={`flex-shrink-0 rounded-full border-2 flex items-center justify-center transition-all cursor-pointer ${
        animating ? "checkbox-animate" : ""
      }`}
      style={{
        width: dim,
        height: dim,
        borderColor: checked || animating ? color : "#C7C7CC",
        backgroundColor: checked || animating ? color : "transparent",
      }}
    >
      {(checked || animating) && (
        <svg
          width={dim * 0.55}
          height={dim * 0.55}
          viewBox="0 0 12 12"
          fill="none"
          stroke="white"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M2 6l3 3 5-5" />
        </svg>
      )}
    </button>
  );
}
