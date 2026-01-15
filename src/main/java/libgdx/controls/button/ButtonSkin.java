package libgdx.controls.button;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.utils.model.FontColor;

public interface ButtonSkin {

    Drawable getImgUp();

    Drawable getImgDown();

    Drawable getImgChecked();

    Drawable getImgDisabled();

    FontColor getButtonDisabledFontColor();
}
