package exceptions;

import java.net.Socket;

public class BadSocketException extends Exception {
    private Socket badSocket;


    public BadSocketException(Socket socket) {
        super("Bad socket caught");
        this.badSocket = socket;
    }

    public Socket getBadSocket() {
        return badSocket;
    }
}
