package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameAnatomy {

    public static void main(String[] args) {
        AnatomyGame game = new AnatomyGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.anatomy.name();
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
                return "Anatomie";
            case da:
                return "Anatomi";
            case de:
                return "Anatomie Spiel";
            case el:
                return "Ανατομία";
            case en:
                return "Anatomy Game";
            case es:
                return "Anatomía";
            case fi:
                return "Anatomia";
            case fr:
                return "Anatomie";
            case hi:
                return "एनाटॉमी";
            case hr:
                return "Anatomija";
            case hu:
                return "Anatómia";
            case id:
                return "Anatomi";
            case it:
                return "Anatomia";
            case ja:
                return "解剖学";
            case ko:
                return "해부";
            case ms:
                return "Anatomi";
            case nl:
                return "Anatomie";
            case no:
                return "Anatomi-spill";
            case pl:
                return "Anatomia";
            case pt:
                return "Anatomia";
            case ro:
                return "Anatomie";
            case ru:
                return "Игра Анатомия";
            case sk:
                return "Anatómia";
            case sv:
                return "Anatomi";
            case th:
                return "กายวิภาคศาสตร์";
            case tr:
                return "Anatomi Oyunu";
            case uk:
                return "Анатомія";
            case vi:
                return "Giải phẫu học";
            case zh:
                return "解剖学";
        }
        return null;
    }
}
