package libgdx.implementations.screens.implementations.anatomy;

import libgdx.campaign.CampaignLevel;
import libgdx.game.ScreenManager;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.spec.AnatomyGameType;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class AnatomyScreenManager extends ScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(AnatomyQuestionCategoryEnum.cat5, 10)), MathCampaignLevelEnum.LEVEL_0_0);
        showCampaignScreen();
//        showLevelScreen(AnatomyCampaignLevelEnum.LEVEL_0_11);
    }

    public void showCampaignScreen() {
        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showLevelScreen(AnatomyCampaignLevelEnum campaignLevelEnum) {
        showScreen(AnatomyScreenTypeEnum.LEVEL_SCREEN, campaignLevelEnum);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel, AnatomyGameType anatomyGameType) {
        showScreen(AnatomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel, anatomyGameType);
    }
}
