package com.squaregame.task;

import com.squaregame.task.engine.Game;
import com.squaregame.task.engine.GameStarter;
import com.squaregame.task.model.Color;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        new GameStarter().start();
    }
}