package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class RefreshButtonBuilder {

    private MyButton createScreenRefreshButton(ButtonSkin buttonSkin, ChangeListener changeListener, final AbstractScreen screen) {
        MyButton button = new ImageButtonBuilder(buttonSkin, screen)
                .setFixedButtonSize(MainButtonSize.BACK_BUTTON)
                .build();
        button.addListener(changeListener);
        return button;
    }

    public MyButton addHoverRefreshButton(AbstractScreen screen, ChangeListener changeListener, float x) {
        MyButton refresh = new RefreshButtonBuilder().createScreenRefreshButton(MainButtonSkin.REFRESH, changeListener, screen);
        refresh.setPosition(x,
                ScreenDimensionsManager.getScreenHeight() - MainButtonSize.BACK_BUTTON.getHeight());
        screen.addActor(refresh);
        return refresh;
    }
}
