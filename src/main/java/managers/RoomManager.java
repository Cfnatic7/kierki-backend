package managers;

import app.Main;
import data.Card;
import data.Room;
import data.User;
import enums.Responses;
import enums.RoomNumber;
import enums.RoundNumber;
import exceptions.BadSocketException;
import exceptions.RoomIsFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomManager {

    public RoomManager() {
    }

    private static boolean toNotify = false;


    public synchronized void notifyAllClients() throws IOException, BadSocketException {

        while (!toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        synchronized (Main.getRoomSockets()) {
            for (Socket roomSocket : Main.getRoomSockets()) {
                try {
                    if (!roomSocket.isConnected()) continue;
                    DataOutputStream dataOut = new DataOutputStream(roomSocket.getOutputStream());
                    for (var room : Main.rooms) {
                        dataOut.writeUTF(room.getRoomNumber().name());
                        String responseToSend = room.isFull() ? Responses.ROOM_FULL.name() : Responses.OK.name();
                        dataOut.writeUTF(responseToSend);
                        System.out.println("Notification send: " + room.getRoomNumber().name() + " " + responseToSend);
                    }
                } catch (IOException e) {
                    throw new BadSocketException(roomSocket);
                }

            }
            toNotify = false;
        }

    }

    public synchronized void handleRoomJoin(User player, Socket clientSocket) throws IOException {
        while (toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        var dataIn = new DataInputStream(clientSocket.getInputStream());
        var dataOut = new DataOutputStream(clientSocket.getOutputStream());
        dataOut.writeUTF(Responses.OK.name());
        RoomNumber roomNumber = RoomNumber.valueOf(dataIn.readUTF());
        var room = Main.rooms.get(roomNumber.ordinal());
        room.setSubRound(1);
        room.setRoundNumber(RoundNumber.FIRST);
        if (roomNumber == RoomNumber.ONE) {
            try {
                sendJoinRoomResponse(0, player, dataOut);
                player.setRoomNumber(RoomNumber.ONE);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber == RoomNumber.TWO) {
            try {
                sendJoinRoomResponse(1, player, dataOut);
                player.setRoomNumber(RoomNumber.TWO);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber == RoomNumber.THREE) {
            try {
                sendJoinRoomResponse(2, player, dataOut);
                player.setRoomNumber(RoomNumber.THREE);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber == RoomNumber.FOUR) {
            try {
                sendJoinRoomResponse(3, player, dataOut);
                player.setRoomNumber(RoomNumber.FOUR);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        if (Main.rooms.get(roomNumber.ordinal()).getPlayers().size() == 1) {
            player.setHasTurn(true);
            player.setFirstTurn(true);
        }
    }

    private void sendJoinRoomResponse(int index, User player, DataOutputStream dataOut) throws RoomIsFullException, IOException {
        Main.rooms.get(index).addPlayer(player);
        if (Main.rooms.size() == 1) {
            player.setHasTurn(true);
            player.setFirstTurn(true);
        }
        dataOut.writeUTF(Responses.OK.name());
        toNotify = true;
        this.notify();
    }

    public synchronized void handleLeaveRoom(User player, Socket clientSocket) throws IOException {
        while (toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        var dataOut = new DataOutputStream(clientSocket.getOutputStream());
        if (player == null || player.getRoomNumber() == null) {
            dataOut.writeUTF(Responses.OK.name());
            return;
        }
        var room = Main.rooms.get(player.getRoomNumber().ordinal());
        int enemyPlayerIndex = room.getPlayers().indexOf(player) == 0 ? 1 : 0;
        if (room.isFull()) {
            var enemy = room.getPlayers().get(enemyPlayerIndex);
            var enemyDataOut = new DataOutputStream(enemy.getSendEnemyCardSocket().getOutputStream());
            enemyDataOut.writeUTF(Responses.RESET.name());
        }
        var ourDataOut = new DataOutputStream(player.getSendEnemyCardSocket().getOutputStream());
        ourDataOut.writeUTF(Responses.RESET.name());
        room.removeplayer(player);
        room.getDeck().clearDeck();
        room.getDeck().refill();
        room.setSubRound(0);
        player.getCardsInHand().clear();
        player.setRoomNumber(null);
        toNotify = true;
        dataOut.writeUTF(Responses.OK.name());
        this.notifyAll();
    }

}
