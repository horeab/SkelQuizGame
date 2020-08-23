package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

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

    private MyWrappedLabel pressedLettersLabel;

    public CountriesHangmanQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(" ")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        if (correctAnswer) {
            gameQuestionInfo.getAnswers().clear();
        } else if (wrongAnwer) {
            gameQuestionInfo.getAnswers().clear();
        }
        pressedLettersLabel.setText(((CountriesHangmanGameService) GameServiceContainer.getGameService(gameQuestionInfo.getQuestion())).getPressedAnswers(new ArrayList<>(gameQuestionInfo.getAnswerIds())));
    }

    public MyWrappedLabel getPressedLettersLabel() {
        return pressedLettersLabel;
    }

    public Table createTopCountriesTable() {
        Table table = new Table();
        float fontSize = 0.9f;
        for (int i = 1; i <= CountriesHangmanGameScreen.TOP_COUNTRIES_TO_BE_FOUND; i++) {
            MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText("" + i)
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            MyWrappedLabel countryName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText("Russia")
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
            Table countryContainer = new Table();
            countryContainer.add(topNr).width(ScreenDimensionsManager.getScreenWidthValue(10));
            countryContainer.add(countryName).width(ScreenDimensionsManager.getScreenWidthValue(40));
            table.add(countryContainer).width(ScreenDimensionsManager.getScreenWidthValue(50));
            if (i % 2 == 0) {
                table.row();
            }
        }
        return table;
    }
}
