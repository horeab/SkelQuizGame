package libgdx.implementations.screens.implementations.geoquiz;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class QuizScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(QuizScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignScreen() {
        showMainScreen();
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(QuizScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
