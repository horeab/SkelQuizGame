package libgdx.controls.popup;

import libgdx.screen.AbstractScreen;

public interface Popup {

    PopupManager getPopupManager();

    Popup addToPopupManager();

    void hide();

    AbstractScreen getScreen();

    void onBackKeyPress();
}
