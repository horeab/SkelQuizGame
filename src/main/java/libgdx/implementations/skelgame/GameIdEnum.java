package libgdx.implementations.skelgame;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.game.GameId;
import libgdx.game.SubGameDependencyManager;
import libgdx.implementations.anatomy.AnatomyDependencyManager;
import libgdx.implementations.geoquiz.QuizGameDependencyManager;
import libgdx.implementations.hangman.HangmanDependencyManager;
import libgdx.implementations.judetelerom.JudeteleRomDependencyManager;
import libgdx.implementations.kennstde.KennstDeDependencyManager;

public enum GameIdEnum implements GameId {

    judetelerom(JudeteleRomDependencyManager.class),
    anatomy(AnatomyDependencyManager.class),
    kennstde(KennstDeDependencyManager.class),
    quizgame(QuizGameDependencyManager.class),
    hangman(HangmanDependencyManager.class),;

    private Class<? extends SubGameDependencyManager> dependencyManagerClass;

    GameIdEnum(Class<? extends CampaignGameDependencyManager> dependencyManagerClass) {
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
