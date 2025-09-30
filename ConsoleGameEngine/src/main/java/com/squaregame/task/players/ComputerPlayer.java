package com.squaregame.task.players;

import com.squaregame.task.engine.Board;
import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class ComputerPlayer implements Player {
    private final Color color;
    private final Board board;
    private final Random rand = new Random();

    @Override
    public Color color() {
        return color;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public Point nextMove() {
        throw new UnsupportedOperationException("Implement me!");
    }
}
