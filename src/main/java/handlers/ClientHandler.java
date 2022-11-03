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

    private Socket clientSocket;

    private DataOutputStream dataOut;

    private DataInputStream dataIn;

    private final LoginManager loginManager;

    private final RegisterManager registerManager;

    private final RoomManager roomManager;

    private boolean isrunning;

    private volatile User loggedInUser;

    private RoomHandler roomHandler;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        dataOut = new DataOutputStream(clientSocket.getOutputStream());
        dataIn = new DataInputStream(clientSocket.getInputStream());
        isrunning = true;
        loginManager = new LoginManager(dataOut, dataIn);
        registerManager = new RegisterManager(dataOut, dataIn);
        roomManager = new RoomManager(dataOut, dataIn);
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
                        roomHandler = new RoomHandler(clientSocket, roomManager, loggedInUser);
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


    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public DataOutputStream getDataOut() {
        return dataOut;
    }

    public void setDataOut(DataOutputStream dataOut) {
        this.dataOut = dataOut;
    }

    public DataInputStream getDataIn() {
        return dataIn;
    }

    public void setDataIn(DataInputStream dataIn) {
        this.dataIn = dataIn;
    }

    public void kill() {
        isrunning = false;
    }
}
