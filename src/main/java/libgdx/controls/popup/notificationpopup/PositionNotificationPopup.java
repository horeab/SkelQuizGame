package libgdx.controls.popup.notificationpopup;

import java.util.Objects;

import libgdx.controls.popup.NotTransferableToOtherScreenPopup;

public class PositionNotificationPopup extends MyNotificationPopup implements NotTransferableToOtherScreenPopup {

    private float x;
    private float y;

    PositionNotificationPopup(float x, float y, MyNotificationPopupConfig config) {
        super(config);
        this.x = x;
        this.y = y;
        setTouchable();
    }


    @Override
    public void executeOnShow() {
        setX(x);
        setY(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionNotificationPopup that = (PositionNotificationPopup) o;
        return Float.compare(that.x, x) == 0 &&
                Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public PositionNotificationPopup createNewInstance() {
        return new MyNotificationPopupCreator(getConfig()).positionNotificationPopup(x, y);
    }
}
