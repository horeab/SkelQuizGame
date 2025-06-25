package libgdx.implementations.countries;

import libgdx.campaign.QuestionDifficulty;

public enum CountriesDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3,
    _4,
    _5,
    _6,
    ;

    public int getIndex() {
        return ordinal();
    }

}
