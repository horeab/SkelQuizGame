package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesPopulationAreaNeighbQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesPopulationAreaNeighbGameService> {

    private static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";

    public CountriesPopulationAreaNeighbQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesPopulationAreaNeighbGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
    }

    @Override
    public void refreshCountries() {
        super.refreshCountries();
        Table countriesTable = getAbstractGameScreen().getRoot().findActor(TOP_COUNTRIES_TABLE);
        countriesTable.clear();
        addCountriesToTable(countriesTable);
    }

    @Override
    public Table createTopCountriesTable() {
        Table table = new Table();
        table.setName(TOP_COUNTRIES_TABLE);
        addCountriesToTable(table);
        return table;
    }

    private void addCountriesToTable(Table table) {
        float fontSize = 0.9f;
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int countryInfoWidth = 50;
        for (int i = 1; i <= gameService.getPossibleAnswers().size(); i++) {
            MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText("" + i)
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            String countryName = gameService.getPossibleAnswers().get(i - 1);
            float countryLabelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
            MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setWidth(countryLabelWidth)
                    .setText(foundCountries.contains(countryName) ? countryName : "")
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize * 1.3f)).build());
            Table countryContainer = new Table();
            boolean lastCountryFound = !foundCountries.isEmpty() && foundCountries.get(foundCountries.size() - 1).equals(countryName);
            Res backgr = lastCountryFound ? MainResource.btn_menu_down : MainResource.btn_lowcolor_up;
            if (lastCountryFound) {
                new ActorAnimation(countryNameLabel, getAbstractGameScreen()).animateFastFadeIn();
            }
            countryContainer.setBackground(GraphicUtils.getNinePatch(backgr));
            countryContainer.add(topNr).width(ScreenDimensionsManager.getScreenWidthValue(10));
            countryContainer.add(countryNameLabel.fitToContainer()).width(countryLabelWidth);
            countryContainer.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
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
