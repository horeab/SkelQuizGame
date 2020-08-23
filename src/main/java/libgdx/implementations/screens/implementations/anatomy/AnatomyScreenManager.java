package libgdx.implementations.screens.implementations.anatomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class AnatomyScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(AnatomyQuestionCategoryEnum.cat5, 10)), MathCampaignLevelEnum.LEVEL_0_0);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
