package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.paintings.PaintingsGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGamePaintings {

    public static void main(String[] args) {
        PaintingsGame game = new PaintingsGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.paintings.name();
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
                        return Language.it.name();
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Slavné Obrazy";
            case da:
                return "Berømte Malerier";
            case de:
                return "Berühmte Gemälde";
            case el:
                return "Διάσημοι πίνακες ζωγραφικής";
            case en:
                return "Famous Paintings";
            case es:
                return "Pinturas Famosas";
            case fi:
                return "Kuuluisia Maalauksia";
            case fr:
                return "Tableaux Célèbres";
            case hi:
                return "प्रसिद्ध चित्रकारी";
            case hr:
                return "Poznate Slike";
            case hu:
                return "Híres Festmények";
            case id:
                return "Lukisan Terkenal";
            case it:
                return "Quadri Famosi";
            case ja:
                return "有名な絵画";
            case ko:
                return "유명한 그림";
            case ms:
                return "Lukisan Terkenal";
            case nl:
                return "Beroemde Schilderijen";
            case no:
                return "Berømte Malerier";
            case pl:
                return "Znane Obrazy";
            case pt:
                return "Pinturas Famosas";
            case ro:
                return "Picturi Celebre";
            case ru:
                return "Знаменитые картины";
            case sk:
                return "Slávne Maľby";
            case sv:
                return "Berömda Målningar";
            case th:
                return "ชื่อภาพเขียน";
            case tr:
                return "Ünlü Tablolar";
            case uk:
                return "Відомі картини";
            case vi:
                return "Bức tranh nổi tiếng";
            case zh:
                return "名画";
        }
        return null;
    }
}
