package com.squaregameapi.task.api.adapters.in.web.dto;

import com.squaregameapi.task.api.domain.model.CellColor;
import lombok.Data;

@Data
public class MoveDto {
    private int x;
    private int y;
    private CellColor color;
}
