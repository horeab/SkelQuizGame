package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
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
                return "Astronomie";
            case da:
                return "Astronomi";
            case de:
                return "Astronomie";
            case el:
                return "Αστρονομία";
            case en:
                return "Astronomy";
            case es:
                return "Astronomía";
            case fi:
                return "Tähtitiede";
            case fr:
                return "Astronomie";
            case hi:
                return "खगोल";
            case hr:
                return "Astronomija";
            case hu:
                return "Csillagászat";
            case id:
                return "Astronomi";
            case it:
                return "Astronomia";
            case ja:
                return "天文学";
            case ko:
                return "천문학";
            case ms:
                return "Astronomi";
            case nl:
                return "Astronomie";
            case no:
                return "Astronomi";
            case pl:
                return "Astronomia";
            case pt:
                return "Astronomia";
            case ro:
                return "Astronomie";
            case ru:
                return "Астрономия";
            case sk:
                return "Astronómie";
            case sv:
                return "Astronomi";
            case th:
                return "ดาราศาสตร์";
            case tr:
                return "Astronomi";
            case uk:
                return "Астрономія";
            case vi:
                return "Thiên văn học";
            case zh:
                return "天文学";
        }
        return null;
    }
}
