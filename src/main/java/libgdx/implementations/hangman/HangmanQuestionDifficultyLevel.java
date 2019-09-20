package libgdx.implementations.hangman;

import libgdx.campaign.QuestionDifficulty;

public enum HangmanQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3;

    public int getIndex() {
        return ordinal();
    }

}
