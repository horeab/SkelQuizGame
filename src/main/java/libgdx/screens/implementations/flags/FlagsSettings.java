package libgdx.screens.implementations.flags;

import libgdx.implementations.flags.FlagsDifficultyLevel;

public class FlagsSettings {

    private FlagsDifficultyLevel flagsDifficultyLevel = FlagsDifficultyLevel._0;

    public FlagsDifficultyLevel getFlagsDifficultyLevel() {
        return flagsDifficultyLevel;
    }

    public void setFlagsDifficultyLevel(FlagsDifficultyLevel flagsDifficultyLevel) {
        this.flagsDifficultyLevel = flagsDifficultyLevel;
    }
}
