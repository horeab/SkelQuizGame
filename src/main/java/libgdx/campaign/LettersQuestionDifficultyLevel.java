package libgdx.campaign;

public enum LettersQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2;

    public int getIndex() {
        return ordinal();
    }

}
