package managers;

import app.Main;
import data.User;
import enums.Responses;
import enums.RoomNumber;
import exceptions.RoomIsFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomManager {

    private DataOutputStream dataOut;

    private DataInputStream dataIn;



    public RoomManager(DataOutputStream dataOut, DataInputStream dataIn, DataOutputStream roomOut, DataInputStream roomIn) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
    }

    private static boolean toNotify = false;

    private User player;

    public synchronized void notifyAllClients() throws IOException {

        while (!toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

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

    public synchronized void handleRoomJoin(User player) throws IOException {
        while (toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        dataOut.writeUTF(Responses.OK.name());
        String roomNumber = dataIn.readUTF();
        if (roomNumber.equals(RoomNumber.ONE.name())) {
            try {
                sendJoinRoomResponse(0, player);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.TWO.name())) {
            try {
                sendJoinRoomResponse(1, player);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.THREE.name())) {
            try {
                sendJoinRoomResponse(2, player);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
        else if (roomNumber.equals(RoomNumber.FOUR.name())) {
            try {
                sendJoinRoomResponse(3, player);
            } catch (RoomIsFullException e) {
                dataOut.writeUTF(Responses.ROOM_FULL.name());
            }
        }
    }

    private void sendJoinRoomResponse(int index, User player) throws RoomIsFullException, IOException {
        Main.rooms.get(index).addPlayer(player);
        dataOut.writeUTF(Responses.OK.name());
        toNotify = true;
        this.notify();
    }

    public synchronized void handleLeaveRoom(User player) throws IOException {
        while (toNotify) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        dataOut.writeUTF(Responses.OK.name());
        String roomNumber = dataIn.readUTF();
        if (roomNumber.equals(RoomNumber.ONE.name())) {
            Main.rooms.get(0).removeplayer(player);
        }
        else if (roomNumber.equals(RoomNumber.TWO.name())) {
            Main.rooms.get(1).removeplayer(player);
        }
        else if (roomNumber.equals(RoomNumber.THREE.name())) {
            Main.rooms.get(2).removeplayer(player);
        }
        else if (roomNumber.equals(RoomNumber.FOUR.name())) {
            Main.rooms.get(3).removeplayer(player);
        }
        dataOut.writeUTF(Responses.OK.name());
        toNotify = true;
        this.notifyAll();
    }
}
