package libgdx.implementations.countries.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import libgdx.controls.button.MyButton;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.countries.CountriesQuestionCreator;
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

public class CountriesPopulationAreaNeighbCreatorDependencies extends CreatorDependencies {

    private CountriesQuestionCreator questionCreator = new CountriesQuestionCreator();
    private List<String> allCountries;

    public CountriesPopulationAreaNeighbCreatorDependencies() {
        allCountries = CountriesGame.getInstance().getSubGameDependencyManager().getAllCountries();
    }

    @Override
    public GameService getGameService(Question question) {
        HashMap<Integer, List<String>> q = new HashMap<>();
        if (question.getQuestionCategory() == CountriesCategoryEnum.cat3) {
            addNeighbQuestions(q, 2);
            addNeighbQuestions(q, 3);
            addNeighbQuestions(q, 4);
        } else {
            q.put(0, allCountries.subList(0, CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND));
        }
        return new CountriesPopulationAreaNeighbGameService(question, allCountries, q);
    }

    private void addNeighbQuestions(HashMap<Integer, List<String>> q, int countryLine) {
        List<String> neigbQuestions = getNeigbQuestions(countryLine);
        q.put(Integer.parseInt(neigbQuestions.get(0)), neigbQuestions.subList(1, neigbQuestions.size()));
    }

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return CountriesPopulationAreaNeighbGameService.class;
    }

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        CountriesCategoryEnum categoryEnum = CountriesCategoryEnum.cat3;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        int ind = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ind++;
        }
        return questions;
    }

    public List<String> getNeigbQuestions(int countryLine) {
        List<String> questions = new ArrayList<>();
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        CountriesCategoryEnum categoryEnum = CountriesCategoryEnum.cat3;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        int ind = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (ind == countryLine) {
                questions.add(line.split(":")[0]);
                questions.addAll(Arrays.asList(line.split(":")[1].split(",")));
            }
            ind++;
        }
        return questions;
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
