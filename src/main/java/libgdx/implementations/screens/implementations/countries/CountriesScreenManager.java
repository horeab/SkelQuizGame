package libgdx.implementations.screens.implementations.countries;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class CountriesScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {

//        showCampaignGameScreen(null, CountriesCampaignLevelEnum.LEVEL_0_2);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(CountriesScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
//        Map<CountriesCampaignLevelEnum, CountriesCategoryEnum> m = new HashMap<>();
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_0, CountriesCategoryEnum.cat0);
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_1, CountriesCategoryEnum.cat1);
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_2, CountriesCategoryEnum.cat2);
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_3, CountriesCategoryEnum.cat3);
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_4, CountriesCategoryEnum.cat4);
//        m.put(CountriesCampaignLevelEnum.LEVEL_0_5, CountriesCategoryEnum.cat5);
//
//        Question question = new Question(0, CountriesDifficultyLevel._0, m.get(campaignLevel), "");
//        gameContext = new GameContextService().createGameContext(Collections.singletonList(question));

        showScreen(CountriesScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
