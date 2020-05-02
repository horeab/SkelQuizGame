package libgdx.screens.implementations.flags;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screen.AbstractScreen;
import org.apache.commons.lang3.StringUtils;

public class FlagsLevelFinishedPopup extends LevelFinishedPopup {


    public FlagsLevelFinishedPopup(AbstractScreen abstractScreen,
                                   CampaignLevel currentCampaignLevel,
                                   GameContext gameContext) {
        super(abstractScreen, currentCampaignLevel, gameContext);
    }

    @Override
    protected GameContext getGameContext() {
        return new GameContextService().createGameContext(
                FlagsContainers.getAllQuestions(getCurrentCampaignLevel()));
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }
}