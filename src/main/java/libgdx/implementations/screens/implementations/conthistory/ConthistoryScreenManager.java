package libgdx.implementations.screens.implementations.conthistory;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class ConthistoryScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions());
//        Collections.shuffle(questions);
//        questions = questions.subList(0, 9);
//        GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, null);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(ConthistoryScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(ConthistoryScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
