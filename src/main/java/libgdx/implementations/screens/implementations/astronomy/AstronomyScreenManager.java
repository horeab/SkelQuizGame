package libgdx.implementations.screens.implementations.astronomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class AstronomyScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions(Arrays.asList(PeriodicTableDifficultyLevel._0), PeriodicTableCategoryEnum.cat5));
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
