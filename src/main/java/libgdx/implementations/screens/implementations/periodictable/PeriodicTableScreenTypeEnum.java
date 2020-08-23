package libgdx.implementations.screens.implementations.periodictable;

import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum PeriodicTableScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new PeriodicTableCampaignScreen();
        }
    },

    PERIODICTABLE_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new PeriodicTableScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new PeriodicTableGameScreen((GameContext) params[0]);
        }
    },
}