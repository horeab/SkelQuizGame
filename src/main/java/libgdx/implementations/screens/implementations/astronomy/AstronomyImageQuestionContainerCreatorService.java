package libgdx.implementations.screens.implementations.astronomy;

import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.ImageClickQuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.utils.SoundUtils;

public class AstronomyImageQuestionContainerCreatorService extends ImageClickQuestionContainerCreatorService {

    private AstronomyPreferencesManager astronomyPreferencesManager = new AstronomyPreferencesManager();
    public AstronomyImageQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected void processAfterAnswerPressed(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.WON) {
            astronomyPreferencesManager.putLevelScore((AstronomyCategoryEnum) gameQuestionInfo.getQuestion().getQuestionCategory(), gameContext.getCurrentUserGameUser().getWonQuestions());
            SoundUtils.playSound(AstronomySpecificResource.level_success);
        } else if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.LOST) {
            SoundUtils.playSound(AstronomySpecificResource.level_fail);
        }
    }

}