package libgdx.implementations.screens.implementations.astronomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

import java.util.List;

public enum AstronomyScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AstronomyCampaignScreen();
        }
    },

    CAMPAIGN_DETAILED_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AstronomyDetailedCampaignScreen((AstronomyGameType) params[0]);
        }
    },

    CAMPAIGN_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AstronomyGameScreen((GameContext) params[0], (CampaignLevel) params[1], (AstronomyGameType) params[2]);
        }
    },

    CAMPAIGN_DRAG_DROP_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new AstronomyDragDropPlanetsScreen((AstronomyPlanetsGameType) params[0], (List<Planet>) params[1]);
        }
    },
}