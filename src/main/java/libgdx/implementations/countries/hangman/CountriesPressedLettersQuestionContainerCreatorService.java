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
import java.util.Collections;
import java.util.HashMap;
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
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class CountriesPressedLettersQuestionContainerCreatorService<TGameService extends CountriesPressedLettersGameService> extends HangmanQuestionContainerCreatorService {

    private static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";
    private static final String ALL_GAME_VIEW = "ALL_GAME_VIEW";
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
        if (pressedAnswers.length() > 3
                || gameService.getPossibleAnswersLowerCase().contains(pressedAnswers)
                || gameService.getAllSynonymCountries().contains(pressedAnswers)) {
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
        table.setFillParent(true);
        table.setName(ALL_GAME_VIEW);
        addAllGameContainers(table);
        return table;
    }

    public Table createCountryContainerInGame(String countryName, String topText, String endText, int index) {
        Table countryContainer = createCountryContainer(countryName, topText, endText, index);
        boolean lastCountryFound = !foundCountries.isEmpty() && foundCountries.get(foundCountries.size() - 1).equals(countryName);
        final MainResource normalBackground = MainResource.btn_lowcolor_up;
        Res backgr = lastCountryFound ? MainResource.btn_menu_down : normalBackground;
        if (lastCountryFound) {
            new ActorAnimation(countryContainer.getChildren().get(1), getAbstractGameScreen()).animateFastFadeIn();
            RunnableAction ra = new RunnableAction();
            ra.setRunnable(new ScreenRunnable(getAbstractGameScreen()) {
                @Override
                public void executeOperations() {
                    countryContainer.setBackground(GraphicUtils.getNinePatch(normalBackground));
                }
            });
            getAbstractGameScreen().addAction(Actions.sequence(Actions.delay(1), ra));
        }
        countryContainer.setBackground(GraphicUtils.getNinePatch(backgr));
        return countryContainer;
    }

    private Table createCountryContainer(String countryName, String topText, String endText, int index) {
        float marginTableWidth = getMarginColumnWidth();
        float countryLabelWidth = getCountryLabelWidth();
        String toDisplay = StringUtils.isNotBlank(countryName) ? getSynonymNameIfLongOrCountry(countryName, index) : countryName;
        boolean dontShowEndColumn = dontShowEndColumn(toDisplay, endText);
        if (dontShowEndColumn) {
            countryLabelWidth = countryLabelWidth + marginTableWidth;
        }
        Table countryContainer = new Table();
        countriesTop.add(countryContainer);
        float fontScale = FontManager.calculateMultiplierStandardFontSize(getCountryContainerFontSize());
        MyWrappedLabel topNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(topText.toUpperCase())
                .setFontScale(fontScale / 1.2f).build());
        MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(countryLabelWidth)
                .setSingleLineLabel()
                .setText(StringUtils.capitalize(toDisplay))
                .setFontScale(fontScale).build());
        countryNameLabel.setFontScale(getCountryNameFontScale(toDisplay, countryNameLabel.getLabels().get(0).getFontScaleY()));
        countryNameLabel.setEllipsis(countryLabelWidth);
        countryContainer.add(topNr).width(marginTableWidth);
        countryContainer.add(countryNameLabel).width(countryLabelWidth);
        if (!dontShowEndColumn) {
            MyWrappedLabel endNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(endText)
                    .setFontScale(fontScale / 1.2f).build());
            countryContainer.add(endNr).width(marginTableWidth);
        }
        return countryContainer;
    }

    private float getMarginColumnWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(getCountryContainerWidth() / 5f);
    }

    private boolean dontShowEndColumn(String countryName, String endText) {
        return StringUtils.isBlank(endText) && countryName.length() > 13;
    }

    private float getCountryNameFontScale(String countryName, float fontScale) {
        int length = countryName.length();
        if (length > 30) {
            fontScale = fontScale / 1.4f;
        } else if (length > 25) {
            fontScale = fontScale / 1.35f;
        } else if (length > 20) {
            fontScale = fontScale / 1.30f;
        } else if (length > 15) {
            fontScale = fontScale / 1.25f;
        }
        return fontScale;
    }

    private float getCountryLabelWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(getCountryContainerWidth() / 1.5f);
    }

    public float getCountryContainerWidth() {
        return 50f;
    }

    public float getCountryContainerFontSize() {
        return 1f;
    }

    private void addAllGameContainers(Table table) {
        table.add(createHeaderTable()).height(ScreenDimensionsManager.getScreenHeightValue(10)).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(createTopCountriesTable()).height(ScreenDimensionsManager.getScreenHeightValue(40)).row();
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
        table.add(pressedLettersTable).height(ScreenDimensionsManager.getScreenHeightValue(10)).bottom().row();
        Table answerOptionsTable = createAnswerOptionsTable();
        table.add(answerOptionsTable).height(ScreenDimensionsManager.getScreenHeightValue(40)).bottom();
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
        } else if (allCountriesFound()) {
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
                    gameService = getGameService();
                    addAllGameContainers(allGameTable);
                }
            });
            gameScreen.addAction(Actions.sequence(Actions.delay(3), ra));
            gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswers().clear();
            pressedLettersLabel.setText("");
            foundCountries.clear();
            executorService.shutdown();
        }
    }

    public TGameService getGameService() {
        return null;
    }

    private boolean allCountriesFound() {
        return countriesTop.size() == foundCountries.size();
    }

    private void displayAllCountryNames() {
        int i = 1;
        for (Table table : countriesTop) {
            //the country text is displayed in the second label
            MyWrappedLabel countryLabel = (MyWrappedLabel) table.getChildren().get(1);
            MainResource backgr = getCorrectAnswBackgr();
            if (StringUtils.isBlank(countryLabel.getText())) {
                backgr = MainResource.btn_lowcolor_down;
                String countryName = getCorrectAnswerLevelFinished(i);
                String endText = table.getChildren().size > 2 ? ((MyWrappedLabel) table.getChildren().get(2)).getText() : "";
                boolean dontShowEndColumn = dontShowEndColumn(countryName, endText);
                countryLabel.setText(countryName);
                countryLabel.setFontScale(getCountryNameFontScale(countryName, countryLabel.getLabels().get(0).getFontScaleY()));
                countryLabel.setEllipsis(dontShowEndColumn ? countryLabel.getWidth() + getMarginColumnWidth() : countryLabel.getWidth());
            }
            table.setBackground(GraphicUtils.getNinePatch(backgr));
            i++;
        }
    }

    public String getCorrectAnswerLevelFinished(int i) {
        return getSynonymNameIfLongOrCountry(gameService.getPossibleAnswers().get(i - 1), i);
    }

    public String getSynonymNameIfLongOrCountry(String countryName, int index) {
        HashMap<Integer, List<String>> synonyms = gameService.getSynonyms();
        if (synonyms.isEmpty()) {
            return countryName;
        }
        List<String> syn = synonyms.getOrDefault(gameService.getAllCountries().indexOf(countryName) + 1, Collections.emptyList());
        for (String s : syn) {
            if (countryName.length() > 22 && s.length() < countryName.length() && s.length() > 4) {
                countryName = s;
            }
        }
        return countryName;
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
