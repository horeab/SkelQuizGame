package libgdx.implementations.skelgame;

import libgdx.resources.dimen.MainDimen;

public enum GameButtonSize implements libgdx.controls.button.ButtonSize {

    CAMPAIGN_LEVEL_ROUND_IMAGE(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),
    SQUARE_QUIZ_OPTION_ANSWER(GameDimen.width_btn_menu_normal.getDimen() / 1.1f, GameDimen.height_btn_menu_big.getDimen() * 1.5f),
    LONG_QUIZ_OPTION_ANSWER(GameDimen.width_btn_screen_size.getDimen(), GameDimen.height_btn_menu_big.getDimen() / 1.1f),
    HANGMAN_BUTTON(GameDimen.width_hangman_button.getDimen(), GameDimen.height_hangman_button.getDimen()),
    HANGMAN_SMALL_BUTTON(GameDimen.width_hangman_button.getDimen() / 1.33f, GameDimen.height_hangman_button.getDimen()),
    ASTRONOMY_MENU_BUTTON(GameDimen.width_btn_menu_normal.getDimen() / 1.9f, GameDimen.width_btn_menu_normal.getDimen() / 1.9f),
    BIG_MENU_ROUND_IMAGE(MainDimen.side_btn_image.getDimen() * 2.5f, MainDimen.side_btn_image.getDimen() * 2.5f),
    NORMAL_MENU_ROUND_IMAGE(MainDimen.side_btn_image.getDimen() * 1.7f, MainDimen.side_btn_image.getDimen() * 1.7f),
    PERIODICTABLE_MENU_BUTTON(MainDimen.horizontal_general_margin.getDimen() * 5, MainDimen.horizontal_general_margin.getDimen() * 5),
    PERIODICTABLE_ANSWER_BUTTON(MainDimen.horizontal_general_margin.getDimen() * 15, MainDimen.horizontal_general_margin.getDimen() * 5),
    IMAGE_CLICK_ANSWER_OPTION(MainDimen.side_btn_image.getDimen() / 1.1f, MainDimen.side_btn_image.getDimen() / 1.1f),
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
