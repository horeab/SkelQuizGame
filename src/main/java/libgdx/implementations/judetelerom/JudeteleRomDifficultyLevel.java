package libgdx.implementations.judetelerom;

import libgdx.campaign.QuestionDifficulty;

public enum JudeteleRomDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,;

    public int getIndex() {
        return ordinal();
    }

}
