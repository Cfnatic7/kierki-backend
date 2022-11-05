package handlers;

import managers.RoomManager;
import java.io.IOException;

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
