package data;

import enums.RoomNumber;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String login;

    private final List<Card> cardsInHand;

    private String password;

    private boolean isLoggedIn;

    private Card cardPlayed;

    private boolean hasTurn;

    private Socket sendEnemyCardSocket;

    private RoomNumber roomNumber;

    private Socket clientSocket;

    private boolean firstTurn;

    public User(String login, String password, Socket clientSocket, Socket sendEnemyCardSocket) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        this.login = login;
        this.password = password;
        this.isLoggedIn = false;
        cardsInHand = new ArrayList<>(Deck.HALF_THE_DECK);
        this.clientSocket = clientSocket;
        this.sendEnemyCardSocket = sendEnemyCardSocket;
        hasTurn = false;
        firstTurn = false;
    }

    public User(String login, String password, Socket clientSocket) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        this.login = login;
        this.password = password;
        this.isLoggedIn = false;
        cardsInHand = new ArrayList<>(Deck.HALF_THE_DECK);
        this.clientSocket = clientSocket;
        hasTurn = false;
        firstTurn = false;
    }

    public User(String login, String password) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        this.login = login;
        this.password = password;
        this.isLoggedIn = false;
        cardsInHand = new ArrayList<>(Deck.HALF_THE_DECK);
        hasTurn = false;
        firstTurn = false;
    }

    @Override
    public String toString() {
        return "User: " + login + "\n" + this.getCardPlayed().toString() +
                "Turn: " + this.hasTurn + "\n" +
                "First turn: " + this.firstTurn + "\n" +
                "Number of cards in possesion: " + this.cardsInHand.size() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return user.login.equals(this.login) && user.password.equals(this.password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public boolean hasTurn() {
        return hasTurn;
    }

    public void setHasTurn(boolean hasTurn) {
        this.hasTurn = hasTurn;
    }

    public Socket getSendEnemyCardSocket() {
        return sendEnemyCardSocket;
    }

    public void setSendEnemyCardSocket(Socket sendEnemyCardSocket) {
        this.sendEnemyCardSocket = sendEnemyCardSocket;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }
}
