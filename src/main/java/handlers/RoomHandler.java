package handlers;

import data.User;
import managers.RoomManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomHandler extends Thread {

    private RoomManager roomManager;

    private boolean isRunning = true;

    public RoomHandler(RoomManager roomManager) {
        isRunning = true;

        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                this.roomManager.notifyAllClients();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Connection to client lost");
            }
        }
    }

    public void kill() {
        isRunning = false;
    }

}
