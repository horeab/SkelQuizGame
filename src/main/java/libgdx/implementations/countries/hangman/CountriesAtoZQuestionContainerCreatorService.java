package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesHangmanGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesAtoZQuestionContainerCreatorService extends CountriesPressedLettersQuestionContainerCreatorService<CountriesAtoZGameService> {

    private static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";

    public CountriesAtoZQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen, (CountriesAtoZGameService) GameServiceContainer.getGameService(gameContext.getQuestion()));
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

    private void addCountriesToTable(Table table) {
        float fontSize = 0.9f;
        Table firstColumn = new Table();
        Table secondColumn = new Table();
        int countryInfoWidth = 50;
        List<String> startingLetters = gameService.getStartingLettersOfCountries();
        int i = 0;
        for (String letter : startingLetters) {
            MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(letter.toUpperCase())
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            float countryLabelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
            MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setWidth(countryLabelWidth)
                    .setText(getCountryForFirstLetter(letter))
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize * 1.3f)).build());
            Table countryContainer = new Table();
            boolean lastCountryFound = !foundCountries.isEmpty() && foundCountries.get(foundCountries.size() - 1).substring(0, 1).toLowerCase().equals(letter.toLowerCase());
            Res backgr = lastCountryFound ? MainResource.btn_menu_down : MainResource.btn_lowcolor_up;
            if (lastCountryFound) {
                new ActorAnimation(countryNameLabel, getAbstractGameScreen()).animateFastFadeIn();
            }
            countryContainer.setBackground(GraphicUtils.getNinePatch(backgr));
            countryContainer.add(topNr).width(ScreenDimensionsManager.getScreenWidthValue(10));
            countryContainer.add(countryNameLabel.fitToContainer()).width(countryLabelWidth);
            countryContainer.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
            float rowHeight = 3.7f;
            if (i <= startingLetters.size() / 2) {
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
