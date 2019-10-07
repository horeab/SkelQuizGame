package libgdx.screens.implementations.hangman;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.screen.AbstractScreenManager;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenTypeEnum;

public class HangmanScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        HangmanCampaignLevelEnum campaignLevel = HangmanCampaignLevelEnum.LEVEL_0_0;
//        showCampaignGameScreen(new GameContextService().createGameContext(new CampaignLevelEnumService(campaignLevel).getQuestionConfig(HangmanGameScreen.TOTAL_QUESTIONS)), campaignLevel);
        showScreen(HangmanScreenTypeEnum.MAIN_MENU_SCREEN);
    }

    public void showCampaignScreen() {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(HangmanScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
