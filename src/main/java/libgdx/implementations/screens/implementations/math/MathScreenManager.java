package libgdx.implementations.screens.implementations.math;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class MathScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(MathScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(MathScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
