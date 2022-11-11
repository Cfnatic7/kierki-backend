package managers;

import app.Main;
import data.Card;
import data.Deck;
import data.User;
import enums.Responses;
import exceptions.EmptyDeckException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeckManager {

    private DataInputStream datain;

    private DataOutputStream dataOut;

    public DeckManager(DataInputStream dataIn, DataOutputStream dataOut) {
        this.datain = dataIn;
        this.dataOut = dataOut;
    }

    public DeckManager() {

    }

    public void handleGetHand(User loggedInUser) throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        Deck roomDeck = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getDeck();
        for (int i = 0; i < Deck.HALF_THE_DECK; i++) {
            try {
                Card card = roomDeck.getCard();
                dataOut.writeUTF(card.getSuit().name());
                dataOut.writeUTF(card.getRank().name());
                loggedInUser.getCardsInHand().add(card);
            } catch(EmptyDeckException e) {
                System.out.println("Deck is empty");
            }
        }
        dataOut.writeUTF(Responses.END_RECEIVE_CARDS.name());
    }

    public void handleGetHandSendEnemyCardSocket(User loggedInUser) throws IOException {
        System.out.println("Sending cards");
        var sendEnemySocketDataOut = new DataOutputStream(loggedInUser.getSendEnemyCardSocket().getOutputStream());
        Deck roomDeck = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getDeck();
        roomDeck.shuffleDeck();
        for (int i = 0; i < Deck.HALF_THE_DECK; i++) {
            try {
                Card card = roomDeck.getCard();
                sendEnemySocketDataOut.writeUTF(card.getSuit().name());
                sendEnemySocketDataOut.writeUTF(card.getRank().name());
                loggedInUser.getCardsInHand().add(card);
            } catch(EmptyDeckException e) {
                System.out.println("Deck is empty");
            }
        }
    }
}
