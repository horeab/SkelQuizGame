package libgdx.implementations.screens.implementations.periodictable;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class PeriodicTableScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(CreatorDependenciesContainer.getCreator(PeriodicTableCreatorDependencies.class).getQuestionCreator(PeriodicTableDifficultyLevel._0,
//                PeriodicTableCategoryEnum.cat0).getAllQuestions());
//        Collections.shuffle(questions);
//        questions = questions.subList(0, 15);
//        GameContext gameContext = new GameContextService().createGameContext
//                (0, questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, null);
//        showPeriodicTableScreen();
        showCampaignScreen();
    }

    public void showPeriodicTableScreen() {
        showScreen(PeriodicTableScreenTypeEnum.PERIODICTABLE_SCREEN);
    }

    @Override
    public void showCampaignScreen() {
        showScreen(PeriodicTableScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(PeriodicTableScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
