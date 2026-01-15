package libgdx.controls.popup.notificationpopup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Objects;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.popup.Popup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.screen.AbstractScreen;


public abstract class MyNotificationPopup<TScreen extends AbstractScreen> extends Table implements Popup {

    private static final float SECONDS_NOTIFICATION_POPUP_SHOW_HIDE_DELAY = 0.3f;

    private TScreen abstractScreen;
    private MyNotificationPopupConfig config;

    public MyNotificationPopup(MyNotificationPopupConfig config) {
        this.config = config;
        this.abstractScreen = Game.getInstance().getScreen() != null && Game.getInstance().getScreen() instanceof AbstractScreen ? (TScreen) Game.getInstance().getScreen() : null;
        setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
    }

    @Override
    public MyNotificationPopupManager getPopupManager() {
        return abstractScreen.getMyNotificationPopupManager();
    }

    @Override
    public TScreen getScreen() {
        return abstractScreen;
    }

    public void setTouchable() {
        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeOnTouch();
            }
        });
    }

    protected void executeOnTouch() {
        hide();
    }

    public void executeOnShow() {
    }

    @Override
    public void onBackKeyPress() {
    }

    public MyNotificationPopupConfig getConfig() {
        return config;
    }

    public abstract <TType extends MyNotificationPopup> TType createNewInstance();

    @Override
    public MyNotificationPopup addToPopupManager() {
        if (abstractScreen != null && !getPopupManager().isPopupAlreadyManaged(this)) {
            getColor().a = 0;
            addAction(Actions.fadeIn(SECONDS_NOTIFICATION_POPUP_SHOW_HIDE_DELAY));
            getPopupManager().addPopupToDisplay(this);
        }
        return this;
    }

    @Override
    public void hide() {
        final MyNotificationPopup popup = this;
        RunnableAction removeTableFromScreen = new RunnableAction();
        removeTableFromScreen.setRunnable(new ScreenRunnable(getScreen()) {
            @Override
            public void executeOperations() {
                getPopupManager().hidePopup(popup);
            }
        });
        addAction(Actions.sequence(Actions.fadeOut(SECONDS_NOTIFICATION_POPUP_SHOW_HIDE_DELAY), removeTableFromScreen));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyNotificationPopup<?> popup = (MyNotificationPopup<?>) o;
        return Objects.equals(config, popup.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config);
    }
}
