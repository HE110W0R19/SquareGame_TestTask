package com.squaregame.task.model;

public enum Color {
    W,
    B;

    public static Color of(String color) {
        return "W".equals(color) ? Color.W : Color.B;
    }

    @Override
    public String toString() {
        return name();
    }
}
