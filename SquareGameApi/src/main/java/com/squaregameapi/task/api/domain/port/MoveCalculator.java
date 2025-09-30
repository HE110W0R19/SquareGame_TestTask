package com.squaregameapi.task.api.domain.port;

import com.squaregameapi.task.api.domain.model.BoardState;
import com.squaregameapi.task.api.domain.model.MoveResponse;

public interface MoveCalculator {
    MoveResponse calculate(BoardState board);
}
