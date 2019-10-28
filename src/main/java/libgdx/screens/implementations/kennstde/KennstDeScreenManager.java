package libgdx.screens.implementations.kennstde;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.QuestionConfig;
import libgdx.implementations.kennstde.KennstDeCampaignLevelEnum;
import libgdx.implementations.kennstde.KennstDeQuestionCategoryEnum;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.implementations.hangman.HangmanScreenTypeEnum;

public class KennstDeScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_GAME_SCREEN, new GameContextService().createGameContext(new QuestionConfig(KennstDeQuestionCategoryEnum.cat3, 10)), KennstDeCampaignLevelEnum.LEVEL_0_0);
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(KennstDeScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
