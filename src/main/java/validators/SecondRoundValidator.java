package validators;

import app.Main;
import data.Card;
import data.PointWrapper;
import data.User;
import enums.Suit;

import java.io.IOException;
import java.util.List;

public class SecondRoundValidator extends Validator {

    private final FirstRoundValidator firstRoundValidator;

    public final int POINTS = -20;

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
            if (filteredHearts.size() == loggedInUser.getCardsInHand().size()
                    && loggedInUser.getCardPlayed().getSuit() == Suit.KIER) return true;
            return loggedInUser.getCardPlayed().getSuit() != Suit.KIER;
        }
        return true;
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
                    if (card.getSuit() == Suit.KIER) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getSuit() == Suit.KIER) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getSuit() == Suit.KIER) {
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
                    if (card.getSuit() == Suit.KIER) {
                        pointWrapper.add(new PointWrapper(0, POINTS));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getSuit() == Suit.KIER) {
                        pointWrapper.add(new PointWrapper(POINTS, 0));
                    }
                });
                return pointWrapper;
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                PointWrapper pointWrapper = new PointWrapper(0, 0);
                cardsPlayer.forEach((card) -> {
                    if (card.getSuit() == Suit.KIER) {
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
