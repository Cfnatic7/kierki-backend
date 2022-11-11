package validators;

import app.Main;
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
        System.out.println("Seventh round validation");
        return validators.get(1).isMoveCorrect(loggedInUser);
    }

    @Override
    public PointWrapper evaluateMove(User loggedInUser) {
        PointWrapper pointWrapper = new PointWrapper(0 ,0);
        var enemy = Main
                .rooms
                .get(loggedInUser.getRoomNumber().ordinal())
                .getPlayers()
                .get(Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0);
        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return null;
        for (var validator : validators) {
            pointWrapper.add(validator.evaluateMove(loggedInUser));
        }
        return pointWrapper;
    }
}
