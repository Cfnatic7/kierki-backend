package managers;


import app.Main;
import data.User;
import enums.Responses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class LoginManager {

    private DataInputStream dataIn;

    private DataOutputStream dataOut;

    private Socket roomSocket;

    public LoginManager(DataOutputStream dataOut, DataInputStream dataIn, Socket roomSocket) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.roomSocket = roomSocket;
    }

    public Optional<User> handleLogin() throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        String login = dataIn.readUTF();
        dataOut.writeUTF(Responses.OK.name());
        String password = dataIn.readUTF();
        int userIndex = Main.userAccounts.getUsers().indexOf(new User(login, password));
        if (userIndex == -1) {
            System.out.println("User not found");
            dataOut.writeUTF(Responses.USER_NOT_FOUND.name());
            return Optional.empty();
        }
        else {
            var user = Main.userAccounts.getUsers().get(userIndex);
            if (user.isLoggedIn()) {
                dataOut.writeUTF(Responses.USER_LOGGED_IN.name());
                System.out.println("User already logged in");
                return Optional.empty();
            }
            else dataOut.writeUTF(Responses.OK.name());
            System.out.println("Logging in");
            user.setLoggedIn(true);
            return Optional.of(user);
        }
    }

    public void handleLogout(User loggedInUser) throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        synchronized (Main.getRoomSockets()) {
            Main.roomSockets.remove(roomSocket);
        }
        if (loggedInUser != null) {
            loggedInUser.setLoggedIn(false);
        }
    }
}
