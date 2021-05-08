package libgdx.implementations.screens.implementations.astronomy;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.skelgame.GameButtonSkin;

public enum AstronomyGameType {

    FIND_PLANET("astronomy_question_category_0", GameButtonSkin.ASTRONOMY_GAME_TYPE_FIND_PLANET),
    SOLAR_SYSTEM("astronomy_game_type_solar_system", GameButtonSkin.ASTRONOMY_GAME_TYPE_SOLAR_SYSTEM),
    ASTRONOMY_QUIZ("astronomy_game_type_astronomy_quiz", GameButtonSkin.ASTRONOMY_GAME_TYPE_ASTRONOMY_QUIZ),
    ASTRONOMY_IMAGES_QUIZ("astronomy_game_type_astronomy_images_quiz", GameButtonSkin.ASTRONOMY_GAME_TYPE_ASTRONOMY_IMAGES_QUIZ);

    String levelName;
    ButtonSkin buttonSkin;

    AstronomyGameType(String levelName, ButtonSkin buttonSkin) {
        this.levelName = levelName;
        this.buttonSkin = buttonSkin;
    }
}
