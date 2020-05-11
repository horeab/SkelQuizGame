package libgdx.implementations.animals;

import libgdx.campaign.QuestionDifficulty;

public enum AnimalsQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    ;

    public int getIndex() {
        return ordinal();
    }

}
