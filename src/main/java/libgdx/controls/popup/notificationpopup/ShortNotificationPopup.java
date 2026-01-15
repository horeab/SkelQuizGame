package libgdx.controls.popup.notificationpopup;

import com.badlogic.gdx.utils.Timer;

import libgdx.controls.popup.Popup;
import libgdx.screen.AbstractScreen;

public class ShortNotificationPopup extends MyNotificationPopup<AbstractScreen> implements Popup {

    private static final float SECONDS_NOTIFICATION_POPUP_DISPLAYED = 1.5f;

    ShortNotificationPopup(MyNotificationPopupConfig config) {
        super(config);
        setTouchable();
    }

    @Override
    public void executeOnShow() {
        super.executeOnShow();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hide();
            }
        }, SECONDS_NOTIFICATION_POPUP_DISPLAYED);
    }

    @Override
    public ShortNotificationPopup createNewInstance() {
        return new MyNotificationPopupCreator(getConfig()).shortNotificationPopup();
    }
}
