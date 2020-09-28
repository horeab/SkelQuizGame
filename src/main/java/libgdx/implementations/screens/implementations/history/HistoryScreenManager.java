package libgdx.implementations.screens.implementations.history;

import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreenManager;

public class HistoryScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(HistoryScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext) {
        showScreen(HistoryScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext);
    }
}
