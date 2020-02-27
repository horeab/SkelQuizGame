package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameAstronomy {

    public static void main(String[] args) {
        AstronomyGame game = new AstronomyGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.astronomy.name();
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
                        return Language.en.name();
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "xxx";
            case da:
                return "xxx";
            case de:
                return "xxx";
            case el:
                return "xxx";
            case en:
                return "Astronomy";
            case es:
                return "xxx";
            case fi:
                return "xxx";
            case fr:
                return "xxx";
            case hi:
                return "xxx";
            case hr:
                return "xxx";
            case hu:
                return "xxx";
            case id:
                return "xxx";
            case it:
                return "xxx";
            case ja:
                return "xxx";
            case ko:
                return "xxx";
            case ms:
                return "xxx";
            case nl:
                return "xxx";
            case no:
                return "xxx";
            case pl:
                return "xxx";
            case pt:
                return "xxx";
            case ro:
                return "xxx";
            case ru:
                return "xxx";
            case sk:
                return "xxx";
            case sv:
                return "xxx";
            case th:
                return "xxx";
            case tr:
                return "xxx";
            case uk:
                return "xxx";
            case vi:
                return "xxx";
            case zh:
                return "xxx";
        }
        return null;
    }
}
