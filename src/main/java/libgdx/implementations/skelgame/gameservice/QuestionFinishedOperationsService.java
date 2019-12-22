package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.ScreenRunnable;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screens.GameScreen;
import libgdx.screens.implementations.geoquiz.HeaderCreator;

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
        new CampaignStoreService().incrementNrOfQuestionsPlayed();
        gameScreen.showPopupAd(new Runnable() {
            @Override
            public void run() {
                processLastFinishedQuestion();
            }
        });
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
        GameUser currentUserGameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameFinished(gameContext)) {
            executeGameFinishedOperations();
        }
        if (currentUserGameUser.getGameQuestionInfo(currentUserGameUser.getFinishedQuestions() - 1).getStatus() == GameQuestionInfoStatus.WON && currentUserGameUser.getWonQuestions() == LevelFinishedService.correctAnsweredQuestionsForGameSuccess(currentUserGameUser.getTotalNrOfQuestions())) {
//            SoundUtils.playSound(Resource.sound_success_game_over);
        }
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
                        boolean gameWon = gameContext.getCurrentUserGameUser().equals(usersWithLevelFinished.getGameUserThatWon());
//                        SoundUtils.playSound(gameWon ? Resource.sound_success_game_over : Resource.sound_fail_game_over);
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
                Actor actor = gameScreen.getRoot().findActor(HeaderCreator.HEADER_TABLE_NAME);
                if (actor != null) {
                    actor.addAction(Actions.fadeOut(0.5f));
                }
                gameScreen.executeLevelFinished();
            }
        });
        gameScreen.animateGameFinished();
        gameScreen.addAction(Actions.sequence(Actions.delay(1f), ra));
    }
}
