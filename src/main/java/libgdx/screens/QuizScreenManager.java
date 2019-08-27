package libgdx.screens;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.QuizCampaignLevelEnum;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.ScreenTypeEnum;

public class QuizScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(ScreenTypeEnum.MAIN_MENU_SCREEN);
        showScreen(ScreenTypeEnum.GAME_SCREEN, QuizCampaignLevelEnum.LEVEL_0_0);
//        showCampaignScreen();
    }

    public void showGameScreen(GameContext gameContext) {
        showScreen(ScreenTypeEnum.GAME_SCREEN, gameContext);
    }

    public void showCampaignScreen() {
        showScreen(ScreenTypeEnum.CAMPAIGN_SCREEN);
    }

}
