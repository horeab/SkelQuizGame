package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesAtoZQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesAtoZGameService> {

    public CountriesAtoZQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesAtoZGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        super.processGameInfo(gameQuestionInfo);

    }

    private String getCountryForFirstLetter(String letter) {
        for (String country : foundCountries) {
            if (country.substring(0, 1).toLowerCase().equals(letter.toLowerCase())) {
                return country;
            }
        }
        return "";
    }

    @Override
    public void fillCountriesTopTable(Table table) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int countryInfoWidth = 50;
        List<String> startingLetters = gameService.getStartingLettersOfCountries();
        int i = 0;
        for (String letter : startingLetters) {
            Table countryContainer = createCountryTopTable(getCountryForFirstLetter(letter), letter);
            float rowHeight = 3f;
            if (i < startingLetters.size() / 2) {
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
}
