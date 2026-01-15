package libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class AbstractScreenManager {

    static String LOADING_LABEL_NAME = "LOADING_LABEL_NAME";

    public static boolean backAlreadyKeyPressed;
    private Game game;

    public void initialize(Game game) {
        Gdx.input.setCatchBackKey(true);
        this.game = game;
    }

    public abstract void showMainScreen();

    protected void showScreen(final ScreenType screenType, final Object... params) {
        //invalidate old screen
        if (game.getScreen() != null && game.getScreen() instanceof AbstractScreen) {
            AbstractScreen screen = (AbstractScreen) game.getScreen();
            screen.setScreenInvalid();
        }
        //Gdx.app.postRunnable needed because of external calls (example: android app is running on other thread, not the main Gdx)
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                final AbstractScreen newScreen = screenType.getScreen(params);
                fadeInAndSetNewScreen(game.getScreen(), newScreen);
            }
        });
    }

    private void fadeInAndSetNewScreen(Screen oldScreen, AbstractScreen newScreen) {
        MyWrappedLabel loadingLabel = addLoadingLabel(newScreen);
        SequenceAction actionToExecute = Actions.action(SequenceAction.class);
        if (game.getScreen() == null || !newScreen.getClass().equals(game.getScreen().getClass())) {
            newScreen.getRoot().getColor().a = 0;
            actionToExecute.addAction(Actions.fadeIn(0.0f));
        }
        actionToExecute.addAction(executeScreenBuildStageAsync(newScreen));
        actionToExecute.addAction(removeLoadingLabelAction(newScreen, loadingLabel));
        newScreen.getRoot().addAction(actionToExecute);
        game.setScreen(newScreen);
        transferInformationFromOldScreenToNewScreen(oldScreen, newScreen);
    }

    private RunnableAction executeScreenBuildStageAsync(final AbstractScreen newScreen) {
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                newScreen.buildStage();
                newScreen.afterBuildStage();
            }
        });
        return runnableAction;
    }

    private void transferInformationFromOldScreenToNewScreen(Screen oldScreen, AbstractScreen newScreen) {
        if (oldScreen != null && oldScreen instanceof AbstractScreen) {
            newScreen.getMyNotificationPopupManager().transferNotificationsFromOtherPopupManager(((AbstractScreen) oldScreen).getMyNotificationPopupManager());
        }
    }

    public static RunnableAction removeLoadingLabelAction(final AbstractScreen newScreen, final MyWrappedLabel loadingLabel) {
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                newScreen.removeActor(loadingLabel);
            }
        });
        return runnableAction;
    }

    public static MyWrappedLabel addLoadingLabel(AbstractScreen newScreen) {
        MyWrappedLabel loadingLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontColor(FontColor.WHITE).setSingleLineLabel().setText(MainGameLabel.loading.getText()).build());
        loadingLabel.setFontConfig(new FontConfig(FontConfig.FONT_SIZE));
        loadingLabel.setName(LOADING_LABEL_NAME);
        loadingLabel.setFontScale(Game.getInstance().getAppInfoService().isPortraitMode() ? FontManager.getNormalBigFontDim() : FontManager.getBigFontDim());
        ActorPositionManager.setActorCenterScreen(loadingLabel);
        newScreen.addActor(loadingLabel);
        return loadingLabel;
    }
}
