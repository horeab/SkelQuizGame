package libgdx.implementations.screens.implementations.countries;

import libgdx.implementations.flags.FlagsDifficultyLevel;

public class CountriesSettings {

    private FlagsDifficultyLevel flagsDifficultyLevel = FlagsDifficultyLevel._0;

    public FlagsDifficultyLevel getFlagsDifficultyLevel() {
        return flagsDifficultyLevel;
    }

    public void setFlagsDifficultyLevel(FlagsDifficultyLevel flagsDifficultyLevel) {
        this.flagsDifficultyLevel = flagsDifficultyLevel;
    }
}
