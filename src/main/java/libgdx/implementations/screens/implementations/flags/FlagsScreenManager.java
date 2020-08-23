package libgdx.implementations.screens.implementations.flags;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;

public class FlagsScreenManager extends CampaignScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new QuestionCreator().getAllQuestions());
//        Collections.shuffle(questions);
////        questions = questions.subList(0, 65);
//        GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, FlagsCampaignLevelEnum.LEVEL_0_0);
        showCampaignScreen();
    }

    @Override
    public void showCampaignScreen() {
        showScreen(FlagsScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    @Override
    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        showScreen(FlagsScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel);
    }
}
