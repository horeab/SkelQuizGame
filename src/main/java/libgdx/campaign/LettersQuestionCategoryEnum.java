package libgdx.campaign;

public enum LettersQuestionCategoryEnum implements QuestionCategory {

    CAT0,
    CAT1,
    CAT2,
    CAT3,
    CAT4,
    ;

    @Override
    public int getIndex() {
        return ordinal();
    }

}
