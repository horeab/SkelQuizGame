package libgdx.screens.implementations.painting;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class PaintingsScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(AnatomyQuestionCategoryEnum.cat5, 10)), AnatomyCampaignLevelEnum.LEVEL_0_0);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(PaintingsScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(PaintingsScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
