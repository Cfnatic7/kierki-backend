package managers;

import app.Main;
import data.Card;
import data.User;
import enums.*;
import validators.FirstRoundValidator;
import validators.Validator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class CardManager {

    private final List<Validator> validators = List.of(new FirstRoundValidator());

    private Socket clientSocket;

    private Socket roomSocket;

    private DataInputStream clientDataIn;

    private DataOutputStream clientDataOut;

    private DeckManager deckManager;


    public CardManager(Socket clientSocket, Socket roomSocket, DeckManager deckManager) throws IOException {
        this.clientSocket = clientSocket;
        this.roomSocket = roomSocket;
        this.clientDataIn = new DataInputStream(clientSocket.getInputStream());
        this.clientDataOut = new DataOutputStream(clientSocket.getOutputStream());
        this.deckManager = deckManager;
    }

    public void setUserCard(User player) throws IOException {
        Suit suit = Suit.valueOf(clientDataIn.readUTF());
        Rank rank = Rank.valueOf(clientDataIn.readUTF());
        Card card = new Card(rank, suit);
        player.setCardPlayed(card);
    }

    public void handlePlayCard(User loggedInUser) throws IOException {
        System.out.println("Handling card played");
        System.out.println("Rank: " + loggedInUser.getCardPlayed().getRank().name());
        System.out.println("Suit: " + loggedInUser.getCardPlayed().getSuit().name());
        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        int indexOfEnemy = room.getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
        var enemy = room.getPlayers().get(indexOfEnemy);
        var sendEnemyCardDataOut = new DataOutputStream(enemy
                .getSendEnemyCardSocket()
                .getOutputStream());
        var sendOurCardDataOut = new DataOutputStream(loggedInUser
                .getSendEnemyCardSocket()
                .getOutputStream());
        if (!validators.get(room.getRoundNumber().ordinal()).isMoveCorrect(loggedInUser)) {
            System.out.println("Move incorrect");
            loggedInUser.setCardPlayed(null);
            return;
        }
        validators.get(room.getRoundNumber().ordinal()).setHasTurn(enemy, !enemy.hasTurn(), loggedInUser, !loggedInUser.hasTurn());
        sendOurCardDataOut.writeUTF(Responses.PLAY_CARD_ACK.name());
        sendCardToEnemy(loggedInUser, sendEnemyCardDataOut);
        validators.get(room.getRoundNumber().ordinal()).evaluateMove(loggedInUser);
        System.out.println("Card sent to enemy");
        System.out.println("Number of cards in the deck in room: " + room.getDeck().getCards().size());
        if (enemy.getCardsInHand().isEmpty() && loggedInUser.getCardsInHand().isEmpty()) {
            if (room.getRoundNumber().equals(RoundNumber.SEVENTH)) {
                System.out.println("game end");
                return;
            }
            System.out.println("Going to the next round");
            room.setRoundNumber(RoundNumber.values()[room.getRoundNumber().ordinal() + 1]);
            sendOurCardDataOut.writeUTF(Responses.SEND_HAND.name());
            sendEnemyCardDataOut.writeUTF(Responses.SEND_HAND.name());
            deckManager.handleGetHandSendEnemyCardSocket(loggedInUser);
            deckManager.handleGetHandSendEnemyCardSocket(enemy);
        }
    }

    private void sendCardToEnemy(User loggedInUser, DataOutputStream sendEnemyCardDataOut) throws IOException {
        sendEnemyCardDataOut.writeUTF(Responses.SEND_ENEMY_CARD.name());
        sendEnemyCardDataOut.writeUTF(loggedInUser.getCardPlayed().getSuit().name());
        sendEnemyCardDataOut.writeUTF(loggedInUser.getCardPlayed().getRank().name());
    }
}
