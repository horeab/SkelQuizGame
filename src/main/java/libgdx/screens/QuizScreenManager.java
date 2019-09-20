package libgdx.screens;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.QuestionConfig;
import libgdx.implementations.skelgame.QuizCampaignLevelEnum;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screen.AbstractScreenManager;

public class QuizScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(ScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(ScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
