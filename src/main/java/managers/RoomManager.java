package managers;

import app.Main;
import data.Card;
import data.Room;
import data.User;
import enums.Responses;
import enums.RoomNumber;
import exceptions.RoomIsFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomManager {



    public RoomManager() {
    }

    private static boolean toNotify = false;


    public synchronized void notifyAllClients() throws IOException {

        while (!toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        synchronized (Main.getRoomSockets()) {
            for (Socket roomSocket : Main.getRoomSockets()) {
                DataOutputStream dataOut = new DataOutputStream(roomSocket.getOutputStream());
                for (var room : Main.rooms) {
                    dataOut.writeUTF(room.getRoomNumber().name());
                    String responseToSend = room.isFull() ? Responses.ROOM_FULL.name() : Responses.OK.name();
                    dataOut.writeUTF(responseToSend);
                    System.out.println("Notification send: " + room.getRoomNumber().name() + " " + responseToSend);
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
        String roomNumber = dataIn.readUTF();
        if (roomNumber.equals(RoomNumber.ONE.name())) {
            try {
                sendJoinRoomResponse(0, player, dataOut);
                player.setRoomNumber(RoomNumber.ONE);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.TWO.name())) {
            try {
                sendJoinRoomResponse(1, player, dataOut);
                player.setRoomNumber(RoomNumber.TWO);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.THREE.name())) {
            try {
                sendJoinRoomResponse(2, player, dataOut);
                player.setRoomNumber(RoomNumber.THREE);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.FOUR.name())) {
            try {
                sendJoinRoomResponse(3, player, dataOut);
                player.setRoomNumber(RoomNumber.FOUR);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
    }

    private void sendJoinRoomResponse(int index, User player, DataOutputStream dataOut) throws RoomIsFullException, IOException {
        Main.rooms.get(index).addPlayer(player);
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
        Main.rooms.get(player.getRoomNumber().ordinal()).removeplayer(player);
        player.setRoomNumber(null);
        Room playerRoom = Main.rooms.get(player.getRoomNumber().ordinal());
        for (Card card : player.getCardsInHand()) {
            playerRoom.getDeck().addCard(card);
        }
        player.getCardsInHand().clear();
        toNotify = true;
        dataOut.writeUTF(Responses.OK.name());
        this.notifyAll();
    }

}
