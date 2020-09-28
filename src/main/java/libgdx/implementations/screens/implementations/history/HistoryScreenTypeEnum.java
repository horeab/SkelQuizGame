package libgdx.implementations.screens.implementations.history;

import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum HistoryScreenTypeEnum implements ScreenType {


    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HistoryCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HistoryGameScreen((GameContext) params[0], null);
//            return new HistoryGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}