package libgdx.implementations.skelgame;


import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGameSpecificResource;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.paintings.PaintingsSpecificResource;
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

    ANSWER_IMAGE_CLICK(QuizGameSpecificResource.btn_question_up, QuizGameSpecificResource.btn_question_down, QuizGameSpecificResource.btn_question_up, QuizGameSpecificResource.btn_question_disabled, null),
    ANSWER_IMAGE_CLICK_CORRECT(QuizGameSpecificResource.btn_question_correct, QuizGameSpecificResource.btn_question_correct, QuizGameSpecificResource.btn_question_correct, QuizGameSpecificResource.btn_question_correct, FontColor.GREEN),
    ANSWER_IMAGE_CLICK_WRONG(QuizGameSpecificResource.btn_question_wrong, QuizGameSpecificResource.btn_question_wrong, QuizGameSpecificResource.btn_question_wrong, QuizGameSpecificResource.btn_question_wrong, FontColor.RED),

    PAINTINGS_COLOR_CATEG0(PaintingsSpecificResource.btn_categ0_up, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ0_up, PaintingsSpecificResource.btn_categ_disabled, null),
    PAINTINGS_COLOR_CATEG1(PaintingsSpecificResource.btn_categ1_up, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ1_up, PaintingsSpecificResource.btn_categ_disabled, null),
    PAINTINGS_COLOR_CATEG2(PaintingsSpecificResource.btn_categ2_up, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ2_up, PaintingsSpecificResource.btn_categ_disabled, null),
    PAINTINGS_COLOR_CATEG3(PaintingsSpecificResource.btn_categ3_up, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ3_up, PaintingsSpecificResource.btn_categ_disabled, null),
    PAINTINGS_COLOR_CATEG4(PaintingsSpecificResource.btn_categ4_up, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ4_up, PaintingsSpecificResource.btn_categ_disabled, null),

    PAINTINGS_COLOR_CATEG_FINISHED(PaintingsSpecificResource.btn_categ_finished, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ_finished, PaintingsSpecificResource.btn_categ_disabled, null),
    PAINTINGS_COLOR_CATEG_STAR(PaintingsSpecificResource.btn_categ_star, PaintingsSpecificResource.btn_categ_down, PaintingsSpecificResource.btn_categ_star, PaintingsSpecificResource.btn_categ_disabled, null),
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
