package libgdx.implementations.countries;

import libgdx.campaign.QuestionDifficulty;

public enum CountriesDifficultyLevel implements QuestionDifficulty {

    _0,
    ;

    public int getIndex() {
        return ordinal();
    }

}
