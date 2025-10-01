package com.squaregameapi.task.api.domain.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BoardState {
    int size;
    CellColor next;
    @Singular("row")
    List<List<CellColor>> board;
}
