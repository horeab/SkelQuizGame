package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.hangmanarena.HangmanArenaGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameHangmanArena {

    public static void main(String[] args) {
        HangmanArenaGame game = new HangmanArenaGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.hangmanarena.name();
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
                    public boolean isPortraitMode() {
                        return true;
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
                return "Šibenice";
            case da:
                return "Galgespil";
            case de:
                return "Galgenmännchen 2";
            case el:
                return "Κρεμάλα";
            case en:
                return "Hangman";
            case es:
                return "Ahorcado";
            case fi:
                return "Hirsipu";
            case fr:
                return "Le Pendu";
            case hr:
                return "Vješala";
            case hu:
                return "Akasztófa";
            case id:
                return "Indonesian Hangman";
            case it:
                return "L'impiccato";
            case ms:
                return "Malay Hangman";
            case nl:
                return "Galgje";
            case no:
                return "Norsk Hangman";
            case pl:
                return "Wisielec";
            case pt:
                return "Jogo da forca";
            case ro:
                return "Spânzurătoarea";
            case ru:
                return "Виселица";
            case sk:
                return "Slovenský Hangman";
            case sv:
                return "Hänga gubbe";
            case tr:
                return "Adam asmaca";
            case uk:
                return "Шибениця";
        }
        return null;
    }
}
