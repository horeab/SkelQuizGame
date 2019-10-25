package libgdx.screens.implementations.kennstde;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.implementations.hangman.HangmanScreenTypeEnum;

public class KennstDeScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
