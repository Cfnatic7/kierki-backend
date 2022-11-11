package exceptions;

public class BadCommandException extends RuntimeException {

    public BadCommandException(String command, Throwable err) {
        super(String.format("Bad command: %s", command), err);
    }
}
