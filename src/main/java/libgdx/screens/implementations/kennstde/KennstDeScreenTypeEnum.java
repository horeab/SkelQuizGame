package libgdx.screens.implementations.kennstde;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;
import libgdx.screens.implementations.hangman.HangmanCampaignScreen;
import libgdx.screens.implementations.hangman.HangmanGameScreen;
import libgdx.screens.implementations.hangman.HangmanMainMenuScreen;

public enum KennstDeScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HangmanMainMenuScreen();
        }
    },

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HangmanCampaignScreen();
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HangmanGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}