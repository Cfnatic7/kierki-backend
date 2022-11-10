package validators;

import data.User;
import enums.Suit;

import java.io.IOException;

public class SecondRoundValidator extends Validator {

    private final FirstRoundValidator firstRoundValidator;

    public SecondRoundValidator() {
        this.firstRoundValidator = new FirstRoundValidator();
    }


    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        if (!firstRoundValidator.isMoveCorrect(loggedInUser)) return false;
        var filteredHearts = loggedInUser.getCardsInHand().stream()
                .filter(card -> card
                .getSuit() == Suit.KIER)
                .toList();
        if (loggedInUser.isFirstTurn()) {
            if (!filteredHearts.isEmpty()) return false;
        }
    }

    @Override
    public void evaluateMove(User loggedInUser) throws IOException {

    }
}
