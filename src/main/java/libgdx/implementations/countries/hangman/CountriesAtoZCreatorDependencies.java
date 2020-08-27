package libgdx.implementations.countries.hangman;

import java.util.List;
import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.countries.CountriesQuestionCreator;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesHangmanGameScreen;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.HintButtonType;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.RefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;

public class CountriesAtoZCreatorDependencies extends CreatorDependencies {

    private CountriesQuestionCreator questionCreator = new CountriesQuestionCreator();
    private List<String> allCountries;

    public CountriesAtoZCreatorDependencies() {
        allCountries = CountriesGame.getInstance().getSubGameDependencyManager().getAllCountries();
    }

    @Override
    public GameService getGameService(Question question) {
        return new CountriesAtoZGameService(question, allCountries.subList(0, CountriesHangmanGameScreen.TOP_COUNTRIES_TO_BE_FOUND));
    }

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return CountriesAtoZGameService.class;
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new CountriesHangmanRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new CountriesAtoZQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_PRESS_RANDOM_ANSWER;
    }
}
