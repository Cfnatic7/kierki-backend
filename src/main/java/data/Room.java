package data;

import enums.RoomNumber;
import exceptions.RoomIsFullException;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final List<User> players;

    private final RoomNumber roomNumber;

    public Room(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
        players = new ArrayList<>();
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

}
