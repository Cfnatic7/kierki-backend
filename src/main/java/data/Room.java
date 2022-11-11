package data;

import enums.RoomNumber;
import enums.RoundNumber;
import exceptions.RoomIsFullException;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final List<User> players;

    private final RoomNumber roomNumber;

    private RoundNumber roundNumber;

    private final Deck deck;

    private int subRound;

    public Room(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
        players = new ArrayList<>();
        deck = new Deck();
        subRound = 0;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public void addPlayer(User player) throws RoomIsFullException {
        if (players.size() < 2) {
            players.add(player);
        }
        else throw new RoomIsFullException();
    }

    public void removeplayer(User player) {
        players.remove(player);
        System.out.println("Number of players in room: " + roomNumber.name() + " is " + players.size());
    }

    public boolean isFull() {
        return this.players.size() == 2;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<User> getPlayers() {
        return players;
    }

    public RoundNumber getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(RoundNumber roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getSubRound() {
        return subRound;
    }

    public void setSubRound(int subRound) {
        this.subRound = subRound;
    }

    public void goToNextRound() {
        this.roundNumber = RoundNumber.values()[this.roundNumber.ordinal() + 1];
        this.subRound = 0;
        this.deck.clearDeck();
        this.deck.refill();
        var playerOne = this.players.get(0);
        var playerTwo = this.players.get(1);
        playerOne.setCardPlayed(null);
        playerTwo.setCardPlayed(null);
        playerOne.setHasTurn(true);
        playerTwo.setHasTurn(false);
        playerOne.setFirstTurn(true);
        playerTwo.setFirstTurn(false);
    }

}
