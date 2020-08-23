package libgdx.implementations.screens.implementations.countries;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screen.AbstractScreen;

public class CountriesLevelFinishedPopup extends LevelFinishedPopup {


    public CountriesLevelFinishedPopup(AbstractScreen abstractScreen,
                                       CampaignLevel currentCampaignLevel,
                                       GameContext gameContext) {
        super(abstractScreen, currentCampaignLevel, gameContext);
    }

    @Override
    protected GameContext getGameContext() {
        return new GameContextService().createGameContext(
                CountriesContainers.getAllQuestions(getCurrentCampaignLevel()));
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }
}