package validators;

import app.Main;
import data.User;
import java.io.IOException;

public class FirstRoundValidator extends Validator {

    public final int POINTS = -20;

    public FirstRoundValidator() {

    }

    @Override
    public boolean isMoveCorrect(User loggedInUser) {
        int enemyIndex = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
        var enemy = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().get(enemyIndex);
        if (!loggedInUser.hasTurn()) {
            return false;
        }
        if (loggedInUser.isFirstTurn()) {
            return true;
        } else {
            var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
            int indexOfEnemy = room.getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
            var enemyPlayer = room.getPlayers().get(indexOfEnemy);
            if (enemyPlayer.getCardPlayed() == null) {
                return false;
            }
            if (loggedInUser.getCardPlayed().getSuit() == enemyPlayer.getCardPlayed().getSuit()) {
                return true;
            }
            var filteredCards = loggedInUser
                    .getCardsInHand()
                    .stream()
                    .filter((card) -> card.getSuit() == enemyPlayer.getCardPlayed().getSuit()).toList();
            if (filteredCards.size() > 0 && loggedInUser.getCardPlayed().getSuit() != enemyPlayer.getCardPlayed().getSuit()) {
                System.out.println("Number of cards of the same color in possesion: " + filteredCards.size());
                System.out.println(filteredCards.get(0).getSuit());
                System.out.println(filteredCards.get(0).getRank());
                return false;
            }
            else {
                return true;
            }
        }
    }

    @Override
    public void evaluateMove(User loggedInUser) throws IOException {
        System.out.println("Evaluating move");
        var enemy = Main
                .rooms
                .get(loggedInUser.getRoomNumber().ordinal())
                .getPlayers()
                .get(Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0);

        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return;
        if (loggedInUser.isFirstTurn()) {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, POINTS, 0, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, POINTS, 0, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, 0, POINTS, enemy, false, true);
            }
        }
        else {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                handlePlayersConfiguration(loggedInUser, 0, POINTS, enemy, false, true);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, POINTS, 0, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                handlePlayersConfiguration(loggedInUser, 0, POINTS, enemy, false, true);
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

    private void handlePlayersConfiguration(User loggedInUser, int POINTS, int enemyPoints, User enemy, boolean ourTurn, boolean enemyTurn) throws IOException {
        writePoints(loggedInUser, POINTS, enemyPoints);
        writePoints(enemy, enemyPoints, POINTS);
        setTurns(loggedInUser, enemy, ourTurn, enemyTurn);
    }
}
