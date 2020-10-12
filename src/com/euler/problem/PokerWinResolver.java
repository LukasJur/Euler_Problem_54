package com.euler.problem;

import com.euler.problem.card.model.Card;
import com.euler.problem.card.model.Hand;
import com.euler.problem.card.model.Suit;
import com.euler.problem.player.model.Player;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Poker hands
 * Problem 54
 *
 * In the card game poker, a hand consists of five cards and are ranked, from
 * lowest to highest, in the following way:
 *
 * High Card: Highest value card.
 * One Pair: Two cards of the same value.
 * Two Pairs: Two different pairs.
 * Three of a Kind: Three cards of the same value.
 * Straight: All cards are consecutive values.
 * Flush: All cards of the same suit.
 * Full House: Three of a kind and a pair
 * Four of a Kind: Four cards of the same value.
 * Straight Flush: All cards are consecutive values of same suit.
 * Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
 *
 * The cards are valued in the order:
 * 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
 *
 * If two players have the same ranked hands then the rank made up of the
 * highest value wins; for example, a pair of eights beats a pair of fives (see
 * example 1 below). But if two ranks tie, for example, both players have a pair
 * of queens, then highest cards in each hand are compared (see example 4
 * below); if the highest cards tie then the next highest cards are compared,
 * and so on.
 *
 * Consider the following five hands dealt to two players:
 * Hand		Player 1			Player 2				Winner
 * 1		5H 5C 6S 7S KD		2C 3S 8S 8D TD			Player 2
 * 			Pair of Fives		Pair of Eights
 * 2		5D 8C 9S JS AC		2C 5C 7D 8S QH			Player 1
 * 			Highest card Ace	Highest card Queen
 * 3		2D 9C AS AH AC		3D 6D 7D TD QD			Player 2
 * 			Three Aces			Flush with Diamonds
 * 4		4D 6S 9H QH QC		3D 6D 7H QD QS			Player 1
 * 			Pair of Queens		Pair of Queens
 * 			Highest card Nine	Highest card Seven
 * 5		2H 2D 4C 4D 4S		3C 3D 3S 9S 9D			Player 1
 * 			Full House			Full House
 * 			With Three Fours	with Three Threes
 *
 * The file, poker.txt, contains one-thousand random hands dealt to two players.
 * Each line of the file contains ten cards (separated by a single space): the
 * first five are Player 1's cards and the last five are Player 2's cards. You
 * can assume that all hands are valid (no invalid characters or repeated
 * cards), each player's hand is in no specific order, and in each hand there is
 * a clear winner.
 *
 * How many hands does Player 1 win?
 */
public class PokerWinResolver {

    private static final String SPACE = " ";
    // Using plain Java 8, so no Map.of or Guava
    private static final Map<Character, Integer> CARD_NUMBER_MAP = new HashMap<Character, Integer>(){{
        put('A', 12);
        put('K', 11);
        put('Q', 10);
        put('J', 9);
        put('T', 8);
        put('9', 7);
        put('8', 6);
        put('7', 5);
        put('6', 4);
        put('5', 3);
        put('4', 2);
        put('3', 1);
        put('2', 0);
    }};
    private static final Set<Integer> ROYAL_FLUSH_NUMBERS = new HashSet<>(Arrays.asList(12, 11, 10, 9, 8));

    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        // Pass the desired player name (number) to args or use the default
        String playerName = args.length < 2 ? "1" :  args[0];
        String fileName = args.length < 2 ? System.getProperty("user.dir") + "\\poker.txt" :  args[1];

        File input = new File(fileName);
        assert (input.canRead());
        int result = 0;
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        while (null != (line = in.readLine())) {
            Player winner = resolveWinner(line);

            if (winner.getCode().equals(playerName)) {
                result++;
            }
        }

