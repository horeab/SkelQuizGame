package libgdx.implementations.countries.hangman;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesSpecificResource;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;
import libgdx.implementations.screens.implementations.countries.CountriesSettingsService;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountriesPressedLettersQuestionContainerCreatorService<TGameService extends CountriesPressedLettersGameService> extends HangmanQuestionContainerCreatorService {

    private static final String TOP_COUNTRIES_TABLE = "TopCountriesTable";
    private static final String ALL_GAME_VIEW = "ALL_GAME_VIEW";
    private MyWrappedLabel pressedLettersLabel;
    List<String> foundCountries = new ArrayList<>();
    boolean questionOver = false;
    private MutableLong countdownAmountMillis;
    private MyWrappedLabel counterLabel;
    private MyWrappedLabel scoreLabel;
    TGameService gameService;
    List<Table> countriesTop = new ArrayList<>();
    ScheduledExecutorService executorService;
    MyButton clearPressedBtn;
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    int score = 0;
    int currentQuestion = 0;
    CountriesGameScreen countriesGameScreen;

    public CountriesPressedLettersQuestionContainerCreatorService(GameContext gameContext, CountriesGameScreen countriesGameScreen, TGameService gameService) {
        super(gameContext, countriesGameScreen);
        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(" ")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.7f)).build());
        this.gameService = gameService;
        this.countriesGameScreen = countriesGameScreen;
    }

    public CountriesGameScreen getCountriesGameScreen() {
        return countriesGameScreen;
    }

    @Override
    public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
        String pressedAnswers = gameService.getPressedAnswers(new ArrayList<>(gameQuestionInfo.getAnswers()));
        pressedLettersLabel.setText(pressedAnswers);
        clearPressedBtn.setVisible(true);
        clearPressedBtn.setButtonSkin(GameButtonSkin.COUNTRIES_CLEAR_LETTERS);
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
                clearPressedLetters();
                refreshCountries();
            } else if (pressedCorrectAnswers.isEmpty()) {
                new ActorAnimation(getAbstractGameScreen()).animatePulse();
                SoundUtils.playSound(CountriesSpecificResource.level_fail);
                clearPressedLetters();
            }
        }
        isQuestionOver();
    }

    private void clearPressedLetters() {
        gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswers().clear();
        pressedLettersLabel.setText("");
        if (gameService.getQuestionEntries().size() == 1) {
            clearPressedBtn.setVisible(false);
        }
        clearPressedBtn.setButtonSkin(GameButtonSkin.COUNTRIES_NEXTLEVEL);
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
            updateScore();
            new ActorAnimation(getAbstractGameScreen()).animateFastFadeIn(countryContainer.getChildren().get(1));
            RunnableAction ra = new RunnableAction();
            ra.setRunnable(new ScreenRunnable(getAbstractGameScreen()) {
                @Override
                public void executeOperations() {
                    countryContainer.setBackground(GraphicUtils.getNinePatch(questionOver ? getCorrectAnswBackgr() : normalBackground));
                }
            });
            getAbstractGameScreen().addAction(Actions.sequence(Actions.delay(1), ra));
        }
        countryContainer.setBackground(GraphicUtils.getNinePatch(backgr));
        return countryContainer;
    }

    private void updateScore() {
        score = score + 1;
        SoundUtils.playSound(CountriesSpecificResource.level_success);
        if (score > campaignStoreService.getScoreWon(countriesGameScreen.getCampaignLevel())) {
            campaignStoreService.updateScoreWon(countriesGameScreen.getCampaignLevel(), score);
            new CountriesSettingsService().setHighScore(true);
        }
        float scaleFactor = 1f;
        float duration = 0.2f;
        scoreLabel.addAction(Actions.sequence(Actions.scaleBy(scaleFactor, scaleFactor, duration),
                Actions.scaleBy(-scaleFactor, -scaleFactor, duration)));
        scoreLabel.setText("+" + score);
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
                .setFontScale(fontScale / getTopEndFontSize()).build());
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
                    .setFontScale(fontScale / getTopEndFontSize()).build());
            countryContainer.add(endNr).width(marginTableWidth);
        }
        return countryContainer;
    }

    private float getMarginColumnWidth() {
        return ScreenDimensionsManager.getScreenWidth(getCountryContainerWidth() / 5f);
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
        return ScreenDimensionsManager.getScreenWidth(getCountryContainerWidth() / 1.5f);
    }

    public float getCountryContainerWidth() {
        return 50f;
    }

    public float getCountryContainerFontSize() {
        return 1f;
    }

    public float getTopEndFontSize() {
        return 1.2f;
    }

    private void addAllGameContainers(Table table) {
        table.add(createHeaderTable()).height(ScreenDimensionsManager.getScreenHeight(15)).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(createTopCountriesTable()).height(ScreenDimensionsManager.getScreenHeight(35)).row();
        Table pressedLettersTable = new Table();
        pressedLettersTable.add(getPressedLettersLabel());
        FontConfig fontConfig = new FontConfig(Color.RED);
        clearPressedBtn = new ButtonBuilder()
                .setFixedButtonSize(GameButtonSize.COUNTRIES_CLEAR_LETTERS_BUTTON)
                .setButtonSkin(GameButtonSkin.COUNTRIES_NEXTLEVEL)
                .setFontConfig(fontConfig).build();
        if (gameService.getQuestionEntries().size() == 1) {
            clearPressedBtn.setVisible(false);
        }
        clearPressedBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswers().size() == 0) {
                    new ActorAnimation(getAbstractGameScreen()).animatePulse();
                    clearPressedBtn.setDisabled(true);
                    goToNextLevel(1);
                } else {
                    clearPressedLetters();
                }
            }
        });
        pressedLettersTable.add(clearPressedBtn).width(clearPressedBtn.getWidth()).height(clearPressedBtn.getHeight());
        table.add(pressedLettersTable).height(ScreenDimensionsManager.getScreenHeight(10)).bottom().row();
        Table answerOptionsTable = createAnswerOptionsTable();
        table.add(answerOptionsTable).height(ScreenDimensionsManager.getScreenHeight(40)).bottom();
    }

    public Table createHeaderTable() {
        String questionName = getQuestionName();
        Table table = new Table();
        Table categContainer = new Table();
        String categText = StringUtils.isNotBlank(questionName) ? getCategText() + ": " : getCategText();
        float borderWidth = 3.1f;
        float categFontScale = 1.05f;
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        float categTextWidth = ScreenDimensionsManager.getScreenWidth(60);
        MyWrappedLabel categLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(categTextWidth - dimen * 2)
                .setText(StringUtils.capitalize(categText))
                .setFontConfig(new FontConfig(
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * categFontScale)).build());
        MyWrappedLabel actualQName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(StringUtils.capitalize(questionName))
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_BLUE.getColor(),
                        FontConfig.FONT_SIZE * categFontScale * 1.1f)).build());
        categContainer.add(categLabel).row();
        categContainer.add(actualQName);
        Table infoContainer = new Table();
        counterLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("1")
                .setFontConfig(new FontConfig(
                        FontColor.ORANGE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1.5f,
                        borderWidth)).build());
        counterLabel.setBackground(GraphicUtils.getNinePatchIdentical(CountriesSpecificResource.clock));
        scoreLabel = createScoreLabel(borderWidth, 2f);
        Stack animateScoreStack = new Stack();
        animateScoreStack.add(scoreLabel);
        infoContainer.add(animateScoreStack).width(ScreenDimensionsManager.getScreenWidth(20));
        infoContainer.add(categContainer).padLeft(dimen).padRight(dimen).width(categTextWidth);
        float counterSide = ScreenDimensionsManager.getScreenWidth(12);
        infoContainer.add(counterLabel).width(counterSide).height(counterSide);
        table.add(infoContainer).expandX().row();
        countdownProcess();
        return table;
    }

    private MyWrappedLabel createScoreLabel(float borderWidth, float fontScale) {
        String prefix = score > 0 ? "+" : "";
        MyWrappedLabel label = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(prefix + score)
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * fontScale,
                        borderWidth)).build());
        label.setTransform(true);
        return label;
    }

    private String getQuestionName() {
        QuizQuestionCategory questionCategory = gameContext.getQuestion().getQuestionCategory();
        String suffix = "";
        String gameIdPrefix = Game.getInstance().getAppInfoService().getGameIdPrefix();
        String language = Game.getInstance().getAppInfoService().getLanguage();
        if (questionCategory == CountriesCategoryEnum.cat4) {
            suffix = SpecificPropertiesUtils.getText(language + "_" + gameIdPrefix + "_emp_" + gameService.getMapQuestionIndex());
        } else if (questionCategory == CountriesCategoryEnum.cat5) {
            suffix = SpecificPropertiesUtils.getText(language + "_" + gameIdPrefix + "_geo_" + gameService.getMapQuestionIndex());
        } else if (questionCategory == CountriesCategoryEnum.cat3) {
            suffix = gameService.getAllCountries().get(gameService.getMapQuestionIndex() - 1);
        }
        return suffix;
    }

    String getCategText() {
        return SpecificPropertiesUtils.getQuestionCategoryLabel(gameContext.getQuestion().getQuestionCategory().getIndex());
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
        if (countdownAmountMillis.getValue() <= 0) {
            questionOver = true;
            new ActorAnimation(getAbstractGameScreen()).animatePulse();
            displayAllCountryNames();
        } else if (allCountriesFound()) {
            questionOver = true;
            greenBackgroundCountries();
        }
        if (questionOver) {
            goToNextLevel(3);
        }
    }

    private void goToNextLevel(int delay) {
        executorService.shutdown();
        AbstractScreen gameScreen = getAbstractGameScreen();
        RunnableAction ra = new RunnableAction();
        ra.setRunnable(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                clearQuestionInfo();
                Table allGameTable = gameScreen.getRoot().findActor(ALL_GAME_VIEW);
                allGameTable.clear();
                currentQuestion++;
                if (currentQuestion > gameService.getQuestionEntries().size() - 1) {
                    Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                        @Override
                        public void run() {
                            getAbstractGameScreen().getScreenManager().showMainScreen();
                        }
                    });
                } else {
                    gameService.goToQuestion(currentQuestion);
                    addAllGameContainers(allGameTable);
                }
            }
        });
        gameScreen.addAction(Actions.sequence(Actions.delay(delay), ra));
    }

    private void clearQuestionInfo() {
        questionOver = false;
        clearPressedLetters();
        foundCountries.clear();
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
        Map<Integer, List<String>> synonyms = gameService.getSynonyms();
        if (synonyms.isEmpty()) {
            return countryName;
        }
        List<String> syn = new ArrayList<>();
        if (synonyms.containsKey(gameService.getAllCountries().indexOf(countryName) + 1)) {
            syn.addAll(synonyms.get(gameService.getAllCountries().indexOf(countryName) + 1));
        }
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
