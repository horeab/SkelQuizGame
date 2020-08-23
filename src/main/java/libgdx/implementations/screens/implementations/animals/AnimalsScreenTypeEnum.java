package libgdx.implementations.screens.implementations.animals;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum AnimalsScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AnimalsMainMenuScreen();
        }
    },

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AnimalsCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AnimalsGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}