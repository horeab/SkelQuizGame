package libgdx.implementations.history;

import libgdx.campaign.QuestionDifficulty;

public enum HistoryDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,
    _3,;

    public int getIndex() {
        return ordinal();
    }

}
