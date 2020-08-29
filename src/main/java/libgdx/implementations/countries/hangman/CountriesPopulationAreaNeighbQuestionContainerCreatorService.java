package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesPopulationAreaNeighbQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesPopulationAreaNeighbGameService> {

    public CountriesPopulationAreaNeighbQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesPopulationAreaNeighbGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
    }

    @Override
    public int getCounterSeconds() {
        return gameService.getPossibleAnswers().size() * 10;
    }

    @Override
    public void fillCountriesTopTable(Table table) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int countryInfoWidth = 50;
        for (int i = 1; i <= gameService.getPossibleAnswers().size(); i++) {
            String countryName = gameService.getPossibleAnswers().get(i - 1);
            Table countryContainer = super.createCountryTopTable(foundCountries.contains(countryName) ? countryName : " ", i + "");
            float rowHeight = 3.7f;
            if (i <= CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND / 2) {
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
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }
}
