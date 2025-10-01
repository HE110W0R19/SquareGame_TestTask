"use client";

import React from "react";
import { CellColor } from "../lib/api";

interface BoardProps {
  size: number;
  board: CellColor[][];
  onCellClick: (x: number, y: number) => void;
  disabled?: boolean;
}

export default function Board({ size, board, onCellClick, disabled }: BoardProps) {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: `repeat(${size}, 40px)`,
        gap: "6px",
        padding: "12px",
        background: "#f2f2f2",
        borderRadius: "8px",
        width: "fit-content",
      }}
    >
      {board.map((row, y) =>
        row.map((cell, x) => (
          <button
            key={`${x}-${y}`}
            style={{
              width: "40px",
              height: "40px",
              borderRadius: "6px",
              border: "1px solid #ccc",
              fontSize: "20px",
              cursor: disabled || cell !== "E" ? "default" : "pointer",
              background: cell === "W" ? "#fff" : cell === "B" ? "#000" : "#eee",
              color: cell === "B" ? "#fff" : "#000",
            }}
            disabled={disabled || cell !== "E"}
            onClick={() => onCellClick(x, y)}
          >
            {cell === "W" ? "⚪" : cell === "B" ? "⚫" : ""}
          </button>
        ))
      )}
    </div>
  );
}
