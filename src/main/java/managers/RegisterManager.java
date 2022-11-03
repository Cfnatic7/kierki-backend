package managers;

import app.Main;
import enums.Responses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterManager {

    private DataInputStream dataIn;

    private DataOutputStream dataOut;

    public RegisterManager(DataOutputStream dataOut, DataInputStream dataIn) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
    }

    public void handleRegister() throws IOException {
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

    public DataInputStream getDataIn() {
        return dataIn;
    }

    public DataOutputStream getDataOut() {
        return dataOut;
    }
}
