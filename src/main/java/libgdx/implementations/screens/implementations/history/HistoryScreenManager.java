package libgdx.implementations.screens.implementations.history;

import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.screen.AbstractScreenManager;

public class HistoryScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        QuestionCreator questionCreator = new QuestionCreator();
        GameContext gameContext = new GameContextService().createGameContext(questionCreator.getAllQuestions());
        showScreen(HistoryScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, HistoryCampaignLevelEnum.LEVEL_0_0);
    }
}
