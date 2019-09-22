package libgdx.implementations.skelgame;

import libgdx.resources.dimen.DimenUtils;

public enum GameDimen implements libgdx.resources.dimen.Dimen {

    //BUTTONS
    width_btn_menu_normal(50),
    width_btn_screen_size(95),
    height_btn_menu_big(12),

    width_hangman_button(15.5f),
    height_hangman_button(7f),
    width_hangman_letter(9f),
    side_hangman_image(33),
    ////////////////////////////////////
    ;

    private float dimen;

    GameDimen(float dimen) {
        this.dimen = dimen;
    }

    @Override
    public float getDimen() {
        return DimenUtils.getDimen(this);
    }

    @Override
    public int getIntegerValueOfDimen() {
        return DimenUtils.getIntegerValueOfDimen(this);
    }

    @Override
    public float getRawDimen() {
        return dimen;
    }
}
