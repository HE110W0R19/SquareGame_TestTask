package com.squaregameapi.task.api.adapters.in.web.dto;

import com.squaregameapi.task.api.domain.model.CellColor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MoveRequestDto{
    @Min(3)
    private int size;

    @NotNull
    private CellColor next;

    @NotNull
    private List<List<CellColor>> board;
}
