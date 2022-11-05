package managers;

import data.Card;
import data.User;
import enums.Rank;
import enums.Responses;
import enums.Suit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CardHandler {

    private Socket clientSocket;

    private Socket roomSocket;

    private DataInputStream clientDataIn;

    private DataOutputStream clientDataOut;


    public CardHandler(Socket clientSocket, Socket roomSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.roomSocket = roomSocket;
        this.clientDataIn = new DataInputStream(clientSocket.getInputStream());
        this.clientDataOut = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void handlePlayCard(User loggedInUser) throws IOException {
        clientDataOut.writeUTF(Responses.OK.name());
        Suit suit = Suit.valueOf(clientDataIn.readUTF());
        Rank rank = Rank.valueOf(clientDataIn.readUTF());
        Card card = new Card(rank, suit);
        loggedInUser.setCardPlayed(card);
        var sendEnemyCardDataOut = new DataOutputStream(loggedInUser.getSendEnemyCardSocket().getOutputStream());
        sendEnemyCardDataOut.writeUTF(suit.name());
        sendEnemyCardDataOut.writeUTF(rank.name());
    }
}
