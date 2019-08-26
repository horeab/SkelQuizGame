package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import libgdx.controls.ScreenRunnable;
import libgdx.screen.AbstractScreen;
import libgdx.utils.SoundUtils;

import java.util.Random;

public class QuestionFinishedOperationsService {

    private AbstractScreen gameScreen;
    private GameContext gameContext;

    public QuestionFinishedOperationsService(AbstractScreen gameScreen, GameContext gameContext) {
        this.gameScreen = gameScreen;
        this.gameContext = gameContext;
    }


    public void executeGameFinishedDependingNumberOfQuestions() {
        QuestionFinishedOperationsService questionFinishedOperationsService = new QuestionFinishedOperationsService(gameScreen, gameContext);
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
        gameScreen.getScreenCreator().getGameScreenQuestionContainerCreator().getGameControlsService().disableTouchableAllControls();
        new Thread(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                final boolean gameFinished = gameScreen.getLevelFinishedService().isGameFinished(gameContext);
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
                final UsersWithLevelFinished usersWithLevelFinished = gameScreen.getLevelFinishedService().createUsersWithGameFinished(gameContext);
                Gdx.app.postRunnable(new ScreenRunnable(gameScreen) {
                    @Override
                    public void executeOperations() {
                        SoundUtils.playSound(gameContext.getCurrentUserGameUser().equals(usersWithLevelFinished.getUserThatWon()) ? Resource.sound_success_game_over : Resource.sound_fail_game_over);
                        animateGameFinished(usersWithLevelFinished);
                    }
                });
            }
        }).start();
    }

    private void animateGameFinished(final UsersWithLevelFinished usersWithLevelFinished) {
        RunnableAction ra2 = new RunnableAction();
        ra2.setRunnable(new ScreenRunnable(gameScreen) {
            @Override
            public void executeOperations() {
                gameScreen.executeLevelFinished();
            }
        });
        gameScreen.addAction(Actions.sequence(Actions.delay(1f), ra2));
    }
}
