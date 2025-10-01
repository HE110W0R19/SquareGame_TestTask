package com.squaregameapi.task.api.adapters.in.web.dto;

import com.squaregameapi.task.api.domain.model.CellColor;
import lombok.Data;

@Data
public class MoveResponseDto {
    private boolean finished;
    private CellColor winner;
    private MoveDto move;
    private String message;

    @Data
    public static class MoveDto {
        private int x;
        private int y;
        private CellColor color;
    }
}
