package com.squaregame.task.model;

public enum Color {
    WHITE,
    BLACK;

    public static Color of(String color) {
        return "WHITE".equals(color) ? Color.WHITE : Color.BLACK;
    }

    @Override
    public String toString() {
        return name();
    }
}