        in.close();
        long end = System.nanoTime();
        long runtime = end - start;
        System.out.println("Runtime: " + runtime / 1000000 + "ms");
        System.out.println(String.format("Player %s wins %d games ", playerName, result));
    }

    private static Player resolveWinner(String line){
        List<Card> cards = Arrays.stream(line.split(SPACE))
                .map(PokerWinResolver::stringToCard)
                .collect(Collectors.toList());
        int size = cards.size();
        List<Card> playerOneCards = new ArrayList<>(cards.subList(0, (size + 1)/2));
        List<Card> playerTwoCards = new ArrayList<>(cards.subList((size + 1)/2, size));
        Hand playerOneHand = findBestHand(playerOneCards);
        Hand playerTwoHand = findBestHand(playerTwoCards);
        switch(Integer.compare(playerOneHand.getPower(), playerTwoHand.getPower())){
            case 1:
                return Player.PLAYER_ONE;
            case -1:
                return Player.PLAYER_TWO;
            default:
                return findHighestCardPlayer(playerOneCards, playerTwoCards, playerOneHand);
        }
    }

    private static Player findHighestCardPlayer(List<Card> playerOneCards, List<Card> playerTwoCards, Hand hand){
        Integer maxCardOne;
        Integer maxCardTwo;
        int comparisonResult;
        if(Hand.HIGH_CARD.equals(hand) ||
                Hand.FLUSH.equals(hand) ||
                Hand.STRAIGHT_FLUSH.equals(hand) ||
                Hand.STRAIGHT.equals(hand)){
            maxCardOne = findMaxCard(playerOneCards);
            maxCardTwo = findMaxCard(playerTwoCards);
            comparisonResult = Integer.compare(maxCardOne, maxCardTwo);
            if(comparisonResult > 0){
                return Player.PLAYER_ONE;
            }
            // Don't check for equality since tie is impossible
            return Player.PLAYER_TWO;

        }
        // Don't check for double royal flush since it is impossible
        // as implied by the conditions of the task
        Set<Integer> numbersOne = new HashSet<>();
        List<Integer> duplicateOne = playerOneCards.stream()
                .map(Card::getNumber)
                .filter(n -> !numbersOne.add(n))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        Set<Integer> numbersTwo = new HashSet<>();
        List<Integer> duplicateTwo = playerTwoCards.stream()
                .map(Card::getNumber)
                .filter(n -> !numbersTwo.add(n))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        int compare = Integer.compare(duplicateOne.get(0), duplicateTwo.get(0));
        if(compare > 0){
            return Player.PLAYER_ONE;
        }
        if (compare < 0) {
            return Player.PLAYER_TWO;
        }
        // Need to find the highest kicker ( non-repeating card)
        numbersOne.removeAll(duplicateOne);
        numbersTwo.removeAll(duplicateTwo);
        maxCardOne = numbersOne
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(-1);
        maxCardTwo = numbersTwo
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(-1);

        compare = Integer.compare(maxCardOne, maxCardTwo);
        if(compare > 0){
            return Player.PLAYER_ONE;
        }
            return Player.PLAYER_TWO;
    }

    private static int findMaxCard(List<Card> cards){
        return cards
                .stream()
                .map(Card::getNumber)
                .mapToInt(v -> v)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private static Card stringToCard(String input){
        Suit suit = Suit.resolveFromCode(input.charAt(1));
        return new Card(CARD_NUMBER_MAP.get(input.charAt(0)), suit);
    }

    private static Hand findBestHand(List<Card> cards){
        Set<Suit> suits = cards.stream().map(Card::getSuit).collect(Collectors.toSet());
        List<Integer> numbers = cards.stream().map(Card::getNumber).collect(Collectors.toList());
        // First check for flushes
        if(suits.size() == 1){
            if(isRoyalFlush(numbers)){
                return Hand.ROYAL_FLUSH;
            }
            if(isStraightFlush(numbers)){
                return Hand.STRAIGHT_FLUSH;
            }
        }

        if(isFourOfAKind(numbers)){
            return Hand.FOUR_OF_A_KIND;
        }
        if(isFullHouse(numbers)){
            return Hand.FULL_HOUSE;
        }

        if(suits.size() == 1){
            return Hand.FLUSH;
        }

        if(isConsecutive(numbers)){
          return Hand.STRAIGHT;
        }

        if(isThreeOfAKind(numbers)){
            return Hand.THREE_OF_A_KIND;
        }

        if(isTwoPairs(numbers)){
            return Hand.TWO_PAIRS;
        }

        if(isPair(numbers)){
            return Hand.ONE_PAIR;
        }

        return Hand.HIGH_CARD;
    }

    private static boolean isRoyalFlush(List<Integer> cards){
       return ROYAL_FLUSH_NUMBERS.equals(new HashSet<>(cards));
    }

    private static boolean isStraightFlush(List<Integer> cards){
        return isConsecutive(cards);
    }

    private static boolean isFullHouse(List<Integer> cards){
        return hasDuplicates(cards, 4,2);
    }

    private static boolean isFourOfAKind(List<Integer> cards){
        return hasDuplicates(cards, 4, 1);
    }

    private static boolean isThreeOfAKind(List<Integer> cards){
        return hasDuplicates(cards, 3, 1);
    }

    private static boolean isTwoPairs(List<Integer> cards){
        return hasDuplicates(cards, 3, 2);
    }

    private static boolean isPair(List<Integer> cards){
        return hasDuplicates(cards, 2, 1);
    }

    private static boolean isConsecutive(List<Integer> cards){
        int maxCard =  cards
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(0);
        int minCard = cards
                .stream()
                .mapToInt(v -> v)
                .min()
                .orElse(0);
        int size = cards.size();
        if (maxCard - minCard  + 1 == size)
        {
            boolean[] visited=new boolean[size];
            for (int i = 0; i < size; i++)
            {
                if ( visited[cards.get(i) - minCard]){
                    return false;
                }

                visited[cards.get(i) - minCard] = true;
            }

            return true;
        }
        return false;
    }

    private static boolean hasDuplicates(List<Integer> cards, int duplicateCount, int cardinality){
        Set<Integer> numbers = new HashSet<>();
        List<Integer> duplicates = cards.stream()
                .filter(n -> !numbers.add(n))
                .collect(Collectors.toList());
        Set<Integer> duplicateSet = new HashSet<>(duplicates);
        // Decrement expected count since one of duplicates is stored in numbers Set
        return duplicateSet.size() == cardinality && duplicates.size() == duplicateCount-1;
    }


}

