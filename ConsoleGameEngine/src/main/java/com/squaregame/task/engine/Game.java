package com.squaregame.task.engine;

import com.squaregame.task.model.Color;
import com.squaregame.task.model.Point;
import com.squaregame.task.players.ComputerPlayer;
import com.squaregame.task.players.Player;
import com.squaregame.task.players.UserPlayer;

public class Game {
    private GameState gameState = GameState.NOT_STARTED;
    private Player player1, player2;
    private Player move;
    private Board board;

    public String startNewGame(int N, String p1, Color c1, String p2, Color c2) {
        if (N < 2)
            return "Incorrect board size!";
        if (c1 == c2)
            return "Incorrect colors, cannot be two " + c1 + "/" + c2 + "!";
        this.board = new Board(N);
        this.player1 = "user".equals(p1) ? new UserPlayer(c1) : new ComputerPlayer(c1, board);
        this.player2 = "user".equals(p2) ? new UserPlayer(c1) : new ComputerPlayer(c1, board);
        this.move = player1;
        this.gameState = GameState.RUNNING;
        return null;
    }

    public String placeAndProgress(int x, int y) {
        if (!board.in(x, y))
            return "Out of board!";
        if (!board.isEmpty(x, y))
            return "Cell is occupied!";

        Color color = move.color();
        board.place(x, y, color);

        if (!move.isHuman()) {
            //Логи хода ПК
            System.out.printf("%s (%d, %d)%n", color, x, y);
        }
        if (board.hasSquare(color)) {
            System.out.printf("game finished! %s win!%n", color);
            gameState = GameState.FINISHED;
            return null;
        }
        if (board.full()) {
            finishGame();
            return null;
        }
        //Меняем ход на другого игрока
        move = (move == player1) ? player2 : player1;
        return null;
    }

    public String userMove(int x, int y) {
        if (gameState == GameState.NOT_STARTED)
            return "Game is not started!";
        if (gameState == GameState.FINISHED)
            return "Game is finished!";
        if (!move.isHuman())
            return "Not your turn!";

        String err = placeAndProgress(x, y);
        if (err != null)
            return err;

        //Дальше играет ПК
        maybeAutoPlay();
        return null;
    }

    private void maybeAutoPlay() {
        if (gameState != GameState.RUNNING)
            return;
        while (!move.isHuman() && gameState == GameState.RUNNING) {
            Point m = move.nextMove();
            if (m == null) {
                finishGame();
                return;
            }
            String err = placeAndProgress(m.getX(), m.getY());
            if (err != null) {
                // Если поле занято, то ищем другое
                continue;
            }
        }
    }

    private void finishGame() {
        System.out.println("Game finished!");
        gameState = GameState.FINISHED;
    }
}
