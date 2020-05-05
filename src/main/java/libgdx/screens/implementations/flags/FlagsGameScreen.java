package libgdx.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.flags.FlagsCampaignLevelEnum;
import libgdx.implementations.flags.FlagsDifficultyLevel;
import libgdx.implementations.flags.FlagsSpecificResource;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.*;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;
import org.apache.commons.lang3.StringUtils;
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

    private float durationFlagUpToDown;
    private float durationNextFlag;
    private CampaignLevel campaignLevel;
    private CampaignLevelEnumService enumService;

    private int maxNumberOfWrongAnswers;
    private int numberOfWrongAnswersPressed = 0;

    private List<GameQuestionInfo> displayedQuestionInfos = new ArrayList<>();
    private List<GameQuestionInfo> availableGameQuestionInfosToPlay;
    private int leftCountriesToPlay;
    private FlagsSettings flagsSettings;

    public FlagsGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        initFlagsSettings();
        setFlagSpeed();
        availableGameQuestionInfosToPlay = new ArrayList<>(gameContext.getCurrentUserGameUser().getAllQuestionInfos());
        this.campaignLevel = campaignLevel;
        enumService = new CampaignLevelEnumService(campaignLevel);
        leftCountriesToPlay = FlagsContainers.getAllQuestions(campaignLevel).size();
    }

    private void setFlagSpeed() {
        float div = 2.2f;
        maxNumberOfWrongAnswers = 6;
        if (flagsSettings.getFlagsDifficultyLevel() == FlagsDifficultyLevel._1) {
            maxNumberOfWrongAnswers = 5;
            div = 2.7f;
        } else if (flagsSettings.getFlagsDifficultyLevel() == FlagsDifficultyLevel._2) {
            maxNumberOfWrongAnswers = 4;
            div = 3f;
        }
        durationFlagUpToDown = 15f / div;
        durationNextFlag = 3f / div;
    }

    private void initFlagsSettings() {
        String json = campaignStoreService.getJson();
        if (StringUtils.isNotBlank(json)) {
            flagsSettings = new Gson().fromJson(json, FlagsSettings.class);
        } else {
            flagsSettings = new FlagsSettings();
        }
    }

    @Override
    public void buildStage() {
        createAllTable();
        FlagsContainers.setBackgroundDiff(flagsSettings, getBackgroundStage());
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
        if (!gameOver()) {
            String text = gameQuestionInfo.getQuestion().getQuestionString().split(":")[2];
            if (countryNameTable != null) {
                countryNameTable.remove();
            }
            countryNameTable = new Table();
            countryNameTable.setName("countryNameTable");
            int labelWidth = ScreenDimensionsManager.getScreenWidth() / 2;
            MyWrappedLabel countryNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setWrappedLineLabel(labelWidth).setText(
                            StringUtils.capitalize(text)).build());
            countryNameLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            countryNameLabel.setWidth(labelWidth);
            countryNameLabel.setHeight(ScreenDimensionsManager.getScreenHeightValue(10));
            countryNameTable.setWidth(countryNameLabel.getWidth());
            countryNameTable.setHeight(countryNameLabel.getHeight());
            countryNameTable.setX(ScreenDimensionsManager.getScreenWidth() / 2 - labelWidth / 2);
            countryNameTable.setY(MainDimen.vertical_general_margin.getDimen() * 2);
            countryNameTable.add(countryNameLabel).row();


            countryNameLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            countryNameLabel.setWidth(labelWidth);
            countryNameLabel.setHeight(ScreenDimensionsManager.getScreenHeightValue(10));
            countryNameTable.add(FlagsContainers.createFlagsCounter(leftCountriesToPlay, labelWidth, null))
                    .padBottom(MainDimen.vertical_general_margin.getDimen() * 3)
                    .width(countryNameLabel.getWidth()).height(countryNameLabel.getWidth() / 5);
            addActor(countryNameTable);
        }
    }

    private List<GameQuestionInfo> getAvailableGameQuestionInfosToPlay() {
        if (gameContext.getCurrentUserGameUser().getGameQuestionInfo() == null) {
            return new ArrayList<>();
        } else {
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
//            if (new Random().nextInt(100) > 1) {
            if (new Random().nextInt(100) > 60) {
                modifList.addFirst(currentQ);
            }
            return new ArrayList<>(modifList);
        }
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
                        removeFlagFromScreen(gameQuestionInfo, false, 0.2f);
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
                        removeFlagFromScreen(gameQuestionInfo, true, 0.2f);
                        if (questionCorrectAnswered(gameQuestionInfo)) {
                            boolean areBeforeCorrectAnswer = true;
                            for (GameQuestionInfo dispQi : copyDisplayedQuestionInfos) {
                                if (questionCorrectAnswered(dispQi)) {
                                    areBeforeCorrectAnswer = false;
                                }
                                if (areBeforeCorrectAnswer) {
                                    removeFlagFromScreen(dispQi, false, 0.05f);
                                }
                            }
                            leftCountriesToPlay = leftCountriesToPlay - 1;
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

    private void removeFlagFromScreen(GameQuestionInfo gameQuestionInfo, boolean onClick, float duration) {
        Image image = getRoot().findActor(gameQuestionInfo.getQuestion().getQuestionString());
        if (questionCorrectAnswered(gameQuestionInfo) && !onClick
                ||
                !questionCorrectAnswered(gameQuestionInfo) && onClick) {
            new ActorAnimation(getAbstractScreen()).animatePulse();
            Table wrongAnswerTable = new Table();
            wrongAnswerTable.setBackground(GraphicUtils.getNinePatch(FlagsSpecificResource.wall_texture));
            allTable.clearChildren();
            numberOfWrongAnswersPressed++;
            if (numberOfWrongAnswersPressed > 0) {
                allTable.add().growY();
                allTable.add(wrongAnswerTable)
                        .height(ScreenDimensionsManager.getScreenHeightValue((100f / maxNumberOfWrongAnswers)
                                * numberOfWrongAnswersPressed))
                        .width(ScreenDimensionsManager.getScreenWidth())
                        .bottom()
                        .row();
            }
            if (gameOver()) {
                countryNameTable.remove();
                new FlagsLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
                executorService.shutdown();
                for (GameQuestionInfo gq : new ArrayList<>(displayedQuestionInfos)) {
                    Image img = getRoot().findActor(gq.getQuestion().getQuestionString());
                    img.remove();
                }
            }
        }
        image.addAction(Actions.sequence(Actions.fadeOut(duration), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                image.remove();
            }
        })));
        displayedQuestionInfos.remove(gameQuestionInfo);
    }

    private boolean gameOver() {
        return numberOfWrongAnswersPressed >= maxNumberOfWrongAnswers;
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
                            if (!gameOver()) {
                                displayFlag();
                            }
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
            }
        }
    }

    @Override
    public void goToNextQuestionScreen() {
        processPlayedQuestions();
        if (gameContext.getCurrentUserGameUser().getWonQuestions() == gameContext.getCurrentUserGameUser().getAllQuestionInfos().size()) {
            for (int i = 0; i <= enumService.getDifficulty(); i++) {
                campaignStoreService.putQuestionPlayed(FlagsCampaignLevelEnum.valueOf("LEVEL_" + i + "_" + enumService.getCategory()).getName());
            }
            RunnableAction runnableAction = Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    showPopupAd(new Runnable() {
                        @Override
                        public void run() {
                            screenManager.showMainScreen();
                        }
                    });
                }
            });
            ActorAnimation.animateImageCenterScreenFadeOut(FlagsSpecificResource.star, 0.7f);
            addAction(Actions.sequence(Actions.delay(0.7f), runnableAction));
        } else {
            displayCountryName(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void animateGameFinished() {
        super.animateGameFinished();
        processPlayedQuestions();
    }

    @Override
    public void executeLevelFinished() {

    }
}