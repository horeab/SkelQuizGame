package libgdx.implementations.skelgame;

public enum QuizGameButtonSize implements libgdx.controls.button.ButtonSize {

    SQUARE_QUIZ_OPTION_ANSWER(QuizDimen.width_btn_menu_normal.getDimen() / 1.1f, QuizDimen.height_btn_menu_big.getDimen() * 1.5f),
    LONG_QUIZ_OPTION_ANSWER(QuizDimen.width_btn_screen_size.getDimen(), QuizDimen.height_btn_menu_big.getDimen() / 1.1f),
    ;
    private float width;
    private float height;

    QuizGameButtonSize(float width, float height) {
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
