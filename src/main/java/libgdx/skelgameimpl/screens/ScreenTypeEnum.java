package libgdx.skelgameimpl.screens;

import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;
import libgdx.skelgameimpl.screens.mainmenu.MainMenuScreen;

public enum ScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

}