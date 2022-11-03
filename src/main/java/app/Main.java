package app;

import data.UserAccounts;
import handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final static List<Socket> clients = new ArrayList<>();

    private final static int PORT = 8372;

    private static ServerSocket server;

    public static final UserAccounts userAccounts = new UserAccounts();

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(PORT);
        userAccounts.initializeBasicUserAccounts();

        System.out.println("Waiting for the client request");
        while(true) {
            Socket socket = server.accept();
            clients.add(socket);
            var clientHandler = new ClientHandler(socket);
            clientHandler.start();
        }
    }

    public static List<Socket> getClients() {
        return clients;
    }
}