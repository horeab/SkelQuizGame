package libgdx.implementations.countries.hangman;

import java.util.List;
import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.HintButtonType;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.RefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;

public class CountriesPopulationAreaNeighbCreatorDependencies extends CreatorDependencies {

    private CountriesQuestionPopulator countriesQuestionCreator;
    private List<String> allCountries;

    public CountriesPopulationAreaNeighbCreatorDependencies() {
        allCountries = CountriesGame.getInstance().getSubGameDependencyManager().getAllCountries();
    }

    @Override
    public GameService getGameService(Question question) {
        countriesQuestionCreator = new CountriesQuestionPopulator(allCountries, (CountriesCategoryEnum) question.getQuestionCategory());
        return new CountriesPopulationAreaNeighbGameService(question, allCountries, countriesQuestionCreator.getQuestions(), countriesQuestionCreator.getSynonyms());
    }

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return CountriesPopulationAreaNeighbGameService.class;
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new CountriesPressedLettersRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new CountriesPopulationAreaNeighbQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_PRESS_RANDOM_ANSWER;
    }
}
