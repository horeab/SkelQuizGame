package libgdx.screens.implementations.painting;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.QuestionConfig;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.AnatomyQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsCampaignLevelEnum;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screens.implementations.anatomy.AnatomyScreenTypeEnum;

public class PaintingsScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(MathScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(PaintingsQuestionCategoryEnum.cat1, 10)), PaintingsCampaignLevelEnum.LEVEL_0_0);
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
