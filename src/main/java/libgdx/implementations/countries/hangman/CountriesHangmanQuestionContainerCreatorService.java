package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesHangmanGameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesHangmanQuestionContainerCreatorService extends HangmanQuestionContainerCreatorService {

    public static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";
    private CountriesHangmanGameService gameService;
    private MyWrappedLabel pressedLettersLabel;
    private List<String> foundCountries = new ArrayList<>();

    public CountriesHangmanQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        gameService = (CountriesHangmanGameService) GameServiceContainer.getGameService(gameContext.getQuestion());
        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(" ")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
//        if (correctAnswer) {
//            gameQuestionInfo.getAnswers().clear();
//        } else if (wrongAnwer) {
//            gameQuestionInfo.getAnswers().clear();
//        }

        String pressedAnswers = gameService.getPressedAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()));
        pressedLettersLabel.setText(pressedAnswers);
        if (pressedAnswers.length() > 2) {
            List<String> pressedCorrectAnswers = gameService.getPressedCorrectAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()));
            if (pressedCorrectAnswers.size() == 1) {
                foundCountries.add(pressedCorrectAnswers.get(0));
                pressedLettersLabel.setText("");
                gameQuestionInfo.getAnswers().clear();
                Table countriesTable = getAbstractGameScreen().getRoot().findActor(TOP_COUNTRIES_TABLE);
                countriesTable.clear();
                addCountriesToTable(countriesTable);
            } else if (pressedCorrectAnswers.isEmpty()) {
                pressedLettersLabel.setText("");
                gameQuestionInfo.getAnswers().clear();
            }
        }
    }

    public MyWrappedLabel getPressedLettersLabel() {
        return pressedLettersLabel;
    }

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
        for (int i = 1; i <= CountriesHangmanGameScreen.TOP_COUNTRIES_TO_BE_FOUND; i++) {
            MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText("" + i)
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            String countryName = gameService.getPossibleAnswers().get(i - 1);
            MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(foundCountries.contains(countryName) ? countryName : "")
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            Table countryContainer = new Table();
            countryContainer.add(topNr).width(ScreenDimensionsManager.getScreenWidthValue(10));
            countryContainer.add(countryNameLabel).width(ScreenDimensionsManager.getScreenWidthValue(40));
            if (i <= CountriesHangmanGameScreen.TOP_COUNTRIES_TO_BE_FOUND / 2) {
                firstColumn.add(countryContainer).width(ScreenDimensionsManager.getScreenWidthValue(countryInfoWidth));
                firstColumn.row();
            } else {
                secondColumn.add(countryContainer).width(ScreenDimensionsManager.getScreenWidthValue(countryInfoWidth));
                secondColumn.row();
            }
        }
        table.add(firstColumn);
        table.add(secondColumn);
    }
}
