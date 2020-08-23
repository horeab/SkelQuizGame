package libgdx.implementations.screens.implementations.hangmanarena;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class HangmanArenaScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions(Arrays.asList(AnimalsQuestionDifficultyLevel._0), AnimalsQuestionCategoryEnum.cat5));
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
