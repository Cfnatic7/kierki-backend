package validators;

import app.Main;
import data.Room;
import data.User;

import java.io.IOException;

public class SixthRoundValidator extends Validator{
    public final int POINTS = -75;

    private final FirstRoundValidator firstRoundValidator;


    public SixthRoundValidator() {
        firstRoundValidator = new FirstRoundValidator();
    }

    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        return firstRoundValidator.isMoveCorrect(loggedInUser);
    }

    @Override
    public void evaluateMove(User loggedInUser) throws IOException {
        System.out.println("Evaluating move");
        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        var enemy = Main
                .rooms
                .get(loggedInUser.getRoomNumber().ordinal())
                .getPlayers()
                .get(Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0);

        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return;
        if (loggedInUser.isFirstTurn()) {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false, room);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false, room);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true, room);
            }
        }
        else {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true, room);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, POINTS, 0, true, false, room);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, enemy, 0, POINTS, false, true, room);
            }
        }
        room.getDeck().getCards().add(loggedInUser.getCardPlayed());
        room.getDeck().getCards().add(enemy.getCardPlayed());
        loggedInUser.getCardsInHand().remove(loggedInUser.getCardPlayed());
        enemy.getCardsInHand().remove(enemy.getCardPlayed());
        loggedInUser.setCardPlayed(null);
        enemy.setCardPlayed(null);
    }

    private void handlePlayersConfiguration(User loggedInUser, User enemy,
                                            int POINTS,
                                            int enemyPoints,
                                            boolean ourTurn,
                                            boolean enemyTurn,
                                            Room room) throws IOException {
        if (room.getSubRound() == 7 || room.getSubRound() == 26) {
            writePoints(loggedInUser, POINTS, enemyPoints);
            writePoints(enemy, enemyPoints, POINTS);
        }
        setTurns(loggedInUser, enemy, ourTurn, enemyTurn);
    }
}
