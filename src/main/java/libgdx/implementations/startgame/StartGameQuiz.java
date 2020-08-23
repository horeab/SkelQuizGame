package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameQuiz {

    public static void main(String[] args) {
        QuizGame game = new QuizGame(
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
                        return getTitle();
                    }

                    @Override
                    public String getLanguage() {
                        return "en";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Světová geografie";
            case da:
                return "Verdensgeografi";
            case de:
                return "Weltgeografie";
            case el:
                return "Παγκόσμια γεωγραφία";
            case en:
                return "World Geography";
            case es:
                return "Geografia mundial";
            case fi:
                return "Maailman maantiede";
            case fr:
                return "Géographie du monde";
            case hi:
                return "विश्व का भूगोल";
            case hr:
                return "Svjetska geografija";
            case hu:
                return "Világföldrajz";
            case id:
                return "Geografi dunia";
            case it:
                return "Geografia mondiale";
            case ja:
                return "世界の地理";
            case ko:
                return "세계 지리";
            case ms:
                return "Geografi Dunia";
            case nl:
                return "Wereld Aardrijkskunde";
            case no:
                return "Verdensgeografi";
            case pl:
                return "Geografia świata";
            case pt:
                return "Geografia mundial";
            case ro:
                return "Geografia lumii";
            case ru:
                return "Мировая география";
            case sk:
                return "Svetová geografia";
            case sv:
                return "Världsgeografi";
            case th:
                return "ภูมิศาสตร์โลก";
            case tr:
                return "Dünya coğrafyası";
            case uk:
                return "Світова географія";
            case vi:
                return "Địa lý thế giới";
            case zh:
                return "世界地理";
        }
        return null;
    }
}
