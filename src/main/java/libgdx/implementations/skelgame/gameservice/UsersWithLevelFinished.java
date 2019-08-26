package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameUser;

import java.util.ArrayList;
import java.util.List;

public class UsersWithLevelFinished {

    private List<GameUser> usersThatWon;
    private List<GameUser> usersThatLost;

    public UsersWithLevelFinished() {
        this.usersThatWon = new ArrayList<>();
        this.usersThatLost = new ArrayList<>();
    }

    public GameUser getGameUserThatWon() {
        return usersThatWon.isEmpty() ? null : usersThatWon.get(0);
    }

    public GameUser getGameUserThatLost() {
        return usersThatLost.isEmpty() ? null : usersThatLost.get(0);
    }

    public void addUserThatWon(GameUser gameUser) {
        usersThatWon.add(gameUser);
    }

    public void addUserThatLost(GameUser gameUser) {
        usersThatLost.add(gameUser);
    }
}