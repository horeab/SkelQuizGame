package libgdx.implementations.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.judetelerom.JudeteleRomGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameJudeteleRom {

    public static void main(String[] args) {
        JudeteleRomGame game = new JudeteleRomGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.judetelerom.name();
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
            case ro:
                return "Judeţele României";
        }
        return null;
    }
}
