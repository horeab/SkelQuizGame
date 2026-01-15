package libgdx.implementations.anatomy;

import libgdx.campaign.QuestionDifficulty;

public enum AnatomyQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3,
    _4,;

    public int getIndex() {
        return ordinal();
    }

}
