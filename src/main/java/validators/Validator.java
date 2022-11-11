package validators;

import app.Main;
import data.PointWrapper;
import data.User;
import enums.Responses;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Validator {

    public abstract boolean isMoveCorrect(User loggedInUser);

    public abstract PointWrapper evaluateMove(User loggedInUser);

    public void setHasTurn(User enemy, boolean hasTurn, User loggedInUser, boolean hasTurn1) {
        enemy.setHasTurn(hasTurn);
        loggedInUser.setHasTurn(hasTurn1);
    }

    protected static void writePoints(User user, int userPoints, int enemyPoints) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(user.getSendEnemyCardSocket().getOutputStream());
        dataOut.writeUTF(Responses.POINTS.name());
        dataOut.writeUTF(String.valueOf(userPoints));
        dataOut.writeUTF(Responses.ENEMY_POINTS.name());
        dataOut.writeUTF(String.valueOf(enemyPoints));
    }

    public void sendEvaluationToUsers(User loggedInUser, int POINTS, int enemyPoints) {
        int indexOfEnemy = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().indexOf(loggedInUser) == 0 ? 1 : 0;
        var enemy = Main.rooms.get(loggedInUser.getRoomNumber().ordinal()).getPlayers().get(indexOfEnemy);
        if (loggedInUser.getCardPlayed() == null || enemy.getCardPlayed() == null) return;
        try {
            writePoints(loggedInUser, POINTS, enemyPoints);
            writePoints(enemy, enemyPoints, POINTS);
        } catch (IOException e) {
            System.out.println("Couldn't send points");
            return;
        }

        var room = Main.rooms.get(loggedInUser.getRoomNumber().ordinal());
        room.getDeck().getCards().add(loggedInUser.getCardPlayed());
        room.getDeck().getCards().add(enemy.getCardPlayed());
        room.setSubRound(room.getSubRound() + 1);
        loggedInUser.getCardsInHand().remove(loggedInUser.getCardPlayed());
        enemy.getCardsInHand().remove(enemy.getCardPlayed());
        loggedInUser.setCardPlayed(null);
        enemy.setCardPlayed(null);
        if (POINTS < enemyPoints) {
            loggedInUser.setFirstTurn(true);
            enemy.setFirstTurn(false);
        }
    }
}
