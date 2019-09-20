package libgdx.screens.implementations.hangman;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.screens.implementations.geoquiz.GameScreen;

public class HangmanGameScreen extends GameScreen {

    private CampaignLevel campaignLevel;

    public HangmanGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {

    }

    public void goToNextQuestionScreen() {
        screenManager.showCampaignGameScreen(gameContext, campaignLevel);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(QuizGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
    }
}
