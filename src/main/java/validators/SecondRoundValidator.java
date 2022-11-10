package validators;

import data.User;

import java.io.IOException;

public class SecondRoundValidator extends Validator {

    private final FirstRoundValidator firstRoundValidator;

    public SecondRoundValidator() {
        this.firstRoundValidator = new FirstRoundValidator();
    }


    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        return false;
    }

    @Override
    public void evaluateMove(User loggedInUser) throws IOException {

    }
}
