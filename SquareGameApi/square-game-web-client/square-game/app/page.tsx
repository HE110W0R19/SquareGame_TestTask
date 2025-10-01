"use client";

import { useState } from "react";
import Board from "../components/Board";
import { CellColor, requestNextMove } from "../lib/api";

const makeEmpty = (n: number): CellColor[][] =>
  Array.from({ length: n }, () => Array.from({ length: n }, () => "E"));

export default function HomePage() {
  const [size, setSize] = useState(7);
  const [userColor, setUserColor] = useState<CellColor>("W");
  const [aiColor, setAiColor] = useState<CellColor>("B");
  const [board, setBoard] = useState<CellColor[][]>(makeEmpty(7));
  const [next, setNext] = useState<CellColor>("W");
  const [status, setStatus] = useState("Новая игра");
  const [finished, setFinished] = useState(false);
  const [busy, setBusy] = useState(false);

  const resetGame = () => {
  const n = size >= 3 ? size : 3;
  setBoard(makeEmpty(n));
  setNext(userColor);
  setFinished(false);
  setStatus("Новая игра");
};


  const handleCellClick = async (x: number, y: number) => {
    if (finished || busy) return;
    if (board[y][x] !== "E") return;
    if (next !== userColor) return;

    const afterUser = board.map((row, yy) =>
      row.map((c, xx) => (xx === x && yy === y ? userColor : c))
    );
    setBoard(afterUser);
    setNext(aiColor);
    setBusy(true);
    setStatus("Ход компьютера...");

    try {
      const resp = await requestNextMove(size, aiColor, afterUser);

      if (resp.finished) {
        if (resp.winner === userColor) setStatus("Вы выиграли!");
        else if (resp.winner === aiColor) setStatus("Компьютер выиграл!");
        else setStatus("Ничья");
        setFinished(true);
        setBusy(false);
        return;
      }

      if (resp.move) {
        const { x: cx, y: cy, color } = resp.move;
        const afterAi = afterUser.map((row, yy) =>
          row.map((c, xx) => (xx === cx && yy === cy ? color : c))
        );
        setBoard(afterAi);
        setNext(userColor);
        setStatus("Ваш ход");
      }
    } catch (e: any) {
      setStatus("Ошибка API: " + e.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <main style={{ padding: 20 }}>
      <h1>Squares Game</h1>

      <div style={{ marginBottom: 20 }}>
        <label>Размер поля: </label>
        <input
          type="number"
          min={3}
          max={20}
          value={size}
          onChange={(e) => setSize(Number(e.target.value))}
          disabled={!board.every((row) => row.every((c) => c === "E"))}
        />

        <label style={{ marginLeft: 20 }}>Ваш цвет: </label>
        <select
          value={userColor}
          onChange={(e) => {
            const c = e.target.value as CellColor;
            setUserColor(c);
            setAiColor(c === "W" ? "B" : "W");
          }}
          disabled={!board.every((row) => row.every((c) => c === "E"))}
        >
          <option value="W">Белые ⚪</option>
          <option value="B">Чёрные ⚫</option>
        </select>

        <button onClick={resetGame} style={{ marginLeft: 20 }}>
          Новая игра
        </button>
      </div>

      <p>
        <strong>Статус:</strong> {status} {busy ? "⏳" : ""}
      </p>

      <Board
        size={size}
        board={board}
        onCellClick={handleCellClick}
        disabled={finished || busy}
      />
    </main>
  );
}
