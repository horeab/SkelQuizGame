package libgdx.implementations.screens.implementations.geoquiz;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;
import libgdx.implementations.screens.implementations.hangman.HangmanMainMenuScreen;

public enum QuizScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new HangmanMainMenuScreen();
        }
    },

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new GeoQuizCampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return null;
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new CampaignGameScreen((GameContext) params[0], (CampaignLevel) params[1]);
        }
    },
}