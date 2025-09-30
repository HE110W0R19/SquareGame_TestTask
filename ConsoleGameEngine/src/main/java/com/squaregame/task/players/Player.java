package com.squaregame.task.players;

import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;

public interface Player {
    Color color();

    boolean isHuman();

    Point nextMove();
}
