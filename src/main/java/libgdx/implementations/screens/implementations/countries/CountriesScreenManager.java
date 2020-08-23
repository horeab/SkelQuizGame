package libgdx.implementations.screens.implementations.countries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.conthistory.ConthistoryCampaignLevelEnum;
import libgdx.implementations.countries.hangman.CountriesHangmanCreatorDependencies;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;

public class CountriesScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(new CountriesHangmanCreatorDependencies().getPopulationQuestions());
        Collections.shuffle(questions);
//        questions = questions.subList(0, 65);
        GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
        showCampaignGameScreen(gameContext, ConthistoryCampaignLevelEnum.LEVEL_0_0);
//        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(CountriesScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(CountriesScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
