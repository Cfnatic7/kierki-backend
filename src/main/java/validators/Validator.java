package validators;

import data.User;

import java.io.IOException;

public interface Validator {

    boolean isMoveCorrect(User loggedInUser);

    void evaluateMove(User loggedInUser) throws IOException
}
