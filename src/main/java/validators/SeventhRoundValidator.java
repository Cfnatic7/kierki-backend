package validators;

import data.User;

import java.io.IOException;
import java.util.List;

public class SeventhRoundValidator extends Validator {

    private final List<Validator> validators;

    public SeventhRoundValidator() {
        validators = List.of(new FirstRoundValidator(), new SecondRoundValidator(),
                new ThirdRoundValidator(), new FourthRoundValidator(), new FifthRoundValidator(),
                new SixthRoundValidator());
    }

    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        return validators.get(0).isMoveCorrect(loggedInUser) && validators.get(1).isMoveCorrect(loggedInUser);
    }

    @Override
    public void evaluateMove(User loggedInUser) throws IOException {
        for (var validator : validators) {
            validator.evaluateMove(loggedInUser);
        }
    }
}
