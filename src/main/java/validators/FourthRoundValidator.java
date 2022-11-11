package validators;

import app.Main;
import data.Card;
import data.PointWrapper;
import data.User;
import enums.Rank;

import java.io.IOException;
import java.util.List;

public class FourthRoundValidator extends Validator {

    public final int POINTS = -30;

    private final FirstRoundValidator firstRoundValidator;

    public FourthRoundValidator() {
        firstRoundValidator = new FirstRoundValidator();
    }

    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        return firstRoundValidator.isMoveCorrect(loggedInUser);
    }

    @Override
    public PointWrapper evaluateMove(User loggedInUser) {
        System.out.println("Evaluating move");
        var enemy = Main
                .rooms
                .get(loggedInUser.getRoomNumber().ordinal())
                .getPlayers()
                .get(Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0);

        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return null;
        List<Card> cardsPlayer = List.of(loggedInUser.getCardPlayed(), enemy.getCardPlayed());
        if (loggedInUser.isFirstTurn()) {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(0, POINTS));
                    }
                });
                return pointWrapper;
            }
        }
        else {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(0, POINTS));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getRank() == Rank.WALET || card.getRank() == Rank.KRÓL) {
                        pointWrapper.add(new PointWrapper(0, POINTS));
                    }
                });
                return pointWrapper;
            }
            return null;
        }
        return null;
    }
}
