package libgdx.implementations.skelgame;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;

public abstract class CampaignScreenManager extends AbstractScreenManager {

    public abstract void showCampaignScreen();

    public abstract void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel);
}
