package libgdx.implementations.screens.implementations.astronomy;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableDifficultyLevel;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreenManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AstronomyScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        List<Question> questions = new ArrayList<>();
//        questions.addAll(new AstronomyQuestionContainerCreatorService().getAllQuestions(Arrays.asList(PeriodicTableDifficultyLevel._0), PeriodicTableCategoryEnum.cat5));
//        Collections.shuffle(questions);
//        questions = questions.subList(0, 7);
//        GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
//        showCampaignGameScreen(gameContext, null);
        showCampaignScreen();
//        showCampaignDragDropGameScreen(null, null);
//        showDetailedCampaignScreen(AstronomyGameType.SOLAR_SYSTEM);
    }

    public void showCampaignScreen() {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_SCREEN);
    }

    public void showDetailedCampaignScreen(AstronomyGameType astronomyGameType) {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_DETAILED_SCREEN, astronomyGameType);
    }

    public void showCampaignDragDropGameScreen(AstronomyPlanetsGameType planetsGameType, List<Planet> allPlanets) {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_DRAG_DROP_GAME_SCREEN, planetsGameType, allPlanets);
    }

    public void showCampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel, AstronomyGameType astronomyGameType) {
        showScreen(AstronomyScreenTypeEnum.CAMPAIGN_GAME_SCREEN, gameContext, campaignLevel, astronomyGameType);
    }
}
