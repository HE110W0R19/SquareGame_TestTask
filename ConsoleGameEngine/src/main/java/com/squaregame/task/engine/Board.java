package com.squaregame.task.engine;

import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;

import java.util.*;

public class Board {
    private final int N;
    private final Color[][] cells;
    private final Map<Color, Set<Point>> byColor = new EnumMap<>(Color.class);

    public Board(int N) {
        this.N = N;
        this.cells = new Color[N][N];
        byColor.put(Color.B, new HashSet<>());
        byColor.put(Color.W, new HashSet<>());
    }

    public int size() {
        return N;
    }

    public boolean in(int x, int y) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }

    public boolean isEmpty(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    public boolean place(int x, int y, Color c) {
        if (!in(x, y) || cells[y][x] != null) return false;
        cells[y][x] = c;
        byColor.get(c).add(new Point(x, y));
        return true;
    }

    public boolean full() {
        for (int y = 0; y < N; y++)
            for (int x = 0; x < N; x++)
                if (cells[y][x] == null) return false;
        return true;
    }

    public boolean hasSquare(Color c) {
        // Классический O(k^2) через поворот вектора (dx,dy):
        //   A(x1,y1), B(x2,y2) -> C = A - (dy,-dx), D = B - (dy, -dx) и вариант с +(dy, -dx).
        // Dx, dy != (0,0). Все 4 точки должны существовать.
        var pts = byColor.get(c);
        if (pts.size() < 4) return false;
        var arr = pts.toArray(new Point[0]);
        var set = pts; // HashSet для O(1)
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                int dx = arr[j].getX() - arr[i].getX();
                int dy = arr[j].getY() - arr[i].getY();
                if (dx == 0 && dy == 0) continue;

                // вращение вектора на 90°: (dx, dy) -> (-dy, dx)
                Point c1 = new Point(arr[i].getX() - dy, arr[i].getY() + dx);
                Point d1 = new Point(arr[j].getX() - dy, arr[j].getY() + dx);
                if (in(c1.getX(), c1.getY()) && in(d1.getX(), d1.getY()) && set.contains(c1) && set.contains(d1))
                    return true;

                // противоположная ориентация: (dx, dy) -> (dy, -dx)
                Point c2 = new Point(arr[i].getX() + dy, arr[i].getY() - dx);
                Point d2 = new Point(arr[j].getX() + dy, arr[j].getY() - dx);
                if (in(c2.getX(), c2.getY()) && in(d2.getX(), d2.getY()) && set.contains(c2) && set.contains(d2))
                    return true;
            }
        }
        return false;
    }

    public List<Point> emptyCells() {
        List<Point> emptyCells = new ArrayList<>();
        for (int y = 0; y < N; ++y) {
            for (int x = 0; x < N; ++x) {
                if (cells[y][x] == null)
                    emptyCells.add(new Point(x, y));
            }
        }
        return emptyCells;
    }
}
