package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.periodictable.PeriodicTableGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGamePeriodicTable {

    public static void main(String[] args) {

        //TODO CHANGE////**********//////////////////////////////////////
        //**********//////////////////////////////////////
        //**********//////////////////////////////////////
        //**********//////////////////////////////////////
        PeriodicTableGame game = new PeriodicTableGame(
                new DefaultAppInfoService() {
                    @Override
                    //TODO CHANGE//////////////////////////////////////////
                    //**********//////////////////////////////////////
                    //**********//////////////////////////////////////
                    //**********//////////////////////////////////////
                    public String getGameIdPrefix() {
                        return GameIdEnum.periodictable.name();
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

                    @Override
                    public boolean isPortraitMode() {
                        return false;
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }


    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Periodická Tabulka";
            case da:
                return "Periodiske System";
            case de:
                return "Periodensystem";
            case el:
                return "Περιοδικός πίνακας";
            case en:
                return "The Periodic Table Quiz";
            case es:
                return "Tabla Periódica";
            case fi:
                return "Jaksollinen Järjestelmä";
            case fr:
                return "Tableau Périodique";
            case hi:
                return "आवर्त सारणी";
            case hr:
                return "Periodni Sustav Elemenata";
            case hu:
                return "Periódusos Rendszere";
            case id:
                return "Tabel Periodik";
            case it:
                return "Tavola Periodica";
            case ja:
                return "周期表";
            case ko:
                return "주기율표";
            case ms:
                return "Jadual Berkala";
            case nl:
                return "Periodiek Systeem";
            case no:
                return "Periodesystemet";
            case pl:
                return "Układ Okresowy Pierwiastków";
            case pt:
                return "Tabela Periódica";
            case ro:
                return "Tabelul Periodic";
            case ru:
                return "Периодическая таблица";
            case sk:
                return "Periodická Tabuľka";
            case sv:
                return "Periodiska Systemet";
            case th:
                return "ตารางธาตุ";
            case tr:
                return "Periyodik Tablo";
            case uk:
                return "Періодична таблиця";
            case vi:
                return "Bảng Tuần Hoàn";
            case zh:
                return "元素周期表测验";
        }
        return null;
    }
}
