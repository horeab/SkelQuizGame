package libgdx.implementations.geoquiz;

import libgdx.campaign.QuestionDifficulty;

public enum QuizQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3;

    public int getIndex() {
        return ordinal();
    }

}
