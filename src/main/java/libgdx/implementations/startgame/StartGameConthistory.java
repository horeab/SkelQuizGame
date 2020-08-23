package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameConthistory {

    public static void main(String[] args) {
        ConthistoryGame game = new ConthistoryGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.conthistory.name();
                    }

                    @Override
                    public float gameScreenTopMargin() {
                        return super.gameScreenTopMargin();
                    }

                    @Override
                    public String getAppName() {
                        return getTitle();
                    }

                    @Override
                    public String getLanguage() {
                        return Language.de.name();
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case en:
                return "European History";
            case ro:
                return "Istoria Europei";
            case de:
                return "Europäische\nGeschichte";
        }
        return null;
    }
}
