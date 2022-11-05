package data;

import enums.Rank;
import enums.Suit;
import exceptions.EmptyDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    public static final int HALF_THE_DECK = 26;


    private final List<Card> cards = new ArrayList<>(52);

    public Deck() {
        refill();
    }

    public void refill() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public void getCard() throws EmptyDeckException {
        if (cards.size() == 0) throw new EmptyDeckException();
        Card card = cards.get(cards.size() - 1);
        cards.remove(card);
    }

}
