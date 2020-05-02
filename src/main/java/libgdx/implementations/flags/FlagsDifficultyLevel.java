package libgdx.implementations.flags;

import libgdx.campaign.QuestionDifficulty;

public enum FlagsDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,;

    public int getIndex() {
        return ordinal();
    }

}
