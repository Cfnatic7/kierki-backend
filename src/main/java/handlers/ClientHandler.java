package handlers;

import data.User;
import enums.Commands;
import enums.Responses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket;

    private DataOutputStream dataOut;

    private DataInputStream dataIn;

    private LoginHandler loginHandler;

    private boolean isrunning;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        dataOut = new DataOutputStream(clientSocket.getOutputStream());
        dataIn = new DataInputStream(clientSocket.getInputStream());
        isrunning = true;
        loginHandler = new LoginHandler(dataOut, dataIn);
    }

    @Override
    public void run() {
        while (isrunning) {
            try {
                String command = dataIn.readUTF();
                if (command.equals(Commands.LOGIN.name())) {
                    loginHandler.handleLogin();
                }
                else if (command.equals(Commands.REGISTER.name())) {
                    handleRegister();
                }
            } catch (IOException e) {
                System.out.println("Can't receive user command");
            }
        }
    }

    private void handleRegister() throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        String login = dataIn.readUTF();
        dataOut.writeUTF(Responses.OK.name());
        String password = dataIn.readUTF();
        var filteredUsers = Main
                .userAccounts
                .getUsers()
                .stream()
                .filter(user -> user.getLogin().equals(login)).toList();
        if (filteredUsers.size() > 0) dataOut.writeUTF(Responses.USER_ALREADY_EXISTS.name());
        else {
            Main.userAccounts.addUser(login, password);
            dataOut.writeUTF(Responses.OK.name());
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
