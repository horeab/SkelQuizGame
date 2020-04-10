package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kennstde.KennstDeSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.MainResource;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;

import java.util.*;

public abstract class QuestionContainerCreatorService<TGameService extends GameService> {

    private Map<String, MyButton> allAnswerButtons;
    private List<HintButton> hintButtons;
    private GameQuestionInfo gameQuestionInfo;
    protected TGameService gameService;
    protected Table questionContainer;
    protected GameContext gameContext;
    protected GameScreen abstractGameScreen;
    protected GameControlsService gameControlsService;
    protected RefreshQuestionDisplayService refreshQuestionDisplayService;

    public QuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        this.gameQuestionInfo = gameContext.getCurrentUserGameUser().getGameQuestionInfo();
        this.gameService = (TGameService) GameServiceContainer.getGameService(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        this.questionContainer = new Table();
        this.abstractGameScreen = abstractGameScreen;
        this.gameContext = gameContext;
        this.allAnswerButtons = createAnswerOptionsButtons(gameService.getAllAnswerOptions());
        this.hintButtons = createHintButtons(abstractGameScreen);
        this.refreshQuestionDisplayService = gameContext.getCurrentUserCreatorDependencies().getRefreshQuestionDisplayService(abstractGameScreen, gameContext, getAllAnswerButtons());
        this.gameControlsService = new GameControlsService(allAnswerButtons, hintButtons);
        addOnAnswerClick();
        addOnHintClick();
    }

    public AbstractScreen getAbstractGameScreen() {
        return abstractGameScreen;
    }

    protected abstract MyButton createAnswerButton(String answer);

    public abstract ButtonSkin correctAnswerSkin();

    public abstract ButtonSkin wrongAnswerSkin();

    public abstract int getNrOfAnswerRows();

    public abstract int getNrOfAnswersOnRow();

    public abstract Table createAnswerOptionsTable();

    public Table createQuestionTable() {
        Table table = new Table();
        table.add(questionContainer).pad(MainDimen.horizontal_general_margin.getDimen() * 4);
        setContainerBackground();
        return table;
    }


