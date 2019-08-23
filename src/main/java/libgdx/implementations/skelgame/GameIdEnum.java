package libgdx.implementations.skelgame;

import libgdx.game.GameId;
import libgdx.game.SubGameDependencyManager;

public enum GameIdEnum implements GameId {

    quizgame(QuizGameDependencyManager.class),;

    private Class<? extends SubGameDependencyManager> dependencyManagerClass;

    GameIdEnum(Class<? extends QuizGameDependencyManager> dependencyManagerClass) {
        this.dependencyManagerClass = dependencyManagerClass;
    }

    @Override
    public SubGameDependencyManager getDependencyManager() {
        try {
            return dependencyManagerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
