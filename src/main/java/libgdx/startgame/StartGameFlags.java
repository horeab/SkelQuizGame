package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.flags.FlagsGame;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class StartGameFlags {

    public static void main(String[] args) {
        FlagsGame game = new FlagsGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.flags.name();
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
                return "Světové Vlajky";
            case da:
                return "Verdensflagg";
            case de:
                return "Weltflaggen";
            case el:
                return "Παγκόσμιες σημαίες";
            case en:
                return "World Flags";
            case es:
                return "Banderas del Mundo";
            case fi:
                return "Maailman Liput";
            case fr:
                return "Drapeaux du Monde";
            case hi:
                return "विश्व झंडे";
            case hr:
                return "Svjetske Zastave";
            case hu:
                return "Világ Zászlók";
            case id:
                return "Bendera Dunia";
            case it:
                return "Bandiere del Mondo";
            case ja:
                return "世界の旗";
            case ko:
                return "세계 플래그";
            case ms:
                return "Bendera Dunia";
            case nl:
                return "Wereld Vlaggen";
            case no:
                return "Verdensflagg";
            case pl:
                return "Flagi Świata";
            case pt:
                return "Bandeiras do Mundo";
            case ro:
                return "Steagurile Lumii";
            case ru:
                return "Флаги мира";
            case sk:
                return "Svetové Vlajky";
            case sv:
                return "Världsflaggor";
            case th:
                return "ธงโลก";
            case tr:
                return "Dünya Bayrakları";
            case uk:
                return "Світові прапори";
            case vi:
                return "Cờ thế giới";
            case zh:
                return "世界国旗";
        }
        return null;
    }
}
