package data;

import enums.Rank;
import enums.Suit;

public class Card {

    private Rank rank;

    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card cardToCompare)) return false;
        return cardToCompare.rank == this.rank && cardToCompare.suit == this.suit;
    }

}
