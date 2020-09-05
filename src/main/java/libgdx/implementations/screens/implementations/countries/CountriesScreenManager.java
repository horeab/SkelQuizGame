package libgdx.implementations.screens.implementations.countries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.countries.CountriesCampaignLevelEnum;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;

public class CountriesScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
        Map<CountriesCampaignLevelEnum, CountriesCategoryEnum> m = new HashMap<>();
        m.put(CountriesCampaignLevelEnum.LEVEL_0_0, CountriesCategoryEnum.cat0);
        m.put(CountriesCampaignLevelEnum.LEVEL_0_1, CountriesCategoryEnum.cat1);
        m.put(CountriesCampaignLevelEnum.LEVEL_0_2, CountriesCategoryEnum.cat2);
        m.put(CountriesCampaignLevelEnum.LEVEL_0_3, CountriesCategoryEnum.cat3);
        m.put(CountriesCampaignLevelEnum.LEVEL_0_4, CountriesCategoryEnum.cat4);
        m.put(CountriesCampaignLevelEnum.LEVEL_0_5, CountriesCategoryEnum.cat5);

        CountriesCampaignLevelEnum campL = CountriesCampaignLevelEnum.LEVEL_0_3;

        Question question = new Question(0, CountriesDifficultyLevel._0, m.get(campL), "");
//        questions = questions.subList(0, 65);
        GameContext gameContext = new GameContextService().createGameContext(Collections.singletonList(question));
        showCampaignGameScreen(gameContext, campL);
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
