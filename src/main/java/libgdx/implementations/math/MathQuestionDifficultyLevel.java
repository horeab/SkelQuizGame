package libgdx.implementations.math;

import libgdx.campaign.QuestionDifficulty;

public enum MathQuestionDifficultyLevel implements QuestionDifficulty {

    _0,;

    public int getIndex() {
        return ordinal();
    }

}
