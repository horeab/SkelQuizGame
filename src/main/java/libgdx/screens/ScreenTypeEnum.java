package libgdx.screens;

import libgdx.campaign.CampaignLevel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;
import libgdx.screens.mainmenu.MainMenuScreen;

public enum ScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new CampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen((CampaignLevel) params[0]);
        }
    },
}