package com.squaregameapi.task.api.adapters.out.engine;

import com.squaregame.task.engine.Board;
import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;
import com.squaregame.task.players.ComputerPlayer;
import com.squaregame.task.players.Player;
import com.squaregameapi.task.api.domain.model.BoardState;
import com.squaregameapi.task.api.domain.model.CellColor;
import com.squaregameapi.task.api.domain.model.Move;
import com.squaregameapi.task.api.domain.model.MoveResponse;
import com.squaregameapi.task.api.domain.port.MoveCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EngineMoveCalculator implements MoveCalculator {
    @Override
    public MoveResponse calculate(BoardState state) {
        int n = state.getSize();
        Board board = new Board(n);

        //repair board from BoardState
        for (int y = 0; y < n; y++) {
            var row = state.getBoard().get(y);
            for (int x = 0; x < n; x++) {
                CellColor cellColor = row.get(x);
                if (cellColor == CellColor.E)
                    continue;
                Color color = (cellColor == CellColor.W) ? Color.W : Color.B;
                board.place(x, y, color);
            }
        }

        //Checking game status finished or running
        boolean whiteWin = board.hasSquare(Color.W);
        boolean blackWin = board.hasSquare(Color.B);
        if (whiteWin || blackWin) {
            return MoveResponse.builder()
                    .finished(true)
                    .winner(whiteWin ? CellColor.W : CellColor.B)
                    .move(null)
                    .message("Game finished!")
                    .build();
        }
        if (board.full()) {
            return MoveResponse.builder()
                    .finished(true)
                    .winner(null)
                    .move(null)
                    .message("Draw")
                    .build();
        }

        //Who's gonna make turn
        Color next = (state.getNext() == CellColor.W) ? Color.W : Color.B;

        //Generate PC turn (from task 1 Console Game Engine, we just use random)
        Player player = new ComputerPlayer(next, board);
        Point point = player.nextMove();
        if (point == null) {
            return MoveResponse.builder()
                    .finished(true)
                    .winner(null)
                    .move(null)
                    .message("No moves")
                    .build();
        }

        //accept turn and calculate status
        board.place(point.getX(), point.getY(), next);
        boolean win = board.hasSquare(next);
        boolean full = board.full();

        return MoveResponse.builder()
                .finished(win || full)
                .winner(win ? (next == Color.W ? CellColor.W : CellColor.B) : null)
                .move(new Move(point.getX(), point.getY(), (next == Color.W) ? CellColor.W : CellColor.B))
                .message(win ? "Win" : (full ? "Draw" : "Continue"))
                .build();
    }
}
