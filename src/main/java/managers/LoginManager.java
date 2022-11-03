package managers;


import app.Main;
import data.User;
import enums.Responses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginManager {

    private DataInputStream dataIn;

    private DataOutputStream dataOut;

    public LoginManager(DataOutputStream dataOut, DataInputStream dataIn) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
    }

    public void handleLogin() throws IOException {
        dataOut.writeUTF(Responses.OK.name());
        String login = dataIn.readUTF();
        dataOut.writeUTF(Responses.OK.name());
        String password = dataIn.readUTF();
        int userIndex = Main.userAccounts.getUsers().indexOf(new User(login, password));
        if (userIndex == -1) {
            dataOut.writeUTF(Responses.USER_NOT_FOUND.name());
        }
        else {
            var user = Main.userAccounts.getUsers().get(userIndex);
            if (user.isLoggedIn()) {
                dataOut.writeUTF(Responses.USER_LOGGED_IN.name());
            }
            else dataOut.writeUTF(Responses.OK.name());
        }
    }
}
