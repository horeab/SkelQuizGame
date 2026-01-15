package libgdx.controls.popup;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedHashSet;
import java.util.Set;

import libgdx.screen.AbstractScreen;

public abstract class PopupManager<TPopupType extends Actor & Popup> {

    protected Set<TPopupType> popupsWaitingToBeDisplayed = new LinkedHashSet<>();
    protected Set<TPopupType> displayedPopups = new LinkedHashSet<>();

    private AbstractScreen abstractScreen;

    public PopupManager(AbstractScreen abstractScreen) {
        this.abstractScreen = abstractScreen;
    }

    public void addPopupToDisplay(TPopupType popup) {
        if (popup != null) {
            popupsWaitingToBeDisplayed.add(popup);
        }
    }

    public boolean isPopupDisplayed() {
        return !displayedPopups.isEmpty();
    }

    public boolean isPopupAlreadyManaged(TPopupType popup) {
        return isPopupDisplayed(popup) || isWaitingToBeDisplayed(popup);
    }

    public boolean isPopupDisplayed(TPopupType popup) {
        return displayedPopups.contains(popup);
    }

    private boolean isWaitingToBeDisplayed(TPopupType popup) {
        return popupsWaitingToBeDisplayed.contains(popup);
    }

    public boolean isPopupDisplayed(Class<? extends Popup> type) {
        for (TPopupType popup : displayedPopups) {
            if (type.isAssignableFrom(popup.getClass())) {
                return true;
            }
        }
        return false;
    }

    public void hideOtherDisplayedPopups(TPopupType popup) {
        Set<TPopupType> displayedPopups = new LinkedHashSet<>(this.displayedPopups);
        displayedPopups.remove(popup);
        hideDisplayedPopups(new LinkedHashSet<>(displayedPopups));
        this.displayedPopups.clear();
        this.displayedPopups.add(popup);
    }

    private void hideDisplayedPopups(Set<TPopupType> displayedPopups) {
        for (TPopupType popup : displayedPopups) {
            popup.hide();
        }
        displayedPopups.clear();
    }

    public void hidePopup(TPopupType popup) {
        hidePopup(displayedPopups, popup);
    }

    private void hidePopup(Set<TPopupType> displayedPopups, TPopupType popup) {
        displayedPopups.remove(popup);
        popup.remove();
    }

    public void hideAllDisplayedPopups() {
        hideDisplayedPopups(displayedPopups);
    }


    public void onBackKeyPress() {
        for (TPopupType popup : displayedPopups) {
            popup.onBackKeyPress();
        }
    }

    public void bringDisplayedPopupsToFront() {
        for (TPopupType popup : displayedPopups) {
            popup.toFront();
        }
    }

    public void showPopupsWaitingToBeDisplayed() {
        for (TPopupType popup : new LinkedHashSet<>(this.popupsWaitingToBeDisplayed)) {
            if (popupShouldBeDisplayed(popup)) {
                processShowPopup(popup);
            }
        }
    }

    protected abstract boolean popupShouldBeDisplayed(TPopupType popup);

    protected void processShowPopup(TPopupType popup) {
        displayedPopups.add(popup);
        popupsWaitingToBeDisplayed.remove(popup);
    }

    public AbstractScreen getAbstractScreen() {
        return abstractScreen;
    }
}
