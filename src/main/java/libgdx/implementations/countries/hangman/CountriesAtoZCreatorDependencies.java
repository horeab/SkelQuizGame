package libgdx.implementations.countries.hangman;

import java.util.HashMap;
import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDependencyManager;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.HintButtonType;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.RefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;

public class CountriesAtoZCreatorDependencies extends CreatorDependencies {

    private Map<Integer, CountriesAtoZGameService> gameService = new HashMap<>();

    @Override
    public GameService getGameService(Question question) {
        CountriesDependencyManager manager = CountriesGame.getInstance().getSubGameDependencyManager();
        int l = question.getQuestionLineInQuestionFile();
        if (gameService.get(l) == null) {
            gameService.put(l, new CountriesAtoZGameService(question, manager.getAllCountries(), manager.getCategQuestions((CountriesCategoryEnum) question.getQuestionCategory()), manager.getSynonyms()));
        }
        return gameService.get(l);
    }

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return CountriesAtoZGameService.class;
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new CountriesPressedLettersRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new CountriesAtoZQuestionContainerCreatorService(gameContext, (CountriesGameScreen) screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_PRESS_RANDOM_ANSWER;
    }
}
