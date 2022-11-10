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
            loggedInUser.setCardPlayed(null);
            return false;
        }
        if (loggedInUser.isFirstTurn()) {
            setHasTurn(enemy, true, loggedInUser, false);
            return true;
        } else {
            var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
            int indexOfEnemy = room.getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
            var enemyPlayer = room.getPlayers().get(indexOfEnemy);
            if (enemyPlayer.getCardPlayed() == null) {
                loggedInUser.setCardPlayed(null);
                return false;
            }
            if (loggedInUser.getCardPlayed().getSuit() == enemyPlayer.getCardPlayed().getSuit()) {
                setHasTurn(enemy, true, loggedInUser, false);
                return true;
            }
            var filteredCards = loggedInUser
                    .getCardsInHand()
                    .stream()
                    .filter((card) -> card.getSuit() == enemyPlayer.getCardPlayed().getSuit()).toList();
            if (filteredCards.size() > 0 && loggedInUser.getCardPlayed().getSuit() != enemyPlayer.getCardPlayed().getSuit()) {
                loggedInUser.setCardPlayed(null);
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
                writePoints(loggedInUser, POINTS, 0);
                writePoints(enemy, 0, POINTS);
                setTurns(loggedInUser, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                writePoints(loggedInUser, POINTS, 0);
                writePoints(enemy, 0, POINTS);
                setTurns(loggedInUser, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                writePoints(loggedInUser, 0, POINTS);
                writePoints(enemy, POINTS, 0);
                setTurns(loggedInUser, enemy, false, true);
            }
        }
        else {
            if (loggedInUser.getCardPlayed().getSuit() != enemy.getCardPlayed().getSuit()) {
                writePoints(loggedInUser, 0, POINTS);
                writePoints(enemy, POINTS, 0);
                setTurns(loggedInUser, enemy, false, true);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() > enemy.getCardPlayed().getRank().ordinal()) {
                writePoints(loggedInUser, POINTS, 0);
                writePoints(enemy, 0, POINTS);
                setTurns(loggedInUser, enemy, true, false);
            } else if (loggedInUser.getCardPlayed().getRank().ordinal() < enemy.getCardPlayed().getRank().ordinal()) {
                writePoints(loggedInUser, 0, POINTS);
                writePoints(enemy, POINTS, 0);
                setTurns(loggedInUser, enemy, false, true);
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
}
