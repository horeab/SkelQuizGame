package libgdx.screens.implementations.hangman;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class HangmanScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(HangmanScreenTypeEnum.MAIN_MENU_SCREEN);
//        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
