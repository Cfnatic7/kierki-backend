package handlers;

import app.Main;
import exceptions.BadSocketException;
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
                System.out.println("Connection to client lost");
            } catch (BadSocketException e) {
                Main.roomSockets.remove(e.getBadSocket());
                System.out.println("Removed bad socket");
            }
        }
    }

    public void kill() {
        isRunning = false;
    }

}
