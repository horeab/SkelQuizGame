package libgdx.implementations.screens.implementations.astronomy;

public enum PlanetsGameType {

    MEAN_TEMPERATURE("Average Temperature"),
    RADIUS("Planet radius compared to Earth"),
    SUN_LIGHT_DURATION("Distance from Sun"),
    MASS("Planet mass compared to Earth"),
    GRAVITY("How much does 1 kg weigh on this planet"),
    ORBITAL_PERIOD("Orbital Period");

    String levelName;

    PlanetsGameType(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
}
