package com.squaregame.task.players;

import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserPlayer implements Player {
    private final Color color;

    @Override
    public Color color() {
        return color;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public Point nextMove() {
        return null;
        //Для пользователя ход осуществляется через команду MOVE
    }
}
