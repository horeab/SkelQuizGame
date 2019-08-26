package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.Question;

import java.util.HashMap;
import java.util.Map;

public abstract class GameServiceContainer {

    private static Map<Question, GameService> gameServices = new HashMap<>();

    public static GameService getGameService(Class<? extends GameService> gameServiceClass, Question question) {
        GameService instance = gameServices.get(question);
        if (instance == null) {
            try {
                instance = gameServiceClass.getConstructor(Question.class).newInstance(question);
                gameServices.put(question, instance);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return instance;
    }

    public static GameService getGameService(GameQuestionInfo gameQuestionInfo) {
        return getGameService(gameQuestionInfo.getQuestion());
    }

    public static GameService getGameService(Question question) {
        return CreatorDependenciesContainer.getCreator(question.getQuestionCategory().getCreatorDependencies())
                .getGameService(question);
    }

    public static void clearContainer() {
        gameServices.clear();
    }
}
