package com.squaregameapi.task.api.adapters.in.web;

import com.squaregameapi.task.api.adapters.in.web.dto.MoveDto;
import com.squaregameapi.task.api.adapters.in.web.dto.MoveRequestDto;
import com.squaregameapi.task.api.adapters.in.web.dto.MoveResponseDto;
import com.squaregameapi.task.api.application.CalculateNextMoveUseCase;
import com.squaregameapi.task.api.domain.model.BoardState;
import com.squaregameapi.task.api.domain.model.MoveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MoveController {
    private final CalculateNextMoveUseCase useCase;

    @PostMapping(value = "/next-move",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MoveResponseDto next(@Validated @RequestBody MoveRequestDto request) {
        var state = BoardState.builder()
                .size(request.getSize())
                .next(request.getNext())
                .board(request.getBoard())
                .build();

        MoveResponse response = useCase.handle(state);

        var out = new MoveResponseDto();
        out.setFinished(response.isFinished());
        out.setWinner(response.getWinner());
        out.setMessage(response.getMessage());

        if (response.getMove() != null) {
            var move = new MoveDto();
            move.setX(response.getMove().getX());
            move.setY(response.getMove().getY());
            move.setColor(response.getMove().getColor());
            out.setMove(move);
        }
        return out;
    }
}
