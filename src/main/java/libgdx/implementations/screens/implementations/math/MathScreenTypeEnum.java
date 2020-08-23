package libgdx.implementations.screens.implementations.math;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum MathScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}