package com.euler.problem.card.model;

/**
 * Enum to represent different card types (suits)
 */
public enum Suit {

    Clubs('C'),
    Hearts('H'),
    Spades('S'),
    Diamonds('D');

    private final char code;

    Suit(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static Suit resolveFromCode(char code) {
        for (Suit e : values()) {
            if (Character.compare(e.code, code) == 0) {
                return e;
            }
        }
        return null;
    }

}
