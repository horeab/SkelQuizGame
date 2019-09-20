package libgdx.screens.implementations.hangman;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.implementations.geoquiz.QuizScreenTypeEnum;

public class HangmanScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
