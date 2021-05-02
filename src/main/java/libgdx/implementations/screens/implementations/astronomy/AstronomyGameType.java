package libgdx.implementations.screens.implementations.astronomy;

public enum AstronomyGameType {

    SOLAR_SYSTEM("astronomy_game_type_0"),
    ASTRONOMY_QUIZ("astronomy_game_type_1"),
    ASTRONOMY_IMAGES_QUIZ("astronomy_game_type_2");

    String levelName;

    AstronomyGameType(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
}
