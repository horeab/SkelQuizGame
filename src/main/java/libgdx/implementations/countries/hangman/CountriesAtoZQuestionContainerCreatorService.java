package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesAtoZQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesAtoZGameService> {

    List<String> startingLettersOfCountries;

    public CountriesAtoZQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesAtoZGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
        startingLettersOfCountries = gameService.getStartingLettersOfCountries();
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        super.processGameInfo(gameQuestionInfo);

    }

    private String getCountryForFirstLetter(String letter) {
        for (String country : gameService.getPossibleAnswers()) {
            if (country.substring(0, 1).toLowerCase().equals(letter.toLowerCase())) {
                return country;
            }
        }
        return "";
    }

    @Override
    public int getCounterSeconds() {
        return Math.round(startingLettersOfCountries.size() * 10f);
    }

    @Override
    public void fillCountriesTopTable(Table table) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int countryInfoWidth = 50;
        int i = 0;
        for (String letter : startingLettersOfCountries) {
            String countryForFirstLetter = getCountryForFirstLetter(letter);
            Table countryContainer = createCountryTopTable(getCountryToDisplay(letter), letter,
                    countryForFirstLetter.substring(countryForFirstLetter.length() - 1));
            float rowHeight = 3f;
            if (i < startingLettersOfCountries.size() / 2) {
                firstColumn.add(countryContainer)
                        .height(ScreenDimensionsManager.getScreenHeightValue(rowHeight))
                        .width(ScreenDimensionsManager.getScreenWidthValue(countryInfoWidth));
                firstColumn.row();
            } else {
                secondColumn.add(countryContainer)
                        .height(ScreenDimensionsManager.getScreenHeightValue(rowHeight))
                        .width(ScreenDimensionsManager.getScreenWidthValue(countryInfoWidth));
                secondColumn.row();
            }
            i++;
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }

    @Override
    public String getCorrectAnswerLevelFinished(int i) {
        for (String country : gameService.getPossibleAnswersRaw()) {
            if (country.toLowerCase().startsWith(startingLettersOfCountries.get(i).toLowerCase())) {
                return country;
            }
        }
        return "";
    }

    @Override
    public boolean allCountriesFound() {
        return startingLettersOfCountries.size() == foundCountries.size();
    }

    private String getCountryToDisplay(String firstLetter) {
        for (String country : foundCountries) {
            if (country.substring(0, 1).toLowerCase().equals(firstLetter.toLowerCase())) {
                return country;
            }
        }
        return " ";
    }
}
