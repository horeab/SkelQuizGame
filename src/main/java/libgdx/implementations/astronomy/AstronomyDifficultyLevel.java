package libgdx.implementations.astronomy;

import libgdx.campaign.QuestionDifficulty;

public enum AstronomyDifficultyLevel implements QuestionDifficulty {

    _0,;

    public int getIndex() {
        return ordinal();
    }

}
