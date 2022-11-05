package exceptions;

public class EmptyDeckException extends Exception {

    public EmptyDeckException() {
        super("The deck is empty");
    }
}
