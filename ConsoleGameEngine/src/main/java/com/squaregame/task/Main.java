package com.squaregame.task;

import com.squaregame.task.engine.Game;
import com.squaregame.task.model.Color;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    // REGEX'ы ровно под формат из ТЗ (с пробелами терпимее)
    private static final Pattern GAME_CMD = Pattern.compile(
            "\\s*GAME\\s+(\\d+)\\s*,\\s*(user|comp)\\s+(W|B)\\s*,\\s*(user|comp)\\s+(W|B)\\s*",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern MOVE_CMD = Pattern.compile(
            "\\s*MOVE\\s+(-?\\d+)\\s*,\\s*(-?\\d+)\\s*",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern HELP_CMD = Pattern.compile("\\s*HELP\\s*", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXIT_CMD = Pattern.compile("\\s*EXIT\\s*", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Game game = new Game();

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (EXIT_CMD.matcher(line).matches()) {
                break;
            } else if (HELP_CMD.matcher(line).matches()) {
                printHelp();
                continue;
            }

            Matcher gm = GAME_CMD.matcher(line);
            if (gm.matches()) {
                int n = Integer.parseInt(gm.group(1));
                String t1 = gm.group(2).toLowerCase();
                Color c1 = Color.of(gm.group(3));
                String t2 = gm.group(4).toLowerCase();
                Color c2 = Color.of(gm.group(5));

                String err = game.startNewGame(n, t1, c1, t2, c2);
                if (err != null) {
                    System.out.println(err);
                } else {
                    System.out.println("New game started");
                    game.maybeAutoPlay(); // если первый игрок комп — сразу сделает ход(ы)
                }
                continue;
            }

            Matcher mm = MOVE_CMD.matcher(line);
            if (mm.matches()) {
                int x = Integer.parseInt(mm.group(1));
                int y = Integer.parseInt(mm.group(2));
                String res = game.userMove(x, y);
                if (res != null) System.out.println(res);
                // внутри userMove уже печатаются сообщения окончания игры/ход компа
                continue;
            }

            System.out.println("Incorrect command");
        }
    }

    private static void printHelp() {
        System.out.println("""
                GAME N, U1, U2
                  U: 'user|comp' 'W|B'
                MOVE X, Y
                HELP
                EXIT
                """);
    }
}