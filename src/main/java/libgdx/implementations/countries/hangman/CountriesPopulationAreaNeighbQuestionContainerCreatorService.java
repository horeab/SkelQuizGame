package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.utils.ScreenDimensionsManager;
import org.apache.commons.lang3.StringUtils;

public class CountriesPopulationAreaNeighbQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesPopulationAreaNeighbGameService> {

    public CountriesPopulationAreaNeighbQuestionContainerCreatorService(GameContext gameContext, CountriesGameScreen countriesGameScreen) {
        super(gameContext, countriesGameScreen, (CountriesPopulationAreaNeighbGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
    }

    @Override
    public int getCounterSeconds() {
        return Math.min(CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND, gameService.getPossibleAnswers().size()) * 10;
    }

//    @Override
//    public int getCounterSeconds() {
//        return 22;
//    }

    @Override
    public void fillCountriesTopTable(Table table) {
        if (morePossibleAnswersThanMax()) {
            containerWithMaxQuestions(table, CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND);
        } else {
            underMaxQuestions(table, gameService.getPossibleAnswers().size());
        }
    }

    private boolean morePossibleAnswersThanMax() {
        return gameService.getPossibleAnswers().size() > CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND;
    }

    private void underMaxQuestions(Table table, int nrOfQuestions) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        for (int i = 1; i <= nrOfQuestions; i++) {
            String countryName = gameService.getPossibleAnswers().get(i - 1);
            Table countryContainer = super.createCountryContainerInGame(foundCountries.contains(countryName) ? countryName : " ", i + "", " ", i);
            addToColumns(firstColumn, secondColumn, i, countryContainer, nrOfQuestions);
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }

    private void containerWithMaxQuestions(Table table, int nrOfQuestions) {
        super.fillCountriesTopTable(table);
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        for (int i = 1; i <= nrOfQuestions; i++) {
            String countryName = foundCountries.size() >= i && StringUtils.isNotBlank(foundCountries.get(i - 1)) ? foundCountries.get(i - 1) : " ";
            Table countryContainer = super.createCountryContainerInGame(countryName, i + "", " ", i);
            addToColumns(firstColumn, secondColumn, i, countryContainer, nrOfQuestions);
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }

    private void addToColumns(Table firstColumn, Table secondColumn, int i, Table countryContainer, int nrOfQuestions) {
        float rowHeight = 3.5f;
        float countryInfoWidth = getCountryContainerWidth();
        if (i <= Math.ceil((float) (nrOfQuestions / 2f)) || nrOfQuestions <= CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND / 2) {
            firstColumn.add(countryContainer)
                    .height(ScreenDimensionsManager.getScreenHeight(rowHeight))
                    .width(ScreenDimensionsManager.getScreenWidth(countryInfoWidth));
            firstColumn.row();
        } else {
            secondColumn.add(countryContainer)
                    .height(ScreenDimensionsManager.getScreenHeight(rowHeight))
                    .width(ScreenDimensionsManager.getScreenWidth(countryInfoWidth));
            secondColumn.row();
        }
    }
}
