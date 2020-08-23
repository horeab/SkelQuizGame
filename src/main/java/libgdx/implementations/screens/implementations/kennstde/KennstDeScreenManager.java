package libgdx.implementations.screens.implementations.kennstde;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class KennstDeScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(KennstDeQuestionCategoryEnum.cat3, 10)), KennstDeCampaignLevelEnum.LEVEL_0_0);
       showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
