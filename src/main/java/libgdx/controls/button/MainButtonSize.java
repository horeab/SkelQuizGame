package libgdx.controls.button;

import libgdx.game.Game;
import libgdx.resources.dimen.MainDimen;

public enum MainButtonSize implements ButtonSize {

    STANDARD_IMAGE(MainDimen.side_btn_image.getDimen(), MainDimen.side_btn_image.getDimen()),
    BACK_BUTTON(MainDimen.side_btn_back.getDimen() *
            (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f),
            MainDimen.side_btn_back.getDimen() *
                    (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f)),
    SOUND_BUTTON(MainDimen.side_btn_image.getDimen() * (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f),
            MainDimen.side_btn_image.getDimen() * (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f)),
    SETTINGS_BUTTON(MainDimen.side_btn_image.getDimen() * (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f),
            MainDimen.side_btn_image.getDimen() * (Game.getInstance().getAppInfoService().isPortraitMode() ? 1 : 1.5f)),
    UNLOCK_CONTENT_BUTTON(MainDimen.side_btn_unlock_content.getDimen(), MainDimen.side_btn_unlock_content.getDimen()),

    ONE_ROW_BUTTON_SIZE(MainDimen.width_default_btn.getDimen(), MainDimen.height_default_btn.getDimen()),
    TWO_ROW_BUTTON_SIZE(MainDimen.width_default_btn.getDimen(), MainDimen.height_default_btn.getDimen() * 1.5f),
    THREE_ROW_BUTTON_SIZE(MainDimen.width_default_btn.getDimen(), MainDimen.height_default_btn.getDimen() * 2),
    ;

    private float width;
    private float height;

    MainButtonSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
