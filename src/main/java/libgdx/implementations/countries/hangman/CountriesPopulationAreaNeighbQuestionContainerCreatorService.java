package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesPopulationAreaNeighbQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesPopulationAreaNeighbGameService> {

    public CountriesPopulationAreaNeighbQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesPopulationAreaNeighbGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
    }

//    @Override
//    public int getCounterSeconds() {
//        return gameService.getPossibleAnswers().size() * 10;
//    }

    @Override
    public int getCounterSeconds() {
        return 22;
    }

    @Override
    public void fillCountriesTopTable(Table table) {
        if (Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1, CountriesCategoryEnum.cat3).contains(gameContext.getQuestion().getQuestionCategory())) {
            cat_0_1_3_table(table);
        } else if (Arrays.asList(CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5).contains(gameContext.getQuestion().getQuestionCategory())) {
            cat_4_5_table(table);
        }
    }

    private void cat_0_1_3_table(Table table) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        for (int i = 1; i <= gameService.getPossibleAnswers().size(); i++) {
            String countryName = gameService.getPossibleAnswers().get(i - 1);
            Table countryContainer = super.createCountryContainerInGame(foundCountries.contains(countryName) ? countryName : " ", i + "", " ", i);
            addToColumns(firstColumn, secondColumn, i, countryContainer);
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }

    private void cat_4_5_table(Table table) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int nrOfQuestions = Math.min(gameService.getPossibleAnswers().size(), CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND);
        for (int i = 1; i <= nrOfQuestions; i++) {
            String countryName = foundCountries.size() >= i && StringUtils.isNotBlank(foundCountries.get(i - 1)) ? foundCountries.get(i - 1) : " ";
            Table countryContainer = super.createCountryContainerInGame(countryName, i + "", " ", i);
            addToColumns(firstColumn, secondColumn, i, countryContainer);
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }

    private void addToColumns(Table firstColumn, Table secondColumn, int i, Table countryContainer) {
        float rowHeight = 4f;
        float countryInfoWidth = getCountryContainerWidth();
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
}
