package libgdx.implementations.countries.hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.controls.button.MyButton;
import libgdx.game.Game;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.countries.CountriesQuestionCreator;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesHangmanGameScreen;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.HintButtonType;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.RefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;

public class CountriesHangmanCreatorDependencies extends CreatorDependencies {

    private CountriesQuestionCreator questionCreator = new CountriesQuestionCreator();
    private List<String> allCountries;

    public CountriesHangmanCreatorDependencies() {
        allCountries = CountriesGame.getInstance().getSubGameDependencyManager().getAllCountries();
    }

    @Override
    public GameService getGameService(Question question) {
        return new CountriesHangmanGameService(question, allCountries.subList(0, CountriesHangmanGameScreen.TOP_COUNTRIES_TO_BE_FOUND));
    }

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return CountriesHangmanGameService.class;
    }


    public List<Question> getPopulationQuestions() {
        List<Question> questions = new ArrayList<>();
        CampaignGameDependencyManager subGameDependencyManager = (CampaignGameDependencyManager) Game.getInstance().getSubGameDependencyManager();
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        CountriesCategoryEnum categoryEnum = CountriesCategoryEnum.cat0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        int ind = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String country = allCountries.get(ind);
            questions.add(new Question(ind, difficultyLevelToCreate, categoryEnum, "Population " + country + " " + line));
            ind++;
        }
        return questions;
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new CountriesHangmanRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new CountriesHangmanQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_PRESS_RANDOM_ANSWER;
    }
}
