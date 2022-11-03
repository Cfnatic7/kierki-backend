package handlers;

import data.User;
import managers.RoomManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomHandler extends Thread {

    private RoomManager roomManager;

    private User loggedInPlayer;

    private boolean isRunning;

    public RoomHandler(RoomManager roomManager, User loggedInPlayer) throws IOException {
        isRunning = true;

        this.roomManager = roomManager;

        this.loggedInPlayer = loggedInPlayer;
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                this.roomManager.notifyAllClients();
            } catch (IOException e) {
                kill();
                throw new RuntimeException(e);
            }
        }
    }

    public void kill() {
        isRunning = false;
    }

}
