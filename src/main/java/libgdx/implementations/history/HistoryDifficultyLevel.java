package libgdx.implementations.history;

import libgdx.campaign.QuestionDifficulty;

public enum HistoryDifficultyLevel implements QuestionDifficulty {

    _0,;

    public int getIndex() {
        return ordinal();
    }

}
