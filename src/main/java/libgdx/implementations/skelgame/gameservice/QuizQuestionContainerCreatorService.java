package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.skelgame.QuizGameButtonSize;
import libgdx.implementations.skelgame.QuizGameButtonSkin;
import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class QuizQuestionContainerCreatorService extends QuestionContainerCreatorService<QuizGameService> {

    public QuizQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        addOnAnswerClick();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = super.createQuestionTable();
        Image questionImage = gameService.getQuestionImage();
        String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
        MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = new MyWrappedLabelConfigBuilder().setText(questionToBeDisplayed);
        if (questionImage == null) {
            myWrappedLabelConfigBuilder.setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f));
        }
        myWrappedLabelConfigBuilder.setFontScale(getQuestionFontScale(questionToBeDisplayed,
                myWrappedLabelConfigBuilder.getFontScale(), GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions())));
        MyWrappedLabel questionLabel = new MyWrappedLabel(myWrappedLabelConfigBuilder.setStyleDependingOnContrast().build());
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        questionContainer.add(questionLabel).padBottom(verticalGeneralMarginDimen).row();
        if (questionImage != null) {
            addQuestionImage(questionImage);
        }
        return questionTable;
    }

    private float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        float factor = 1f;
        //if there are long answer buttons, the question fontScale should be smaller
        float increaseFactor = longAnswerButtons ? 0.035f : 0.018f;
        int increaseWordCount = 5;
        for (int standardQuestionLength = 160; standardQuestionLength < 600; standardQuestionLength = standardQuestionLength + increaseWordCount)
            if (questionToBeDisplayed.length() > standardQuestionLength) {
                factor = factor + increaseFactor;
            } else {
                break;
            }
        return fontScale / factor;
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new GameControlsCreatorService().createAnswerOptionsTable(gameService.getAllAnswerOptions(),
                getNrOfAnswersOnRow(),
                getNrOfAnswerRows(),
                new ArrayList<>(getAllAnswerButtons().values()));
    }

    @Override
    public int getNrOfAnswerRows() {
        return 2;
    }

    @Override
    public int getNrOfAnswersOnRow() {
        return 2;
    }

    @Override
    public QuizGameButtonSkin correctAnswerSkin() {
        return getCorrectQuizGameButtonSkin(gameService.getAllAnswerOptions());
    }

    public static QuizGameButtonSkin getCorrectQuizGameButtonSkin(List<String> allAnswerOptions) {
        return GameControlsCreatorService.longAnswerButtons(allAnswerOptions) ? QuizGameButtonSkin.LONG_ANSWER_OPTION_CORRECT : QuizGameButtonSkin.SQUARE_ANSWER_OPTION_CORRECT;
    }

    @Override
    public QuizGameButtonSkin wrongAnswerSkin() {
        return GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions()) ? QuizGameButtonSkin.LONG_ANSWER_OPTION_WRONG : QuizGameButtonSkin.SQUARE_ANSWER_OPTION_WRONG;
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        ButtonSize buttonSize = QuizGameButtonSize.SQUARE_QUIZ_OPTION_ANSWER;
        QuizGameButtonSkin buttonSkin = QuizGameButtonSkin.SQUARE_ANSWER_OPTION;
        if (GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions())) {
            buttonSize = QuizGameButtonSize.LONG_QUIZ_OPTION_ANSWER;
            buttonSkin = QuizGameButtonSkin.LONG_ANSWER_OPTION;
        }
        return new ButtonBuilder().setWrappedText(answer, buttonSize.getWidth() / 1.1f).setFixedButtonSize(buttonSize).setButtonSkin(buttonSkin).build();
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
            QuizGameButtonSkin buttonSkin = gameService.isAnswerCorrectInQuestion(gameAnswerInfo.getAnswer()) ? correctAnswerSkin() : wrongAnswerSkin();
            MyButton button = (MyButton) getAllAnswerButtons().get(gameAnswerInfo.getAnswer());
            try {
                gameControlsService.disableButton(button);
            } catch (NullPointerException e) {
                int i = 0;
            }
            button.setButtonSkin(buttonSkin);
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

}
