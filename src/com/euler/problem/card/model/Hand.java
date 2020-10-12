package com.euler.problem.card.model;

/**
 * Enum to represent hand combinations
 */
public enum Hand {

    ROYAL_FLUSH(9),
    STRAIGHT_FLUSH(8),
    FOUR_OF_A_KIND(7),
    FULL_HOUSE(6),
    FLUSH(5),
    STRAIGHT(4),
    THREE_OF_A_KIND(3),
    TWO_PAIRS(2),
    ONE_PAIR(1),
    HIGH_CARD(0);

    /**
     * Power of hand
     * The higher the number, the more powerful the hand
     */
    private final int power;

    Hand(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
