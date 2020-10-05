package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.history.HistoryGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameHistory {

    public static void main(String[] args) {
        HistoryGame game = new HistoryGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.history.name();
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
                return "Dějiny";
            case da:
                return "Historie spil";
            case de:
                return "Geschichtsspiel";
            case el:
                return "Παιχνίδι Ιστορίας";
            case en:
                return "History Game";
            case es:
                return "Juego de historia";
            case fi:
                return "Historiapeli";
            case fr:
                return "Jeu d'histoire";
            case hi:
                return "इतिहास";
            case hr:
                return "Povijest";
            case hu:
                return "Történelem";
            case id:
                return "Sejarah Game";
            case it:
                return "Gioco di storia";
            case ja:
                return "歴史ゲーム";
            case ko:
                return "역사 게임";
            case ms:
                return "Permainan Sejarah";
            case nl:
                return "Geschiedenis spel";
            case no:
                return "Historie spill";
            case pl:
                return "Gra Historia";
            case pt:
                return "Jogo de História";
            case ro:
                return "Joc de istorie";
            case ru:
                return "История игры";
            case sk:
                return "História";
            case sv:
                return "Historia spel";
            case th:
                return "ประวัติศาสตร์";
            case tr:
                return "Tarih Oyunu";
            case uk:
                return "Історія гри";
            case vi:
                return "Lịch sử";
            case zh:
                return "历史游戏";
        }
        return null;
    }
}
