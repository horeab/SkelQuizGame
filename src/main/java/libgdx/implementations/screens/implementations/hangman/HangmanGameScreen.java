package libgdx.implementations.screens.implementations.hangman;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.constants.Contrast;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanPreferencesService;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.history.HistoryCampaignScreen;
import libgdx.implementations.screens.implementations.history.HistoryScreenManager;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.gameservice.HangmanGameService;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.HangmanRefreshQuestionDisplayService;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class HangmanGameScreen extends GameScreen<HangmanScreenManager> {

    public static int TOTAL_QUESTIONS = 15;

    private CampaignLevel campaignLevel;
    private Table allTable;
    private HangmanQuestionContainerCreatorService hangmanQuestionContainerCreatorService;
    private int hintCategAvailable = 4;
    private Table categHintTable;
    private Table hanganAnswersTable;
    private List<MyButton> categHintBtnList = new ArrayList<>();

    public HangmanGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }
        System.out.println(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionDifficultyLevel());
        System.out.println(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionString());
        allTable = new Table();
        allTable.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table headerTable = new Table();
        Table wordTable = new Table();
        wordTable.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        allTable.add(createAnswerTable(0)).row();
        allTable.add(wordTable).growY().row();
        allTable.add(headerTable).padTop(verticalGeneralMarginDimen).row();
        allTable.add(createLevelsTable()).padTop(verticalGeneralMarginDimen).row();
        Table squareAnswerOptionsTable = new Table();
        allTable.add(squareAnswerOptionsTable)
                .growY();
        addActor(allTable);
        this.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, this) {

            @Override
            protected void answerClick(String answer) {
                int beforeLostG = gameContext.getCurrentUserGameUser().getLostQuestions();
                super.answerClick(answer);
                int afterLostG = gameContext.getCurrentUserGameUser().getLostQuestions();
                if (gameContext.getCurrentUserGameUser().getWonQuestions() < TOTAL_QUESTIONS) {
                    int afterNrOfWrongAnswersPressed = GameServiceContainer.getGameService(gameContext.getCurrentUserGameUser().getGameQuestionInfo()).getNrOfWrongAnswersPressed(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswerIds());
                    if (afterNrOfWrongAnswersPressed != 0) {
                        resetHangmanAnswersTable(afterNrOfWrongAnswersPressed);
                    } else if (beforeLostG != afterLostG) {
                        resetHangmanAnswersTable(HangmanGameService.GAME_OVER_WRONG_LETTERS);
                    }
                } else {
                    new HangmanPreferencesService().setHighScore(gameContext.getCurrentUserGameUser().getWonQuestions());
                    processGameOver();
                }
            }

        };
        headerTable.add(new HangmanHeaderCreator().createHeaderTable(gameContext, hangmanQuestionContainerCreatorService.createHintButtonsTable()));
        squareAnswerOptionsTable.add(new HangmanQuestionContainerCreatorService(gameContext, this).createSquareAnswerOptionsTable(new ArrayList<>(hangmanQuestionContainerCreatorService.getAllAnswerButtons().values())));
        hangmanQuestionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());

        for (int i = 0; i < hintCategAvailable; i++) {
            addCategHintBtn(i);
        }
        new BackButtonBuilder().addHoverBackButton(this);
        resetHangmanAnswersTable(0);
    }

    private void resetHangmanAnswersTable(int nrOfWrongAnswersPressed) {
        if (hanganAnswersTable != null) {
            hanganAnswersTable.clearChildren();
            createAnswerTable(nrOfWrongAnswersPressed);
        }
    }

    private Table createAnswerTable(int nrOfWrongAnswersPressed) {
        if (hanganAnswersTable == null) {
            hanganAnswersTable = new Table();
        }
        int allWidth = ScreenDimensionsManager.getScreenWidth();
        int gameOverWrongLetters = HangmanGameService.GAME_OVER_WRONG_LETTERS;
        float partWidth = allWidth / gameOverWrongLetters;
        hanganAnswersTable.setWidth(allWidth);
        float heightValue = ScreenDimensionsManager.getScreenHeightValue(2f);
        hanganAnswersTable.setHeight(heightValue);
        for (int i = gameOverWrongLetters; i >= 1; i--) {
            Table aTable = new Table();
            Color color = i > nrOfWrongAnswersPressed ? Color.GREEN : Color.RED;
            aTable.setBackground(GraphicUtils.getColorBackground(color));
            hanganAnswersTable.add(aTable).width(partWidth).height(heightValue);
        }
        return hanganAnswersTable;
    }

    private void addCategHintBtn(int i) {
        float x1 = ScreenDimensionsManager.getScreenWidthValue(15);
        float x2 = ScreenDimensionsManager.getScreenWidthValue(85);
        float y1 = ScreenDimensionsManager.getScreenHeightValue(46);
        float y2 = ScreenDimensionsManager.getScreenHeightValue(58);

        float x = i % 2 == 0 ? x1 : x2;
        float y = i == 0 || i == 1 ? y1 : y2;

        MyButton categHintBtn = new ImageButtonBuilder(GameButtonSkin.HANGMAN_HINT2, getAbstractScreen())
                .setFixedButtonSize(MainButtonSize.STANDARD_IMAGE)
                .build();
        categHintBtnList.add(categHintBtn);
        categHintBtn.setTransform(true);
        categHintBtn.setX(x - categHintBtn.getWidth() / 2);
        categHintBtn.setY(y);
        categHintBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                categHintBtn.addAction(Actions.fadeOut(0.2f));
                categHintBtn.setDisabled(true);
                if (categHintTable != null) {
                    categHintTable.remove();
                }
                categHintTable = createCategHintTable();
                categHintTable.setX(0);
                categHintTable.setY(ScreenDimensionsManager.getScreenHeightValue(70));
                addActor(categHintTable);
                hintCategAvailable = hintCategAvailable - 1;
            }
        });
        new ActorAnimation(categHintBtn, getAbstractScreen()).animateZoomInZoomOut();
        addActor(categHintBtn);
    }

    public static float getHangmanImgWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(55);
    }

    public static float getHangmanImgHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(35);
    }

    public Table createLevelsTable() {
        Table table = new Table();
        float lvlHeight = ScreenDimensionsManager.getScreenHeightValue(1.5f);
        float margin = MainDimen.vertical_general_margin.getDimen() / 3;
        int percent = 70;
        int r = 255;
        for (int i = TOTAL_QUESTIONS; i >= 1; i--) {
            Table lvlTable = new Table();
            Color color = gameContext.getCurrentUserGameUser().getWonQuestions() >= i ? Color.GREEN : new RGBColor(r, 255, 230).toColor();
            lvlTable.setBackground(GraphicUtils.getColorBackground(color));
            table.add(lvlTable).height(lvlHeight).width(ScreenDimensionsManager.getScreenWidthValue(percent)).padTop(margin).row();
            percent = percent - 4;
            r = r - 10;
        }
        return table;
    }

    private Table createCategHintTable() {
        Table table = new Table();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(
                new SpecificPropertiesUtils().getQuestionCategoryLabel(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionCategory().getIndex())).setFontConfig(new FontConfig(
                FontColor.BLACK.getColor(),
                FontConfig.FONT_SIZE * 1.7f)).setWrappedLineLabel(ScreenDimensionsManager.getScreenWidthValue(90)).build());
        table.add(titleLabel);
        table.addAction(Actions.sequence(Actions.delay(15f), Actions.fadeOut(1f)));
        table.setWidth(ScreenDimensionsManager.getScreenWidth());
        table.setHeight(ScreenDimensionsManager.getScreenHeightValue(5));
        table.setBackground(GraphicUtils.getColorBackground(RGBColor.LIGHT_BLUE.toColor(0.7f)));
        return table;
    }

    public void goToNextQuestionScreen() {
        new HangmanPreferencesService().setHighScore(gameContext.getCurrentUserGameUser().getWonQuestions());
        showPopupAd(new Runnable() {
            @Override
            public void run() {
                if (hanganAnswersTable != null) {
                    hanganAnswersTable.addAction(Actions.fadeOut(0.2f));
                }
                if (gameContext.getCurrentUserGameUser().getLostQuestions() > 0) {
                    new LevelFinishedPopup(getAbstractScreen(), campaignLevel, gameContext) {
                        @Override
                        public void addButtons() {
                            MyButton campaignScreenBtn = new ButtonBuilder().setDefaultButton()
                                    .setContrast(Contrast.LIGHT)
                                    .setFontColor(FontColor.BLACK)
                                    .setText(SkelGameLabel.go_back.getText()).build();
                            addButton(campaignScreenBtn);
                            campaignScreenBtn.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    screenManager.showCampaignScreen();
                                }
                            });
                        }
                    }.addToPopupManager();
                } else {
                    float durationFadeOut = 0.2f;
                    for (MyButton button : categHintBtnList) {
                        button.addAction(Actions.sequence(Actions.fadeOut(durationFadeOut), Utils.createRemoveActorAction(button)));
                    }
                    categHintBtnList.clear();
                    if (categHintTable != null) {
                        categHintTable.remove();
                    }
                    allTable.addAction(Actions.sequence(Actions.fadeOut(durationFadeOut), Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            allTable.remove();
                            buildStage();
                            if (hanganAnswersTable != null) {
                                hanganAnswersTable.addAction(Actions.fadeIn(durationFadeOut));
                            }
                        }
                    })));
                }
            }
        });
    }

    @Override
    public void showPopupAd(Runnable runnable) {
        int questionsPlayed = new CampaignStoreService().getNrOfQuestionsPlayed();
        if (questionsPlayed > 0 && questionsPlayed % 7 == 0) {
            Game.getInstance().getAppInfoService().showPopupAd(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(HangmanGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        if (new SinglePlayerLevelFinishedService().isGameFailed(gameContext.getCurrentUserGameUser())) {
            new LevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        } else {
            screenManager.showMainScreen();
        }
    }

    private void processGameOver() {
        new MyPopup<AbstractScreen, HangmanScreenManager>(getAbstractScreen()) {
            @Override
            protected String getLabelText() {
                String text = MainGameLabel.l_congratulations.getText();
                text = text + "\n";
                text = text + MainGameLabel.l_you_win.getText();
                return text;
            }

            @Override
            protected void addButtons() {
            }

            @Override
            public void hide() {
                super.hide();
                screenManager.showMainScreen();
            }
        }.addToPopupManager();
    }
}
