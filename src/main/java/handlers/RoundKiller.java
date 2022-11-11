package handlers;

import app.Main;
import enums.Responses;
import enums.RoomNumber;
import enums.RoundNumber;
import exceptions.BadCommandException;
import managers.DeckManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RoundKiller extends Thread {

    private boolean isRunning;

    private final DeckManager deckManager;

    private final BufferedReader systemIn;

    public RoundKiller(DeckManager deckManager) {
        this.deckManager = deckManager;
        systemIn = new BufferedReader(new InputStreamReader(System.in));
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning) {
            synchronized (Main.LOCK) {
                try {
                    String command = systemIn.readLine();
                    String subCommand = command.split(" ")[0];
                    RoomNumber roomNumber = RoomNumber.values()[Integer.parseInt(command.split(" ")[1]) - 1];
                    var room = Main.rooms.get(roomNumber.ordinal());
                    var playerOneDataOut = new DataOutputStream(room.getPlayers()
                            .get(0).getSendEnemyCardSocket().getOutputStream());
                    var playerTwoDataOut = new DataOutputStream(room.getPlayers()
                            .get(1).getSendEnemyCardSocket().getOutputStream());
                    if (subCommand.equals("nextround")) {
                        try {
                            if (room.isFull()) {
                                room.goToNextRound();
                                System.out.println("Going to the next round");
                                room.setRoundNumber(RoundNumber.values()[room.getRoundNumber().ordinal() + 1]);
                                playerOneDataOut.writeUTF(Responses.NEXT_ROUND.name());
                                playerTwoDataOut.writeUTF(Responses.NEXT_ROUND.name());
                                playerOneDataOut.writeUTF(Responses.SEND_HAND.name());
                                playerTwoDataOut.writeUTF(Responses.SEND_HAND.name());
                                deckManager.handleGetHandSendEnemyCardSocket(room.getPlayers().get(0));
                                deckManager.handleGetHandSendEnemyCardSocket(room.getPlayers().get(1));
                            }
                        } catch (Exception e) {
                            System.out.printf("Bad command: %s%n", command);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Couldn't read command");
                }
            }
        }
    }

    public void kill() {
        isRunning = false;
    }
}
