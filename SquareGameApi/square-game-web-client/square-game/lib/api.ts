export type CellColor = "W" | "B" | "E";

export interface MoveResponse {
  finished: boolean;
  winner: CellColor | null;
  move: { x: number; y: number; color: CellColor } | null;
  message: string;
}

export async function requestNextMove(
  size: number,
  next: CellColor,
  board: CellColor[][]
): Promise<MoveResponse> {
  const res = await fetch("http://localhost:8080/api/v1/next-move", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ size, next, board }),
  });

  if (!res.ok) {
    throw new Error(`API error: ${res.statusText}`);
  }

  return res.json();
}
