package managers;

import app.Main;
import data.User;
import enums.Commands;
import enums.Responses;
import enums.RoomNumber;
import exceptions.RoomIsFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RoomManager {

    private DataOutputStream dataOut;

    private DataInputStream dataIn;

    public RoomManager(DataOutputStream dataOut, DataInputStream dataIn) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
    }

    public void handleRoomJoin(User player) throws IOException, RoomIsFullException {
        dataOut.writeUTF(Responses.OK.name());
        String roomNumber = dataIn.readUTF();
        if (roomNumber.equals(RoomNumber.ONE.name())) {
            Main.rooms.get(0).addPlayer(player);
        }
    }
}
