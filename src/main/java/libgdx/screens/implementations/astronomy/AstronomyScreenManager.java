package libgdx.screens.implementations.astronomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AstronomyScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions(Arrays.asList(AstronomyDifficultyLevel._0), AstronomyCategoryEnum.cat5));
//        Collections.shuffle(questions);
//        questions = questions.subList(0, 7);
//        GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, null);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
