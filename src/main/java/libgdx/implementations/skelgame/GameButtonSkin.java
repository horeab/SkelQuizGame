package libgdx.implementations.skelgame;


import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGameSpecificResource;
import libgdx.implementations.hangman.HangmanSpecificResource;
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

    HANGMAN_HINT(HangmanSpecificResource.btn_hint, HangmanSpecificResource.btn_hint, HangmanSpecificResource.btn_hint, HangmanSpecificResource.btn_hint_disabled, null),
    HANGMAN_MENU(HangmanSpecificResource.btn_menu_up, HangmanSpecificResource.btn_menu_down, HangmanSpecificResource.btn_menu_up, HangmanSpecificResource.btn_menu_up, null),
    HANGMAN_CATEG(HangmanSpecificResource.btn_categ_up, HangmanSpecificResource.btn_categ_down, HangmanSpecificResource.btn_categ_up, HangmanSpecificResource.btn_categ_disabled, null),
    SQUARE(HangmanSpecificResource.btn_hangman_up, HangmanSpecificResource.btn_hangman_down, HangmanSpecificResource.btn_hangman_up, MainResource.btn_lowcolor_down, null),
    SQUARE_CORRECT(HangmanSpecificResource.btn_hangman_correct, HangmanSpecificResource.btn_hangman_correct, HangmanSpecificResource.btn_hangman_correct, HangmanSpecificResource.btn_hangman_correct, null),
    SQUARE_WRONG(HangmanSpecificResource.btn_hangman_wrong, HangmanSpecificResource.btn_hangman_wrong, HangmanSpecificResource.btn_hangman_wrong, HangmanSpecificResource.btn_hangman_wrong, null),
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
