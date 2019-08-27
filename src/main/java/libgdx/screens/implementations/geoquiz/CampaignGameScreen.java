package libgdx.screens.implementations.geoquiz;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.implementations.skelgame.QuizGame;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;

public class CampaignGameScreen extends GameScreen {

    private CampaignLevel campaignLevel;

    public CampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    public void goToNextQuestionScreen() {
        screenManager.showCampaignGameScreen(gameContext, campaignLevel);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
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
