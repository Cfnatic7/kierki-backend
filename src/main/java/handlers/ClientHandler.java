package handlers;

import app.Main;
import data.User;
import enums.Commands;
import managers.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final LoginManager loginManager;

    private final RegisterManager registerManager;

    private final RoomManager roomManager;

    private boolean isrunning;

    private volatile User loggedInUser;

    private final Socket roomSocket;

    private final Socket sendEnemyCardSocket;

    private final Socket clientSocket;

    private final DataInputStream dataIn;

    private final DeckManager deckManager;

    private final CardManager cardManager;

    public ClientHandler(Socket cS, Socket roomSocket, Socket sendEnemyCardSocket, RoomManager rM) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(cS.getOutputStream());
        clientSocket = cS;
        dataIn = new DataInputStream(clientSocket.getInputStream());
        isrunning = true;
        loginManager = new LoginManager(clientSocket, roomSocket);
        registerManager = new RegisterManager(dataOut, dataIn);
        roomManager = rM;
        this.roomSocket = roomSocket;
        deckManager = new DeckManager(dataIn, dataOut);
        this.sendEnemyCardSocket = sendEnemyCardSocket;
        this.cardManager = new CardManager(clientSocket, roomSocket, deckManager);
    }

    @Override
    public void run() {
        while (isrunning) {
            String command = null;
            try {
                command = dataIn.readUTF();
            } catch (IOException e) {
                System.out.println("Can't receive command");
                kill();
            }
            synchronized (Main.LOCK) {
                try {
                    if (command.equals(Commands.LOGIN.name())) {
                        var user = loginManager.handleLogin();
                        user.ifPresent(value -> {
                            loggedInUser = value;
                            user.get().setClientSocket(clientSocket);
                            user.get().setSendEnemyCardSocket(sendEnemyCardSocket);
                        });
                    }
                    else if (command.equals(Commands.REGISTER.name())) {
                        registerManager.handleRegister();
                    }
                    else if (command.equals(Commands.JOIN_ROOM.name())) {
                        roomManager.handleRoomJoin(loggedInUser, clientSocket);
                    }
                    else if (command.equals(Commands.LEAVE_ROOM.name())) {
                        roomManager.handleLeaveRoom(loggedInUser, clientSocket);
                    }
                    else if (command.equals(Commands.LOGOUT.name())) {
                        roomManager.handleLeaveRoom(loggedInUser, clientSocket);
                        loginManager.handleLogout(loggedInUser);
                    }
                    else if (command.equals(Commands.GET_HAND.name())) {
                        deckManager.handleGetHand(loggedInUser);
                    }
                    else if (command.equals(Commands.PLAY_CARD.name())) {
                        cardManager.setUserCard(loggedInUser);
                        cardManager.handlePlayCard(loggedInUser);
                    }
                } catch (IOException e) {
                    System.out.println("Can't receive user command");
                    kill();
                } catch(NullPointerException e) {
                    System.out.println("Command is null");
                }
            }
        }
    }

    public void kill() {
        isrunning = false;
    }
}
