package managers;

import app.Main;
import data.Card;
import data.User;
import enums.Commands;
import enums.Rank;
import enums.Responses;
import enums.Suit;
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


    public CardManager(Socket clientSocket, Socket roomSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.roomSocket = roomSocket;
        this.clientDataIn = new DataInputStream(clientSocket.getInputStream());
        this.clientDataOut = new DataOutputStream(clientSocket.getOutputStream());
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
        int ourIndex = indexOfEnemy == 0 ? 1 : 0;
        var sendEnemyCardDataOut = new DataOutputStream(room.getPlayers()
                .get(indexOfEnemy)
                .getSendEnemyCardSocket()
                .getOutputStream());
        var sendOurCardDataOut = new DataOutputStream(room
                .getPlayers()
                .get(ourIndex)
                .getSendEnemyCardSocket()
                .getOutputStream());
        if (!validators.get(room.getRoundNumber().ordinal()).isMoveCorrect(loggedInUser)) {
            System.out.println("Move incorrect");
            return;
        }
        sendOurCardDataOut.writeUTF(Responses.PLAY_CARD_ACK.name());
        sendCardToEnemy(loggedInUser, sendEnemyCardDataOut);
        validators.get(room.getRoundNumber().ordinal()).evaluateMove(loggedInUser);
//        loggedInUser.setHasTurn(false);
//        room.getPlayers().get(indexOfEnemy).setHasTurn(true);
        System.out.println("Card sent to enemy");
        System.out.println("Number of cards in the deck in room: " + room.getDeck().getCards().size());
    }

    private void sendCardToEnemy(User loggedInUser, DataOutputStream sendEnemyCardDataOut) throws IOException {
        sendEnemyCardDataOut.writeUTF(Responses.SEND_ENEMY_CARD.name());
        sendEnemyCardDataOut.writeUTF(loggedInUser.getCardPlayed().getSuit().name());
        sendEnemyCardDataOut.writeUTF(loggedInUser.getCardPlayed().getRank().name());
    }
}
