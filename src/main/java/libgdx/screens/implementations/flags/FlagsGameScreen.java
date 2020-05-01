package libgdx.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.*;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screens.GameScreen;
import libgdx.screens.implementations.judetelerom.JudeteContainers;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FlagsGameScreen extends GameScreen<FlagsScreenManager> {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;
    private ScheduledExecutorService executorService;
    private Table countryNameTable;

    private int div = 2;
    private float durationFlagUpToDown = 15f / div;
    private float durationNextFlag = 3f / div;

    private int numberOfWrongAnswers = 5;

    private List<GameQuestionInfo> displayedQuestionInfos = new ArrayList<>();
    private List<GameQuestionInfo> availableGameQuestionInfosToPlay;

    public FlagsGameScreen(GameContext gameContext) {
        super(gameContext);
        availableGameQuestionInfosToPlay = new ArrayList<>(gameContext.getCurrentUserGameUser().getAllQuestionInfos());
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        allTable.add().growY().row();
        addActor(allTable);
        displayCountryName(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        displayFlag();
    }

    private void displayCountryName(GameQuestionInfo gameQuestionInfo) {
        String text = gameQuestionInfo.getQuestion().getQuestionString().split(":")[2];
        if (countryNameTable != null) {
            countryNameTable.remove();
        }
        countryNameTable = new Table();
        countryNameTable.setName("countryNameTable");
        int labelWidth = ScreenDimensionsManager.getScreenWidth() / 2;
        MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(labelWidth).setText(
                        text).build());
        countryNameLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        countryNameLabel.setWidth(labelWidth);
        countryNameLabel.setHeight(ScreenDimensionsManager.getScreenHeightValue(10));
        countryNameTable.setWidth(countryNameLabel.getWidth());
        countryNameTable.setHeight(countryNameLabel.getHeight());
        countryNameTable.add(countryNameLabel);
        countryNameTable.setX(ScreenDimensionsManager.getScreenWidth() / 2 - labelWidth / 2);
        countryNameTable.setY(MainDimen.vertical_general_margin.getDimen() * 2);
        addActor(countryNameTable);
    }

    private List<GameQuestionInfo> getAvailableGameQuestionInfosToPlay() {
        List<GameQuestionInfo> list = new ArrayList<>(availableGameQuestionInfosToPlay);
        Collections.shuffle(list);
        ArrayDeque<GameQuestionInfo> modifList = new ArrayDeque<>(list);
        GameQuestionInfo currentQ = null;
        for (GameQuestionInfo gameQuestionInfo : list) {
            if (questionCorrectAnswered(gameQuestionInfo)) {
                modifList.remove(gameQuestionInfo);
                currentQ = gameQuestionInfo;
                break;
            }
        }
        if (new Random().nextInt(100) > 60) {
            modifList.addFirst(currentQ);
        }
        return new ArrayList<>(modifList);
    }

    private void displayFlag() {
        List<GameQuestionInfo> availableGameQuestionInfosToPlay = getAvailableGameQuestionInfosToPlay();
        for (final GameQuestionInfo gameQuestionInfo : availableGameQuestionInfosToPlay) {
            if (!displayedQuestionInfos.contains(gameQuestionInfo)) {
                displayedQuestionInfos.add(gameQuestionInfo);
                int screenWidth = ScreenDimensionsManager.getScreenWidth();
                float maxWidth = screenWidth / 2.5f;
                QuizGameService gameService = (QuizGameService) GameServiceContainer.getGameService(gameQuestionInfo);
                final Image image = gameService.getQuestionImage();
                image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(maxWidth, image));
                image.setWidth(maxWidth);

                int randomX = new Random().nextInt(screenWidth);
                while (randomX + image.getWidth() > screenWidth) {
                    randomX = new Random().nextInt(screenWidth);
                }
                image.setName(gameQuestionInfo.getQuestion().getQuestionString());
                image.setX(randomX);
                image.setY(ScreenDimensionsManager.getScreenHeight());
                image.addAction(Actions.sequence(Actions.moveTo(image.getX(), 0, durationFlagUpToDown), Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        removeFlagFromScreen(image, gameQuestionInfo, false);
                        if (questionCorrectAnswered(gameQuestionInfo)) {
                            gameService.addAnswerToGameInfo(gameContext.getCurrentUserGameUser(),
                                    new GameAnswerInfo("x", getMillisPassedSinceScreenDisplayed()));
                            goToNextQuestionScreen();
                        }
                    }
                })));
                image.setTouchable(Touchable.enabled);
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ArrayList<GameQuestionInfo> copyDisplayedQuestionInfos = new ArrayList<>(displayedQuestionInfos);
                        removeFlagFromScreen(image, gameQuestionInfo, true);
                        if (questionCorrectAnswered(gameQuestionInfo)) {
                            boolean areBeforeCorrectAnswer = true;
                            for (GameQuestionInfo dispQi : copyDisplayedQuestionInfos) {
                                if (questionCorrectAnswered(dispQi)) {
                                    areBeforeCorrectAnswer = false;
                                }
                                if (areBeforeCorrectAnswer) {
                                    removeFlagFromScreen(getRoot().findActor(dispQi.getQuestion().getQuestionString()),
                                            dispQi, false);
                                }
                            }
                            gameService.addAnswerToGameInfo(gameContext.getCurrentUserGameUser(),
                                    new GameAnswerInfo(gameService.getAnswers().get(0), getMillisPassedSinceScreenDisplayed()));
                            goToNextQuestionScreen();
                        }
                    }
                });
                image.toFront();
                addActor(image);
                displayNextFlag();
                break;
            }
        }
        if (countryNameTable != null) {
            countryNameTable.toFront();
        }
    }

    private boolean questionCorrectAnswered(GameQuestionInfo gameQuestionInfo) {
        return gameQuestionInfo.getQuestion().getQuestionString().equals(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionString());
    }

    private void removeFlagFromScreen(Image image, GameQuestionInfo gameQuestionInfo, boolean onClick) {
        if (questionCorrectAnswered(gameQuestionInfo) && !onClick
                ||
                !questionCorrectAnswered(gameQuestionInfo) && onClick) {
            Table wrongAnswerTable = new Table();
            wrongAnswerTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            allTable.add(wrongAnswerTable)
                    .height(ScreenDimensionsManager.getScreenHeightValue(100 / numberOfWrongAnswers))
                    .width(ScreenDimensionsManager.getScreenWidth())
                    .bottom()
                    .row();
        }
        image.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                image.remove();
            }
        })));
        displayedQuestionInfos.remove(gameQuestionInfo);
    }

    private void displayNextFlag() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        MutableLong countdownAmountMillis = new MutableLong(durationNextFlag * 1000);
        final int period = 1000;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                if (countdownAmountMillis.getValue() <= 0) {
                    countdownAmountMillis.setValue(durationNextFlag * 1000);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            displayFlag();
                            executorService.shutdown();
                        }
                    });
                }
                countdownAmountMillis.subtract(period);
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    private void processPlayedQuestions() {
        for (GameQuestionInfo gameQuestionInfo : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.LOST) {
                gameContext.getCurrentUserGameUser().resetQuestion(gameQuestionInfo);
            } else {
                Question question = gameQuestionInfo.getQuestion();
                if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.WON && !campaignStoreService.isQuestionAlreadyPlayed(JudeteContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
                    campaignStoreService.putQuestionPlayed(JudeteContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()));
                    int questionLineInQuestionFile = question.getQuestionLineInQuestionFile();
                    if (JudeteContainers.isJudetFound(questionLineInQuestionFile)) {
                        new MyNotificationPopupCreator(new MyNotificationPopupConfigBuilder().setText(
                                SpecificPropertiesUtils.getText("ro_judetelerom_judet_found",
                                        new SpecificPropertiesUtils().getQuestionCampaignLabel
                                                (questionLineInQuestionFile))).build()).shortNotificationPopup().addToPopupManager();
                    }
                }
            }
        }
    }

    @Override
    public void goToNextQuestionScreen() {
        displayCountryName(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    protected void setBackgroundColor(RGBColor backgroundColor) {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            super.setBackgroundColor(RGBColor.RED);
        } else {
            super.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void animateGameFinished() {
        super.animateGameFinished();
        if (LevelFinishedService.getPercentageOfWonQuestions(gameContext.getCurrentUserGameUser()) == 100f) {
//            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.star, 0.3f);
        }
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            screenManager.showMainScreen();
        }
//        screenManager.showMainScreen();
    }
}
