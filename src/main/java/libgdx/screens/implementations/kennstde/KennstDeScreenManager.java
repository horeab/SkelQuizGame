package libgdx.screens.implementations.kennstde;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.implementations.hangman.HangmanScreenTypeEnum;

public class KennstDeScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(HangmanScreenTypeEnum.MAIN_MENU_SCREEN);
    }

    public void showCampaignScreen() {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
