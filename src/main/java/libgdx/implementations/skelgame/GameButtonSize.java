package libgdx.implementations.skelgame;

public enum GameButtonSize implements libgdx.controls.button.ButtonSize {

    SQUARE_QUIZ_OPTION_ANSWER(GameDimen.width_btn_menu_normal.getDimen() / 1.1f, GameDimen.height_btn_menu_big.getDimen() * 1.5f),
    LONG_QUIZ_OPTION_ANSWER(GameDimen.width_btn_screen_size.getDimen(), GameDimen.height_btn_menu_big.getDimen() / 1.1f),
    HANGMAN_BUTTON(GameDimen.width_hangman_button.getDimen(), GameDimen.height_hangman_button.getDimen()),
    HANGMAN_SMALL_BUTTON(GameDimen.width_hangman_button.getDimen() / 1.33f, GameDimen.height_hangman_button.getDimen()),
    ;
    private float width;
    private float height;

    GameButtonSize(float width, float height) {
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
