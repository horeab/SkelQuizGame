package libgdx.implementations.hangmanarena;

import libgdx.campaign.QuestionDifficulty;

public enum HangmanArenaQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3,
    _4,
    _5;

    public int getIndex() {
        return ordinal();
    }

}
