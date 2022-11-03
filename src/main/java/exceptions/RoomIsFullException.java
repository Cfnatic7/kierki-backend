package exceptions;

public class RoomIsFullException extends Exception {

    public RoomIsFullException() {
        super("Room is full");
    }
}
