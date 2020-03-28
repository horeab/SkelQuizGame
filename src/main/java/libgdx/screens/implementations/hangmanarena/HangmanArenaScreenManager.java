package libgdx.screens.implementations.hangmanarena;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.implementations.hangmanarena.HangmanArenaQuestionCategoryEnum;
import libgdx.implementations.hangmanarena.HangmanArenaQuestionDifficultyLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.implementations.skelgame.question.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HangmanArenaScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions(Arrays.asList(HangmanArenaQuestionDifficultyLevel._0), HangmanArenaQuestionCategoryEnum.cat5));
//        Collections.shuffle(questions);
//        questions = questions.subList(0, 1);
//        GameContext gameContext = new GameContextService().createGameContext
//                (3, questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, null);
        showScreen(HangmanArenaScreenTypeEnum.MAIN_MENU_SCREEN);
//        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(HangmanArenaScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(HangmanArenaScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
