package handlers;

import data.User;
import enums.Commands;
import enums.Responses;
import managers.LoginManager;
import managers.RegisterManager;
import managers.RoomManager;

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

    private RoomHandler roomHandler;

    private final Socket roomSocket;

    private final DataInputStream dataIn;

    public ClientHandler(Socket clientSocket, Socket roomSocket) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
        dataIn = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream roomOut = new DataOutputStream(roomSocket.getOutputStream());
        DataInputStream roomIn = new DataInputStream(roomSocket.getInputStream());
        isrunning = true;
        loginManager = new LoginManager(dataOut, dataIn);
        registerManager = new RegisterManager(dataOut, dataIn);
        roomManager = new RoomManager(dataOut, dataIn, roomOut, roomIn);
        this.roomSocket = roomSocket;
    }

    @Override
    public void run() {
        while (isrunning) {
            try {
                String command = dataIn.readUTF();
                if (command.equals(Commands.LOGIN.name())) {
                    var user = loginManager.handleLogin();
                    if (user.isPresent()) {
                        loggedInUser = user.get();
                        roomHandler = new RoomHandler(roomManager, loggedInUser);
                        roomHandler.start();
                    }
                }
                else if (command.equals(Commands.REGISTER.name())) {
                    registerManager.handleRegister();
                }
                else if (command.equals(Commands.JOIN_ROOM.name())) {
                    roomManager.handleRoomJoin(loggedInUser);
                }
                else if (command.equals(Commands.LEAVE_ROOM.name())) {
                    roomManager.handleLeaveRoom(loggedInUser);
                }
            } catch (IOException e) {
                System.out.println("Can't receive user command");
                roomHandler.kill();
                kill();
            }
        }
    }

    public void kill() {
        isrunning = false;
    }
}
