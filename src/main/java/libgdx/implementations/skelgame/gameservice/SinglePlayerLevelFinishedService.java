package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameUser;

public class SinglePlayerLevelFinishedService extends LevelFinishedService {

    protected SinglePlayerLevelFinishedService() {
    }

    @Override
    public UsersWithLevelFinished createUsersWithGameFinished(GameContext gameContext) {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        UsersWithLevelFinished usersWithLevelFinished = new UsersWithLevelFinished();
        if (isGameFinished(gameContext)) {
            if (isGameWon(gameUser)) {
                usersWithLevelFinished.addUserThatWon(gameUser);
            } else {
                usersWithLevelFinished.addUserThatLost(gameUser);
            }
        }
        return usersWithLevelFinished;
    }

    protected boolean isGameWon(GameUser gameUser) {
        return gameUser.getWonQuestions() >= correctAnsweredQuestionsForGameSuccess(gameUser.getTotalNrOfQuestions());
    }

    @Override
    public boolean isGameFinished(GameContext gameContext) {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        return gameUser.getFinishedQuestions() == gameUser.getTotalNrOfQuestions();
    }


}
