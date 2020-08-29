package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class CountriesPressedLettersQuestionContainerCreatorService<TGameService extends CountriesPressedLettersGameService> extends HangmanQuestionContainerCreatorService {

    private static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";
    private static final String ALL_GAME_VIEW = "ALL_GAME_VIEW";
    public static final String COUNTRY_NAME_LABEL = "countryNameLabel";
    private MyWrappedLabel pressedLettersLabel;
    List<String> foundCountries = new ArrayList<>();
    private MutableLong countdownAmountMillis;
    private MyWrappedLabel counterLabel;
    TGameService gameService;
    List<Table> countriesTop = new ArrayList<>();
    ScheduledExecutorService executorService;
    MyButton clearPressedBtn;

    public CountriesPressedLettersQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen, TGameService gameService) {
        super(gameContext, abstractGameScreen);
        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(" ")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.7f)).build());
        this.gameService = gameService;
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        String pressedAnswers = gameService.getPressedAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()));
        pressedLettersLabel.setText(pressedAnswers);
        clearPressedBtn.setVisible(true);
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
                clearPressedBtn.setVisible(false);
                gameQuestionInfo.getAnswers().clear();
                refreshCountries();
            } else if (pressedCorrectAnswers.isEmpty()) {
                new ActorAnimation(getAbstractGameScreen()).animatePulse();
                pressedLettersLabel.setText("");
                clearPressedBtn.setVisible(false);
                gameQuestionInfo.getAnswers().clear();
            }
        }
        isQuestionOver();
    }


    public void refreshCountries() {
        Table countriesTable = getAbstractGameScreen().getRoot().findActor(TOP_COUNTRIES_TABLE);
        countriesTable.clear();
        fillCountriesTopTable(countriesTable);
    }

    public Table createTopCountriesTable() {
        Table table = new Table();
        table.setName(TOP_COUNTRIES_TABLE);
        fillCountriesTopTable(table);
        return table;
    }

    public int getCounterSeconds() {
        return 180;
    }

    public MyWrappedLabel getPressedLettersLabel() {
        return pressedLettersLabel;
    }

    public Table createAllGameView() {
        Table table = new Table();
        table.setName(ALL_GAME_VIEW);
        addAllGameContainers(table);
        return table;
    }

    public Table createCountryTopTable(String countryName, String topText) {
        float fontSize = 0.9f;
        Table countryContainer = new Table();
        countriesTop.add(countryContainer);
        MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(topText.toUpperCase())
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
        float countryLabelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
        MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(countryLabelWidth)
                .setText(countryName)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(fontSize)).build());
        countryNameLabel.setName(COUNTRY_NAME_LABEL);
        boolean lastCountryFound = !foundCountries.isEmpty() && foundCountries.get(foundCountries.size() - 1).equals(countryName);
        Res backgr = lastCountryFound ? MainResource.btn_menu_down : MainResource.btn_lowcolor_up;
        if (lastCountryFound) {
            new ActorAnimation(countryNameLabel, getAbstractGameScreen()).animateFastFadeIn();
        }
        countryContainer.setBackground(GraphicUtils.getNinePatch(backgr));
        countryContainer.add(topNr).width(ScreenDimensionsManager.getScreenWidthValue(10));
        countryContainer.add(countryNameLabel.fitToContainer()).width(countryLabelWidth);
        countryContainer.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        return countryContainer;
    }

    private void addAllGameContainers(Table table) {
        float margins = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeaderTable()).padBottom(margins / 1.1f).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(createTopCountriesTable()).padBottom(margins).row();
        Table pressedLettersTable = new Table();
        pressedLettersTable.add(getPressedLettersLabel());
        FontConfig fontConfig = new FontConfig(Color.RED);
        clearPressedBtn = new ButtonBuilder().setFontConfig(fontConfig).setText("X").build();
        clearPressedBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswers().clear();
                pressedLettersLabel.setText("");
                clearPressedBtn.setVisible(false);
            }
        });
        pressedLettersTable.add(clearPressedBtn);
        clearPressedBtn.setVisible(false);
        table.add(pressedLettersTable).padBottom(margins * 1.5f).row();
        Table answerOptionsTable = createAnswerOptionsTable();
        table.add(answerOptionsTable).growY();
    }

    public Table createHeaderTable() {
        Table table = new Table();

        MyWrappedLabel categLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getCategText())
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

    String getCategText() {
        String categText = "";
        if (gameContext.getQuestion().getQuestionCategory() == CountriesCategoryEnum.cat0) {
            categText = "Populous";
        } else if (gameContext.getQuestion().getQuestionCategory() == CountriesCategoryEnum.cat3) {
            categText = gameService.getCountryName(gameService.getQuestionIndex());
        }
        return categText;
    }

    private void countdownProcess() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        countdownAmountMillis = new MutableLong(getCounterSeconds() * 1000);
        final int period = 100;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractGameScreen()) {
            @Override
            public void executeOperations() {
                if (countdownAmountMillis.getValue() <= 0) {
                    executorService.shutdown();
                    isQuestionOver();
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

    private void isQuestionOver() {
        boolean qOver = false;
        if (countdownAmountMillis.getValue() <= 0) {
            qOver = true;
            displayAllCountryNames();
        } else if (gameService.getPossibleAnswers().size() == foundCountries.size()) {
            qOver = true;
            greenBackgroundCountries();
        }
        if (qOver) {
            AbstractScreen gameScreen = getAbstractGameScreen();
            RunnableAction ra = new RunnableAction();
            ra.setRunnable(new ScreenRunnable(gameScreen) {
                @Override
                public void executeOperations() {
                    Table allGameTable = gameScreen.getRoot().findActor(ALL_GAME_VIEW);
                    allGameTable.clear();
                    gameService = (TGameService) new CountriesPopulationAreaNeighbGameService(gameContext.getQuestion(), gameService.getAllCountries(), gameService.getQuestionEntries());
                    addAllGameContainers(allGameTable);
                }
            });
            gameScreen.addAction(Actions.sequence(Actions.delay(2f), ra));
            gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswers().clear();
            pressedLettersLabel.setText("");
            foundCountries.clear();
            executorService.shutdown();
        }
    }

    private void displayAllCountryNames() {
        int i = 0;
        for (Table table : countriesTop) {
            MyWrappedLabel label = table.findActor(COUNTRY_NAME_LABEL);
            MainResource backgr = getCorrectAnswBackgr();
            if (StringUtils.isBlank(label.getText())) {
                backgr = MainResource.btn_lowcolor_down;
                label.setText(gameService.getPossibleAnswers().get(i));
            }
            table.setBackground(GraphicUtils.getNinePatch(backgr));
            i++;
        }
    }

    private void greenBackgroundCountries() {
        for (Table table : countriesTop) {
            table.setBackground(GraphicUtils.getNinePatch(getCorrectAnswBackgr()));
        }
    }

    private MainResource getCorrectAnswBackgr() {
        return MainResource.btn_menu_up;
    }

    public void fillCountriesTopTable(Table table) {
        countriesTop.clear();
    }
}
