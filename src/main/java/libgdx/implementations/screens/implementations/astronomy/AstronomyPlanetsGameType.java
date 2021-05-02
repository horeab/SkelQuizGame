package libgdx.implementations.screens.implementations.astronomy;

public enum AstronomyPlanetsGameType {

    RADIUS("astronomy_planet_game_type_0"),
    GRAVITY("astronomy_planet_game_type_1"),
    SUN_LIGHT_DURATION("astronomy_planet_game_type_2"),
    MASS("astronomy_planet_game_type_3"),
    ORBITAL_PERIOD("astronomy_planet_game_type_4"),
    MEAN_TEMPERATURE("astronomy_planet_game_type_5"),
    ;

    String levelName;

    AstronomyPlanetsGameType(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
}
