package libgdx;

import org.junit.Test;

import libgdx.campaign.CampaignGame;
import libgdx.constants.Language;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;
import libgdx.utils.startgame.test.DefaultBillingService;
import libgdx.utils.startgame.test.DefaultFacebookService;

public class GeoQuizGameServiceTest extends GameServiceTest {


    @Test
    public void testAllQuestions() throws Exception {
        testQuestions();
    }

    @Override
    protected DefaultAppInfoService getAppInfoService() {
        return getDefaultAppInfoService();
    }

    private DefaultAppInfoService getDefaultAppInfoService() {
        return new DefaultAppInfoService() {
            @Override
            public String getGameIdPrefix() {
                return GameIdEnum.quizgame.name();
            }
        };
    }

    @Override
    public CampaignGame createGame() {
        return new QuizGame(new DefaultFacebookService(), new DefaultBillingService(), getDefaultAppInfoService());
    }

    @Override
    protected Language getStartLanguage() {
        return Language.cs;
    }
}
