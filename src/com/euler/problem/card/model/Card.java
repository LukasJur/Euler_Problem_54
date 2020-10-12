package com.euler.problem.card.model;

/**
 * Class for representing in-game cards
 */
public class Card {

    private Integer number;
    private Suit suit;

    public Card(Integer number, Suit suit) {
        this.number = number;
        this.suit = suit;
    }

    public Integer getNumber() {
        return number;
    }

    public Suit getSuit() {
        return suit;
    }

}
