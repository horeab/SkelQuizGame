package libgdx.implementations.skelgame;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.game.GameId;
import libgdx.game.SubGameDependencyManager;
import libgdx.implementations.anatomy.AnatomyDependencyManager;
import libgdx.implementations.geoquiz.QuizGameDependencyManager;
import libgdx.implementations.hangman.HangmanDependencyManager;
import libgdx.implementations.judetelerom.JudeteleRomDependencyManager;
import libgdx.implementations.kennstde.KennstDeDependencyManager;
import libgdx.implementations.math.MathDependencyManager;
import libgdx.implementations.paintings.PaintingsDependencyManager;

public enum GameIdEnum implements GameId {

    math(MathDependencyManager.class),
    judetelerom(JudeteleRomDependencyManager.class),
    anatomy(AnatomyDependencyManager.class),
    paintings(PaintingsDependencyManager.class),
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
