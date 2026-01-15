package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class BackButtonBuilder {

    private ButtonSkin buttonSkin = MainButtonSkin.BACK;

    public BackButtonBuilder setButtonSkin(ButtonSkin buttonSkin) {
        this.buttonSkin = buttonSkin;
        return this;
    }

    public MyButton createScreenBackButton(final AbstractScreen screen) {
        return createScreenBackButton(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.onBackKeyPress();
            }
        }, screen);
    }

    public MyButton createScreenBackButton(ButtonSkin buttonSkin, ChangeListener changeListener, final AbstractScreen screen) {
        MyButton button = new ImageButtonBuilder(buttonSkin, screen)
                .setFixedButtonSize(MainButtonSize.BACK_BUTTON)
                .build();
        button.addListener(changeListener);
        return button;
    }

    public MyButton createScreenBackButton(ChangeListener changeListener, final AbstractScreen screen) {
        return createScreenBackButton(buttonSkin, changeListener, screen);
    }

    public MyButton addHoverBackButton(AbstractScreen screen, float x, float y) {
        MyButton screenBackButton = new BackButtonBuilder().setButtonSkin(buttonSkin).createScreenBackButton(screen);
        screenBackButton.setPosition(x, y);
//        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
        screen.addActor(screenBackButton);
//        }
        return screenBackButton;
    }

    public MyButton addHoverBackButton(AbstractScreen screen) {
        return addHoverBackButton(screen, getX(), getY());
    }

    public static float getX() {
        return MainDimen.horizontal_general_margin.getDimen() * 2;
    }

    public static float getY() {
        return ScreenDimensionsManager.getScreenHeight() - MainButtonSize.BACK_BUTTON.getHeight() * 1.5f;
    }
}
