package validators;

import data.PointWrapper;
import data.User;
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
    public PointWrapper evaluateMove(User loggedInUser) {
        PointWrapper pointWrapper = new PointWrapper(0 ,0);
        for (var validator : validators) {
            pointWrapper.add(validator.evaluateMove(loggedInUser));
        }
        return pointWrapper;
    }
}
