package app;

import data.Lock;
import data.UserAccounts;
import enums.RoomNumber;
import handlers.ClientHandler;
import data.Room;
import handlers.RoomHandler;
import managers.RoomManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final static List<Socket> clients = new ArrayList<>();

    public final static Lock LOCK = new Lock();

    public volatile static List<Socket> roomSockets = new ArrayList<>();

    private final static int PORT_FOR_CLIENT_HANDLER = 8372;

    private final static int PORT_FOR_ROOM_HANDLER = 7482;

    private final static int PORT_FOR_ENEMY_CARD = 7583;

    private static ServerSocket serverForClientHandler;

    private static ServerSocket serverForRoomHandler;

    private static ServerSocket serverForEnemyCard;

    public static final UserAccounts userAccounts = new UserAccounts();

    public static volatile List<Room> rooms = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverForRoomHandler = new ServerSocket(PORT_FOR_ROOM_HANDLER);
        serverForClientHandler = new ServerSocket(PORT_FOR_CLIENT_HANDLER);
        serverForEnemyCard = new ServerSocket(PORT_FOR_ENEMY_CARD);
        RoomManager roomManager = new RoomManager();
        RoomHandler roomHandler = new RoomHandler(roomManager);
        roomHandler.start();
        userAccounts.initializeBasicUserAccounts();
        initializeRooms();

        System.out.println("Waiting for the client request");
        while(true) {
            Socket clientSocket = serverForClientHandler.accept();
            Socket roomSocket = serverForRoomHandler.accept();
            Socket sendEnemyCardSocket = serverForEnemyCard.accept();
            clients.add(clientSocket);
            roomSockets.add(roomSocket);
            var clientHandler = new ClientHandler(clientSocket, roomSocket, sendEnemyCardSocket, roomManager);
            clientHandler.start();
        }
    }

    public static void initializeRooms() {
        rooms.add(new Room(RoomNumber.ONE));
        rooms.add(new Room(RoomNumber.TWO));
        rooms.add(new Room(RoomNumber.THREE));
        rooms.add(new Room(RoomNumber.FOUR));
    }

    public static List<Socket> getClients() {
        return clients;
    }

    public static List<Socket> getRoomSockets() {
        return roomSockets;
    }
}