package com.euler.problem.player.model;

/**
 * Enum for player representation
 */
public enum Player {

    PLAYER_ONE("1"),
    PLAYER_TWO("2");

    private final String code;

    Player(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
