package com.squaregameapi.task.api.application;

import com.squaregameapi.task.api.domain.model.BoardState;
import com.squaregameapi.task.api.domain.model.MoveResponse;
import com.squaregameapi.task.api.domain.port.MoveCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateNextMoveUseCase {
    private final MoveCalculator calculator;

    public MoveResponse handle(BoardState state) {
        return calculator.calculate(state);
    }
}
