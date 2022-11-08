package validators;

import app.Main;
import data.User;
import enums.Responses;
import java.io.DataOutputStream;
import java.io.IOException;

public class FirstRoundValidator {

    public final static int POINTS = -20;

//    public FirstRoundValidator() {
//
//    }

    public static boolean isMoveCorrect(User loggedInUser) {
        if (!loggedInUser.hasTurn()) return false;
        if (loggedInUser.isFirstTurn()) {
            return true;
        } else {
            var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
            int indexOfEnemy = room.getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
            var enemyPlayer = room.getPlayers().get(indexOfEnemy);
            if (enemyPlayer.getCardPlayed() == null) return true;
            var filteredCards = enemyPlayer
                    .getCardsInHand()
                    .stream()
                    .filter((card) -> card.getSuit() == enemyPlayer.getCardPlayed().getSuit()).toList();
            if (filteredCards.size() > 0 && loggedInUser.getCardPlayed().getSuit() != enemyPlayer.getCardPlayed().getSuit())
                return false;
            else if (filteredCards.size() > 0 && loggedInUser.getCardPlayed().getSuit() == enemyPlayer.getCardPlayed().getSuit())
                return true;
            else return true;
        }
    }

    public static void evaluateMove(User loggedInUser) throws IOException {
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
        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        room.getDeck().getCards().add(loggedInUser.getCardPlayed());
        room.getDeck().getCards().add(enemy.getCardPlayed());
        loggedInUser.setCardPlayed(null);
        enemy.setCardPlayed(null);
    }

    private static void writePoints(User user, int userPoints, int enemyPoints) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(user.getSendEnemyCardSocket().getOutputStream());
        dataOut.writeUTF(Responses.POINTS.name());
        dataOut.writeUTF(String.valueOf(userPoints));
        dataOut.writeUTF(Responses.ENEMY_POINTS.name());
        dataOut.writeUTF(String.valueOf(enemyPoints));
    }

    private static void setTurns(User loggedInUser, User enemy, boolean ourTurn, boolean enemyTurn) {
        loggedInUser.setFirstTurn(ourTurn);
        loggedInUser.setHasTurn(ourTurn);
        enemy.setFirstTurn(enemyTurn);
        enemy.setHasTurn(enemyTurn);
    }
}