    private void addOnAnswerClick() {
        for (final Map.Entry<String, MyButton> entry : (Set<Map.Entry<String, MyButton>>) getAllAnswerButtons().entrySet()) {
            entry.getValue().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    answerClick(entry.getKey());
                }
            });
        }
    }

    private void answerClick(final String answer) {
        GameUser currentUserGameUser = gameContext.getCurrentUserGameUser();
        GameQuestionInfo gameQuestionInfo = currentUserGameUser.getGameQuestionInfo();
        gameService.addAnswerToGameInfo(currentUserGameUser, new GameAnswerInfo(answer, abstractGameScreen.getMillisPassedSinceScreenDisplayed()));
        processGameInfo(gameQuestionInfo);
    }

    public void processGameInfo(final GameQuestionInfo gameQuestionInfo) {
        refreshQuestionDisplayService.refreshQuestion(gameQuestionInfo);
        int nrOfWrongLettersPressed = gameService.getNrOfWrongAnswersPressed(gameQuestionInfo.getAnswerIds());
        for (final GameAnswerInfo gameAnswerInfo : new ArrayList<>(gameQuestionInfo.getAnswers())) {
            ButtonSkin buttonSkin = gameService.isAnswerCorrectInQuestion(gameAnswerInfo.getAnswer()) ? correctAnswerSkin() : wrongAnswerSkin();
            MyButton button = (MyButton) getAllAnswerButtons().get(gameAnswerInfo.getAnswer());
            try {
                gameControlsService.disableButton(button);
                button.setButtonSkin(buttonSkin);
            } catch (NullPointerException e) {
                int i = 0;
            }
        }
        if (!gameQuestionInfo.isQuestionOpen()) {
            RunnableAction action1 = new RunnableAction();
            action1.setRunnable(new ScreenRunnable(abstractGameScreen) {
                @Override
                public void executeOperations() {
                    gameControlsService.disableTouchableAllControls();
                    refreshQuestionDisplayService.gameOverQuestion(gameQuestionInfo);
                }
            });
            RunnableAction action2 = new RunnableAction();
            action2.setRunnable(new ScreenRunnable(abstractGameScreen) {
                @Override
                public void executeOperations() {
                    new QuestionFinishedOperationsService(abstractGameScreen, gameContext, gameControlsService).executeFinishedQuestionOperations();
                }
            });
            abstractGameScreen.addAction(Actions.sequence(action1, Actions.delay(1f), action2));
        }
    }

    protected void setContainerBackground() {
        questionContainer.setBackground(GraphicUtils.getNinePatch(Resource.question_background));
    }

    public Map<String, MyButton> getAllAnswerButtons() {
        return allAnswerButtons;
    }

    public List<HintButton> getHintButtons() {
        return hintButtons;
    }

    private List<HintButton> createHintButtons(AbstractScreen abstractGameScreen) {
        List<HintButton> hintButtons = new ArrayList<>();
        for (HintButtonType hintButtonType : gameContext.getAvailableHints()) {
            HintButtonBuilder hintButtonBuilder = new HintButtonBuilder(hintButtonType, abstractGameScreen);
            enrichHintButtonBuilder(hintButtonBuilder);
            hintButtons.add(hintButtonBuilder.build());
        }
        return hintButtons;
    }

    protected void enrichHintButtonBuilder(HintButtonBuilder hintButtonBuilder) {
    }

    private Map<String, MyButton> createAnswerOptionsButtons(List<String> allAnswerOptions) {
        Map<String, MyButton> allAnswerButtons = new LinkedHashMap<>();
        for (String answer : allAnswerOptions) {
            MyButton button = createAnswerButton(answer);
            allAnswerButtons.put(answer.toLowerCase(), button);
        }
        return allAnswerButtons;
    }

    private void addOnHintClick() {
        for (final HintButton hintButton : (List<HintButton>) getHintButtons()) {
            hintButton.getMyButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getHintButtonOnClick(hintButton).run();
                    gameContext.useHint();
                }
            });
        }
    }

    private Runnable getHintButtonOnClick(HintButton hintButton) {
        if (hintButton.getHintButtonType().equals(HintButtonType.HINT_PRESS_RANDOM_ANSWER)) {
            return createRandomAnswerHintButtonOnClick();
        } else if (hintButton.getHintButtonType().equals(HintButtonType.HINT_DISABLE_TWO_ANSWERS)) {
            return createHintDisableTwoAnswersButtonOnClick();
        }
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }


    private Runnable createHintDisableTwoAnswersButtonOnClick() {
        return new Runnable() {
            @Override
            public void run() {
                List<String> originalAnswerOptions = gameService.getAllAnswerOptions();
                List<String> answerOptions = new ArrayList<>(originalAnswerOptions);
                answerOptions.removeAll(gameService.getUnpressedCorrectAnswers(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswerIds()));
                List<String> answersToDisable = new ArrayList<>();
                if (answerOptions.size() > 0 && originalAnswerOptions.size() > 2) {
                    answersToDisable.add(answerOptions.get(0));
                }
                if (answerOptions.size() > 1 && originalAnswerOptions.size() > 3) {
                    answersToDisable.add(answerOptions.get(1));
                }
                for (String answer : answersToDisable) {
                    gameControlsService.disableButton((MyButton) getAllAnswerButtons().get(answer.toLowerCase()));
                }
                gameControlsService.disableAllHintButtons();
            }
        };
    }

    private Runnable createRandomAnswerHintButtonOnClick() {
        return new Runnable() {
            @Override
            public void run() {
                String randomUnpressedAnswer = gameService.getRandomUnpressedAnswerFromQuestion(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswerIds());
                if (StringUtils.isNotBlank(randomUnpressedAnswer)) {
                    answerClick(randomUnpressedAnswer);
                }
            }
        };
    }

    protected void addQuestionImage(Image questionImage) {
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float imgHeight = questionImage.getHeight();
        float imgWidth = questionImage.getWidth();
        float imgHeightToBeDisplayed = 9999;
        float newWidth = 9999;
        int imgHeightMultiplier = 18;
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        while (newWidth > screenWidth - verticalGeneralMarginDimen * 2) {
            imgHeightMultiplier--;
            imgHeightToBeDisplayed = verticalGeneralMarginDimen * imgHeightMultiplier;
            newWidth = ScreenDimensionsManager.getNewWidthForNewHeight(imgHeightToBeDisplayed, imgWidth, imgHeight);
        }
        questionContainer.add(questionImage).height(imgHeightToBeDisplayed).width(newWidth).center();
    }

    protected Table createQuestionImage(Image questionImage, float maxWidth, float maxHeight) {
        return createImageTableForMaxHeightMaxWidth(questionImage, maxWidth, maxHeight);
    }

    private Table createImageTableForMaxHeightMaxWidth(Image questionImage, float maxWidth, float maxHeight) {
        float origImgHeight = questionImage.getHeight();
        float origImgWidth = questionImage.getWidth();
        float newHeight = ScreenDimensionsManager.getNewHeightForNewWidth(maxWidth, origImgWidth, origImgHeight);
        while (newHeight > maxHeight) {
            maxWidth--;
            newHeight = ScreenDimensionsManager.getNewHeightForNewWidth(maxWidth, origImgWidth, origImgHeight);
        }
        Table imageTable = new Table();
        imageTable.add(questionImage).height(newHeight).width(maxWidth).center();
        return imageTable;
    }

    public Table createHintButtonsTable() {
        Table table = new Table();
        float padSide = MainDimen.horizontal_general_margin.getDimen() * 2;
        for (HintButton button : hintButtons) {
            table.add(button.getMyButton()).height(button.getMyButton().getHeight()).width(button.getMyButton().getWidth()).padRight(padSide);
        }
        return table;
    }

    public GameQuestionInfo getGameQuestionInfo() {
        return gameQuestionInfo;
    }

}
