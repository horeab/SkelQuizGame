package libgdx.controls.button;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.constants.Contrast;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.model.FontColor;

public enum MainButtonSkin implements ButtonSkin {

    DEFAULT(MainResource.btn_menu_up, MainResource.btn_menu_down, MainResource.btn_menu_up, MainResource.btn_lowcolor_up, null),
    BACK(getBackBtnUp(), getBackBtnDown(), getBackBtnUp(), getBackBtnUp(), null),
    BACK_LIGHT_CONTRAST(MainResource.btn_back_up, MainResource.btn_back_down, MainResource.btn_back_up, MainResource.btn_back_up, null),
    REFRESH(MainResource.refresh_up, MainResource.refresh_down, MainResource.refresh_up, MainResource.refresh_up, null),
    UNLOCK_EXTRA_CONTENT(MainResource.unlock, MainResource.unlock, MainResource.unlock, MainResource.unlock, null),
    SOUND_ON(MainResource.sound_on, MainResource.sound_on, MainResource.sound_on, MainResource.sound_on, null),
    MUSIC_ON(MainResource.music_on, MainResource.music_on, MainResource.music_on, MainResource.music_on, null),
    SOUND_OFF(MainResource.sound_off, MainResource.sound_off, MainResource.sound_off, MainResource.sound_off, null),
    SETTINGS(MainResource.btn_settings_up, MainResource.btn_settings_down, MainResource.btn_settings_up, MainResource.btn_settings_up, null),
    MUSIC_OFF(MainResource.music_off, MainResource.music_off, MainResource.music_off, MainResource.music_off, null),
    TRANSPARENT(MainResource.transparent_background, MainResource.transparent_background, MainResource.transparent_background, MainResource.transparent_background, null),
    LOW_COLOR(MainResource.btn_lowcolor_up, MainResource.btn_lowcolor_down, MainResource.btn_lowcolor_up, MainResource.btn_lowcolor_up, null),
    ;

    private static MainResource getBackBtnUp() {
        return Game.getInstance().getSubGameDependencyManager().getScreenContrast() == Contrast.LIGHT ? MainResource.btn_back_up : MainResource.btn_back_up_white;
    }

    private static MainResource getBackBtnDown() {
        return Game.getInstance().getSubGameDependencyManager().getScreenContrast() == Contrast.LIGHT ? MainResource.btn_back_down : MainResource.btn_back_down_white;
    }

    MainButtonSkin(Res imgUp, Res imgDown, Res imgChecked, Res imgDisabled, FontColor buttonDisabledFontColor) {
        this.imgUp = imgUp;
        this.imgDown = imgDown;
        this.imgChecked = imgChecked;
        this.imgDisabled = imgDisabled;
        this.buttonDisabledFontColor = buttonDisabledFontColor;
    }

    private Res imgUp;
    private Res imgDown;
    private Res imgChecked;
    private Res imgDisabled;
    private FontColor buttonDisabledFontColor;

    @Override
    public Drawable getImgUp() {
        return GraphicUtils.getImage(imgUp).getDrawable();
    }

    @Override
    public Drawable getImgDown() {
        return GraphicUtils.getImage(imgDown).getDrawable();
    }

    @Override
    public Drawable getImgChecked() {
        return GraphicUtils.getImage(imgChecked).getDrawable();
    }

    @Override
    public Drawable getImgDisabled() {
        return GraphicUtils.getImage(imgDisabled).getDrawable();
    }

    @Override
    public FontColor getButtonDisabledFontColor() {
        return buttonDisabledFontColor;
    }
}
