package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameCountries {

    public static void main(String[] args) {
        CountriesGame game = new CountriesGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.countries.name();
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
                        return Language.ro.name();
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Země světa";
            case da:
                return "Lande i Verden";
            case de:
                return "Länder der Welt";
            case el:
                return "Χώρες του Κόσμου";
            case en:
                return "World Countries";
            case es:
                return "Países del Mundo";
            case fi:
                return "Maailman maat";
            case fr:
                return "Pays du Monde";
            case hr:
                return "Zemlje svjetskog";
            case hu:
                return "A Világ Országai";
            case id:
                return "Negara di Dunia";
            case it:
                return "Paesi del Mondo";
            case ms:
                return "Negara-negara di Dunia";
            case nl:
                return "Landen van de Wereld";
            case no:
                return "Verdens land";
            case pl:
                return "Państwa Świata";
            case pt:
                return "Países do Mundo";
            case ro:
                return "Țările lumii";
            case ru:
                return "Страны Мира";
            case sk:
                return "Štáty sveta";
            case sv:
                return "Världens länder";
            case tr:
                return "Dünya Ülkeleri";
            case uk:
                return "Країни світу";
        }
        return null;
    }
}
