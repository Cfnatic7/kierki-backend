package handlers;

import data.User;
import enums.Responses;
import exceptions.RoomIsFullException;
import managers.RoomManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomHandler extends Thread {

    private DataOutputStream dataOut;

    private DataInputStream dataIn;

    private RoomManager roomManager;

    private User loggedInPlayer;

    private boolean isRunning;

    public RoomHandler(Socket clientSocket, RoomManager roomManager, User loggedInPlayer) throws IOException {
        isRunning = true;

        dataOut = new DataOutputStream(clientSocket.getOutputStream());

        dataIn = new DataInputStream(clientSocket.getInputStream());

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
