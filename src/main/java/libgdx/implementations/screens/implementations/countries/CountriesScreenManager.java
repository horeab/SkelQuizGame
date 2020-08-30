package libgdx.implementations.screens.implementations.countries;

import java.util.Collections;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.conthistory.ConthistoryCampaignLevelEnum;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;

public class CountriesScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
        Question question = new Question(0, CountriesDifficultyLevel._0, CountriesCategoryEnum.cat2, "");
//        questions = questions.subList(0, 65);
        GameContext gameContext = new GameContextService().createGameContext(Collections.singletonList(question));
        showCampaignGameScreen(gameContext, ConthistoryCampaignLevelEnum.LEVEL_0_2);
//        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(CountriesScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(CountriesScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
