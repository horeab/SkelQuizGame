package libgdx.controls.popup.notificationpopup;

import java.util.HashSet;
import java.util.Set;

import libgdx.controls.popup.MultipleShowsOnScreenPopup;
import libgdx.controls.popup.NotTransferableToOtherScreenPopup;
import libgdx.controls.popup.PopupManager;
import libgdx.screen.AbstractScreen;

public class MyNotificationPopupManager extends PopupManager<MyNotificationPopup> {

    public MyNotificationPopupManager(AbstractScreen abstractScreen) {
        super(abstractScreen);
    }

    @Override
    public boolean popupShouldBeDisplayed(MyNotificationPopup popup) {
        return MultipleShowsOnScreenPopup.class.isAssignableFrom(popup.getClass())
                ||
                !isPopupDisplayed(popup.getClass());
    }

    @Override
    protected void processShowPopup(MyNotificationPopup popup) {
        super.processShowPopup(popup);
        popup.executeOnShow();
        getAbstractScreen().addActor(popup);
    }

    public void transferNotificationsFromOtherPopupManager(MyNotificationPopupManager popupManager) {
        Set<MyNotificationPopup> allPopups = new HashSet<>();
        allPopups.addAll(popupManager.displayedPopups);
        allPopups.addAll(popupManager.popupsWaitingToBeDisplayed);
        for (MyNotificationPopup popup : allPopups) {
            if (popup.getConfig().getTransferBetweenScreens() && !NotTransferableToOtherScreenPopup.class.isAssignableFrom(popup.getClass())) {
                popup.createNewInstance().addToPopupManager();
            }
        }
    }
}
