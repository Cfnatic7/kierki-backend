package validators;

import app.Main;
import data.Card;
import data.User;
import enums.Rank;
import enums.Suit;

import java.io.IOException;
import java.util.List;

public class ThirdRoundValidator extends Validator {

    public final int POINTS = -60;

    private final FirstRoundValidator firstRoundValidator;

    public ThirdRoundValidator() {
        this.firstRoundValidator = new FirstRoundValidator();
    }

    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        return firstRoundValidator.isMoveCorrect(loggedInUser);
    }

    @Override
    public void evaluateMove(User loggedInUser) {
        System.out.println("Evaluating move");
        var enemy = Main
                .rooms
                .get(loggedInUser.getRoomNumber().ordinal())
                .getPlayers()
                .get(Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0);

        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return;
        if (loggedInUser.isFirstTurn()) {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true);
            }
        }
        else {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true);
            }
        }
        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        room.getDeck().getCards().add(loggedInUser.getCardPlayed());
        room.getDeck().getCards().add(enemy.getCardPlayed());
        loggedInUser.getCardsInHand().remove(loggedInUser.getCardPlayed());
        enemy.getCardsInHand().remove(enemy.getCardPlayed());
        loggedInUser.setCardPlayed(null);
        enemy.setCardPlayed(null);
    }

    private void handlePlayersConfiguration(User loggedInUser, User enemy, int POINTS, int enemyPoints, boolean ourTurn, boolean enemyTurn) {
        List<Card> cards = List.of(loggedInUser.getCardPlayed(), enemy.getCardPlayed());
        cards.forEach(card -> {
            if (card.getRank() == Rank.DAMA) {
                try {
                    writePoints(loggedInUser, POINTS, enemyPoints);
                    writePoints(enemy, enemyPoints, POINTS);
                } catch (IOException e) {
                    System.out.println("Couldn't send cards");
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    writePoints(loggedInUser, 0, 0);
                    writePoints(enemy, 0, 0);
                } catch (IOException e) {
                    System.out.println("Couldn't send cards");
                    throw new RuntimeException(e);
                }
            }
        });
        setTurns(loggedInUser, enemy, ourTurn, enemyTurn);
    }
}
