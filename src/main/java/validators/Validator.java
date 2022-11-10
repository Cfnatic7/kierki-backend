package validators;

import data.User;

import java.io.IOException;

public abstract class Validator {

    public abstract boolean isMoveCorrect(User loggedInUser);

    public abstract void evaluateMove(User loggedInUser) throws IOException;
}
