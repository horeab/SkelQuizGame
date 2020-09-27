package libgdx.implementations.screens.implementations.history;

import java.util.Arrays;

import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.screen.AbstractScreenManager;

public class HistoryScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        QuestionCreator questionCreator = new QuestionCreator();
        HistoryCategoryEnum cat = HistoryCategoryEnum.cat1;
//        HistoryCategoryEnum cat = HistoryCategoryEnum.cat0;
        GameContext gameContext = new GameContextService().createGameContext(questionCreator.getAllQuestions(Arrays.asList(HistoryDifficultyLevel.values()), cat));
        showScreen(HistoryScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext);
    }
}
