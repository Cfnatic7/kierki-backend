package validators;

import data.User;
import enums.Responses;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Validator {

    public abstract boolean isMoveCorrect(User loggedInUser);

    public abstract void evaluateMove(User loggedInUser) throws IOException;

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

    protected static void setTurns(User loggedInUser, User enemy, boolean ourTurn, boolean enemyTurn) {
        loggedInUser.setFirstTurn(ourTurn);
        loggedInUser.setHasTurn(ourTurn);
        enemy.setFirstTurn(enemyTurn);
        enemy.setHasTurn(enemyTurn);
    }
}
