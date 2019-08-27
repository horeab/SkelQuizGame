package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import libgdx.controls.ScreenRunnable;
import libgdx.implementations.skelgame.QuizGameSpecificResource;
import libgdx.screens.implementations.geoquiz.GameScreen;
import libgdx.utils.SoundUtils;

import java.util.Random;

public class QuestionFinishedOperationsService {

    private GameScreen gameScreen;
    private GameContext gameContext;
    private GameControlsService gameControlsService;
    private LevelFinishedService levelFinishedService;

    public QuestionFinishedOperationsService(GameScreen gameScreen, GameContext gameContext, GameControlsService gameControlsService) {
        this.gameScreen = gameScreen;
        this.gameContext = gameContext;
        this.gameControlsService = gameControlsService;
        this.levelFinishedService = new SinglePlayerLevelFinishedService();
    }


    public void executeGameFinishedDependingNumberOfQuestions() {
        QuestionFinishedOperationsService questionFinishedOperationsService = new QuestionFinishedOperationsService(gameScreen, gameContext, gameControlsService);
        if (gameContext.getCurrentUserGameUser().userHasMultipleQuestions()) {
            questionFinishedOperationsService.executeGameFinishedOperations();
        } else {
            questionFinishedOperationsService.executeFinishedQuestionOperations();
        }
    }

    public void executeFinishedQuestionOperations() {
        processLastFinishedQuestion();
        if (new Random().nextInt(8) == 2) {
            gameScreen.showPopupAd();
        }
    }

    private void processLastFinishedQuestion() {
        gameControlsService.disableTouchableAllControls();
        new Thread(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                final boolean gameFinished = levelFinishedService.isGameFinished(gameContext);
                Gdx.app.postRunnable(new ScreenRunnable(gameScreen) {
                    @Override
                    public void executeOperations() {
                        if (gameFinished) {
                            executeGameFinishedOperations();
                        } else {
                            executeQuestionFinishedOperations();
                        }
                    }
                });
            }
        }).start();
    }

    private void executeQuestionFinishedOperations() {
        gameScreen.goToNextQuestionScreen();
    }

    private void executeGameFinishedOperations() {
        new Thread(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                final UsersWithLevelFinished usersWithLevelFinished = levelFinishedService.createUsersWithGameFinished(gameContext);
                Gdx.app.postRunnable(new ScreenRunnable(gameScreen) {
                    @Override
                    public void executeOperations() {
                        SoundUtils.playSound(gameContext.getCurrentUserGameUser().equals(usersWithLevelFinished.getGameUserThatWon()) ? QuizGameSpecificResource.sound_success_game_over : QuizGameSpecificResource.sound_fail_game_over);
                        animateGameFinished(usersWithLevelFinished);
                    }
                });
            }
        }).start();
    }

    private void animateGameFinished(final UsersWithLevelFinished usersWithLevelFinished) {
        RunnableAction ra = new RunnableAction();
        ra.setRunnable(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                gameScreen.executeLevelFinished();
            }
        });
        gameScreen.addAction(Actions.sequence(Actions.delay(1f), ra));
    }
}
