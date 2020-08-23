package libgdx.implementations.screens.implementations.anatomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum AnatomyScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AnatomyCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AnatomyGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}