package managers;

import app.Main;
import data.Card;
import data.User;
import enums.Commands;
import enums.Rank;
import enums.Responses;
import enums.Suit;
import validators.FirstRoundValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CardManager {

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

    public void handlePlayCard(User loggedInUser) throws IOException {
        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        int indexOfEnemy = room.getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
        var sendEnemyCardDataOut = new DataOutputStream(room.getPlayers()
                .get(indexOfEnemy)
                .getSendEnemyCardSocket()
                .getOutputStream());
        Suit suit = Suit.valueOf(clientDataIn.readUTF());
        Rank rank = Rank.valueOf(clientDataIn.readUTF());
        Card card = new Card(rank, suit);
        loggedInUser.setCardPlayed(card);
        if (!FirstRoundValidator.isMoveCorrect(loggedInUser)) {
            return;
        }
        sendEnemyCardDataOut.writeUTF(Responses.PLAY_CARD_ACK.name());
        sendEnemyCardDataOut.writeUTF(Responses.SEND_ENEMY_CARD.name());
        sendEnemyCardDataOut.writeUTF(suit.name());
        sendEnemyCardDataOut.writeUTF(rank.name());
        FirstRoundValidator.evaluateMove(loggedInUser);
//        loggedInUser.setHasTurn(false);
//        room.getPlayers().get(indexOfEnemy).setHasTurn(true);
        System.out.println("Card sent to enemy");
        System.out.println("Number of cards in the deck in room: " + room.getDeck().getCards().size());
    }
}
