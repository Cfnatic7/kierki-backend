package managers;

import data.Card;
import data.Deck;
import enums.Responses;
import exceptions.EmptyDeckException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeckManager {

    private final Deck deck;

    private final DataInputStream datain;

    private final DataOutputStream dataOut;

    public DeckManager(Deck deck, DataInputStream dataIn, DataOutputStream dataOut) {
        this.deck = deck;
        this.datain = dataIn;
        this.dataOut = dataOut;
    }

    public Deck getDeck() {
        return deck;
    }

    public void handleGetHand() throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        for (int i = 0; i < Deck.HALF_THE_DECK; i++) {
            try {
                Card card = deck.getCard();
                dataOut.writeUTF(card.getSuit().name());
                dataOut.writeUTF(card.getRank().name());
            } catch(EmptyDeckException e) {
                System.out.println("Deck is empty");
            }
        }
        dataOut.writeUTF(Responses.END_RECEIVE_CARDS.name());
    }
}
