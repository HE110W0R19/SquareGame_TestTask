package com.squaregameapi.task.api.adapters.out.engine;

import com.squaregameapi.task.api.domain.model.BoardState;
import com.squaregameapi.task.api.domain.model.MoveResponse;
import com.squaregameapi.task.api.domain.port.MoveCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EngineMoveCalculator implements MoveCalculator {
    @Override
    public MoveResponse calculate(BoardState board) {
        return null;
    }
}
