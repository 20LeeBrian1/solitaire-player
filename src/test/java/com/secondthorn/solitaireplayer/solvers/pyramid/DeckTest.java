package com.secondthorn.solitaireplayer.solvers.pyramid;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeckTest {
    static List<String> orderedCards;

    static {
        orderedCards = new ArrayList<>(52);
        for (char suit : "cdhs".toCharArray()) {
            for (char rank : "A23456789TJQK".toCharArray()) {
                orderedCards.add("" + rank + suit);
            }
        }
    }

    private void assertDeckCardsMatch(List<String> cards, Deck deck) {
        for (int i=0; i<52; i++) {
            assertEquals(cards.get(i), deck.cardAt(i));
        }
    }

    @Test
    public void constructorWithListOfCards() {
        assertDeckCardsMatch(orderedCards, new Deck(orderedCards));
    }

    @Test
    public void constructorWithStringOfCards() {
        StringBuilder stringOfCards = new StringBuilder();
        for (String card : orderedCards) {
            stringOfCards.append(card).append(" ");
        }
        assertDeckCardsMatch(orderedCards, new Deck(stringOfCards.toString()));
    }

    @Test
    public void constructorWithArrayOfCards() {
        String[] cards = orderedCards.toArray(new String[52]);
        assertDeckCardsMatch(orderedCards, new Deck(cards));
    }

    private char cardRank(String card) {
        return card.charAt(0);
    }

    @Test
    public void kingsFoundOnOrderedDeck() {
        Deck orderedDeck = new Deck(orderedCards);
        for (int i=0; i<52; i++) {
            char rank = cardRank(orderedDeck.cardAt(i));
            assertEquals(rank == 'K', orderedDeck.isKing(i));
        }
    }

    @Test
    public void cardRankBucketsOnOrderedDeck() {
        Deck orderedDeck = new Deck(orderedCards);
        for (int i=0; i<52; i++) {
            int rankBucket = "KA23456789TJQK".indexOf(orderedDeck.cardAt(i).charAt(0));
            assertEquals(rankBucket, orderedDeck.cardRankBucket(i));
        }
    }

    private boolean cardRanksAddTo13(String card1, String card2) {
        switch (cardRank(card1)) {
            case 'A': return cardRank(card2) == 'Q';
            case '2': return cardRank(card2) == 'J';
            case '3': return cardRank(card2) == 'T';
            case '4': return cardRank(card2) == '9';
            case '5': return cardRank(card2) == '8';
            case '6': return cardRank(card2) == '7';
            case '7': return cardRank(card2) == '6';
            case '8': return cardRank(card2) == '5';
            case '9': return cardRank(card2) == '4';
            case 'T': return cardRank(card2) == '3';
            case 'J': return cardRank(card2) == '2';
            case 'Q': return cardRank(card2) == 'A';
            default: return false;
        }
    }

    @Test
    public void cardsMatchOnOrderedDeck() {
        Deck orderedDeck = new Deck(orderedCards);
        for (int i=0; i<52; i++) {
            String card1 = orderedDeck.cardAt(i);
            boolean[] matches = orderedDeck.getMatchesForCard(i);
            for (int j=0; j<52; j++) {
                String card2 = orderedDeck.cardAt(j);
                assertEquals(cardRanksAddTo13(card1, card2), matches[j]);
            }
        }
    }

    private long getUnwinnableMask(int... args) {
        long mask = 0;
        for (int arg : args) {
            mask |= 1L << arg;
        }
        return mask;
    }

    @Test
    public void unwinnableMasksOnOrderedDeck() {
        Deck orderedDeck = new Deck(orderedCards);
        long[] expected = {
                getUnwinnableMask(0, 37, 50),
                getUnwinnableMask(1, 36, 49),
                getUnwinnableMask(2, 35, 48),
                getUnwinnableMask(3, 8, 34, 47),
                getUnwinnableMask(4, 20, 33, 46),
                getUnwinnableMask(5, 6, 32, 45),
                getUnwinnableMask(6, 5, 18, 31, 44),
                getUnwinnableMask(7, 30, 43),
                getUnwinnableMask(8, 3, 16, 29, 42),
                getUnwinnableMask(9, 15, 28, 41),
                getUnwinnableMask(10, 14, 27, 40),
                getUnwinnableMask(11, 13, 26, 39),
                0, // Kc is always removable
                getUnwinnableMask(13, 11, 37, 50),
                getUnwinnableMask(14, 10, 23, 36, 49),
                getUnwinnableMask(15, 9, 35, 48),
                getUnwinnableMask(16, 8, 21, 34, 47),
                getUnwinnableMask(17, 20, 33, 46),
                getUnwinnableMask(18, 6, 19, 32, 45),
                getUnwinnableMask(19, 18, 31, 44),
                getUnwinnableMask(20, 4, 17, 30, 43),
                getUnwinnableMask(21, 16, 29, 42),
                getUnwinnableMask(22, 28, 41),
                getUnwinnableMask(23, 14, 27, 40),
                getUnwinnableMask(24, 26, 39),
                0, // Kd is always removable
                getUnwinnableMask(26, 11, 24, 37, 50),
                getUnwinnableMask(27, 10, 23, 36, 49),
        };
        for (int i=0; i<expected.length; i++) {
            assertEquals("Unwinnable Mask " + i + " doesn't match", expected[i], orderedDeck.getUnwinnableMask(i));
        }
    }

    @Test
    public void testToString() {
        Deck orderedDeck = new Deck(orderedCards);
        String expectedString =
                "            Ac\n" +
                "          2c  3c\n" +
                "        4c  5c  6c\n" +
                "      7c  8c  9c  Tc\n" +
                "    Jc  Qc  Kc  Ad  2d\n" +
                "  3d  4d  5d  6d  7d  8d\n" +
                "9d  Td  Jd  Qd  Kd  Ah  2h\n" +
                "3h 4h 5h 6h 7h 8h 9h Th Jh Qh Kh As 2s 3s 4s 5s 6s 7s 8s 9s Ts Js Qs Ks";
        assertEquals(expectedString, orderedDeck.toString());
    }
}
