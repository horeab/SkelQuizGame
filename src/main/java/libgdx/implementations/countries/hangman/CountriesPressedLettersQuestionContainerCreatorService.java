package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class CountriesPressedLettersQuestionContainerCreatorService<TGameService extends CountriesPressedLettersGameService> extends HangmanQuestionContainerCreatorService {

    private static final int SECONDS = 180;
    private static final String COUNTER_LABEL = "COUNTER_LABEL";
    private MyWrappedLabel pressedLettersLabel;
    List<String> foundCountries = new ArrayList<>();
    private MutableLong countdownAmountMillis;
    private ScheduledExecutorService executorService;
    private MyWrappedLabel counterLabel;
    TGameService gameService;

    public CountriesPressedLettersQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen, TGameService gameService) {
        super(gameContext, abstractGameScreen);
        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(" ")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.7f)).build());
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.gameService = gameService;
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        String pressedAnswers = gameService.getPressedAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()));
        pressedLettersLabel.setText(pressedAnswers);
        if (pressedAnswers.length() > 3 || gameService.getPossibleAnswersLowerCase().contains(pressedAnswers)) {
            List<String> pressedCorrectAnswers = new ArrayList<>();
            if (gameService.getPossibleAnswersLowerCase().contains(pressedAnswers)) {
                pressedCorrectAnswers.add(StringUtils.capitalize(pressedAnswers));
            } else {
                pressedCorrectAnswers = gameService.getPressedCorrectAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()), foundCountries);
            }
            if (pressedCorrectAnswers.size() == 1) {
                foundCountries.add(pressedCorrectAnswers.get(0));
                pressedLettersLabel.setText("");
                gameQuestionInfo.getAnswers().clear();
                refreshCountries();
            } else if (pressedCorrectAnswers.isEmpty()) {
                new ActorAnimation(getAbstractGameScreen()).animatePulse();
                pressedLettersLabel.setText("");
                gameQuestionInfo.getAnswers().clear();
            }
        }
    }

    public void refreshCountries() {
    }

    public Table createTopCountriesTable() {
        return new Table();
    }

    public MyWrappedLabel getPressedLettersLabel() {
        return pressedLettersLabel;
    }

    public Table createHeaderTable() {
        Table table = new Table();
        String categText = "Which are the 20 most populous countries";
        if (gameContext.getQuestion().getQuestionCategory() == CountriesCategoryEnum.cat3) {
            categText = gameService.getCountryName(gameService.getPossAnswersLine());
        }
        MyWrappedLabel categLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(categText)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.1f)).build());
        table.add(categLabel).width(ScreenDimensionsManager.getScreenWidthValue(60));
        counterLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("1")
                .setFontColor(FontColor.RED)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.8f)).build());
        table.add(counterLabel).width(ScreenDimensionsManager.getScreenWidthValue(40));
        countdownProcess();
        return table;
    }

    private void countdownProcess() {
        countdownAmountMillis = new MutableLong(SECONDS * 1000);
        final int period = 100;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractGameScreen()) {
            @Override
            public void executeOperations() {
                if (countdownAmountMillis.getValue() <= 0) {
                    executorService.shutdown();
                }
                counterLabel.setText(countdownAmountMillis.getValue() <= 0 ? "0" : String.valueOf(countdownAmountMillis.intValue() / 1000));
                countdownAmountMillis.subtract(period);
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }
}
