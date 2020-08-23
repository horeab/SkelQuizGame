package libgdx.implementations.screens.implementations.judetelerom;

import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum JudeteleRomScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new JudeteleRomCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new JudeteleRomGameScreen((GameContext) params[0]);
        }
    },
}