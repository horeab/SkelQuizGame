package libgdx.campaign;

public class RandomCategoryAndDifficulty {

    private QuestionCategory questionCategory;
    private QuestionDifficulty questionDifficulty;

    public RandomCategoryAndDifficulty(QuestionCategory questionCategory, QuestionDifficulty questionDifficulty) {
        this.questionCategory = questionCategory;
        this.questionDifficulty = questionDifficulty;
    }

    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }

    public QuestionDifficulty getQuestionDifficulty() {
        return questionDifficulty;
    }
}
