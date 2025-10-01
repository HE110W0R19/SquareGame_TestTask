package com.squaregameapi.task.api.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MoveResponse {
    boolean finished; //Статус игры, закончен или нет
    CellColor winner; //null если нет победителя
    Move move; //следующий ход, если возможен
    String message;
}
