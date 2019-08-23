package libgdx.startgame;

import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.skelgame.QuizGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;
import libgdx.utils.startgame.test.DefaultBillingService;
import libgdx.utils.startgame.test.DefaultFacebookService;

public class StartGame {

    public static void main(String[] args) {
        QuizGame game = new QuizGame(
                new DefaultFacebookService(),
                new DefaultBillingService(),
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                            return GameIdEnum.quizgame.name();
                    }

                    @Override
                    public float gameScreenTopMargin() {
                        return super.gameScreenTopMargin();
                    }

                    @Override
                    public String getAppName() {
                        return "Crossword Garden";
                    }

                    @Override
                    public String getLanguage() {
                        return "ro";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }
}
