package libgdx.implementations.skelgame;


import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGameSpecificResource;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.model.FontColor;

public enum GameButtonSkin implements libgdx.controls.button.ButtonSkin {

    SQUARE_ANSWER_OPTION_CORRECT(QuizGameSpecificResource.btn_answer_opt_correct, QuizGameSpecificResource.btn_answer_opt_correct, QuizGameSpecificResource.btn_answer_opt_correct, QuizGameSpecificResource.btn_answer_opt_correct, FontColor.GREEN),
    SQUARE_ANSWER_OPTION_WRONG(QuizGameSpecificResource.btn_answer_opt_wrong, QuizGameSpecificResource.btn_answer_opt_wrong, QuizGameSpecificResource.btn_answer_opt_wrong, QuizGameSpecificResource.btn_answer_opt_wrong, FontColor.RED),
    LONG_ANSWER_OPTION_CORRECT(QuizGameSpecificResource.btn_long_answer_opt_correct, QuizGameSpecificResource.btn_long_answer_opt_correct, QuizGameSpecificResource.btn_long_answer_opt_correct, QuizGameSpecificResource.btn_long_answer_opt_correct, FontColor.GREEN),
    LONG_ANSWER_OPTION_WRONG(QuizGameSpecificResource.btn_long_answer_opt_wrong, QuizGameSpecificResource.btn_long_answer_opt_wrong, QuizGameSpecificResource.btn_long_answer_opt_wrong, QuizGameSpecificResource.btn_long_answer_opt_wrong, FontColor.RED),
    SQUARE_ANSWER_OPTION(QuizGameSpecificResource.btn_answer_opt_up, QuizGameSpecificResource.btn_answer_opt_down, QuizGameSpecificResource.btn_answer_opt_up, QuizGameSpecificResource.btn_answer_opt_disabled, null),
    LONG_ANSWER_OPTION(QuizGameSpecificResource.btn_long_answer_opt_up, QuizGameSpecificResource.btn_long_answer_opt_down, QuizGameSpecificResource.btn_long_answer_opt_up, QuizGameSpecificResource.btn_long_answer_opt_disabled, null),
    HINT(QuizGameSpecificResource.btn_hint, QuizGameSpecificResource.btn_hint, QuizGameSpecificResource.btn_hint, QuizGameSpecificResource.btn_hint_disabled, null),
    SQUARE(MainResource.btn_menu_up, MainResource.btn_menu_down, MainResource.btn_menu_up, MainResource.btn_lowcolor_down, null),
    SQUARE_CORRECT(MainResource.btn_menu_up, MainResource.btn_menu_down, MainResource.btn_menu_up, MainResource.btn_lowcolor_down, null),
    SQUARE_WRONG(MainResource.btn_menu_up, MainResource.btn_menu_down, MainResource.btn_menu_up, MainResource.btn_lowcolor_down, null),
    ;

    GameButtonSkin(Res imgUp, Res imgDown, Res imgChecked, Res imgDisabled, FontColor buttonDisabledFontColor) {
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
