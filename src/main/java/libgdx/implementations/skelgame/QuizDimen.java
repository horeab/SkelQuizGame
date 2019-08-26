package libgdx.implementations.skelgame;

import libgdx.resources.dimen.DimenUtils;

public enum QuizDimen implements libgdx.resources.dimen.Dimen {

    //BUTTONS
    width_btn_menu_normal(50),
    width_btn_screen_size(95),
    height_btn_menu_big(12),
    ////////////////////////////////////
    ;

    private float dimen;

    QuizDimen(float dimen) {
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
