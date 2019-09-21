package libgdx.implementations.skelgame.question;

import libgdx.campaign.CampaignGame;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.utils.EnumUtils;

import java.util.Objects;

public class Question {

    private int l;
    private String y;
    private String t;
    private String s;

    public Question(int randomIdInFile, QuestionDifficulty questionDifficulty, QuestionCategory questionCategory, String questionString) {
        this.l = randomIdInFile;
        this.y = questionDifficulty.name();
        this.t = questionCategory.name();
        this.s = questionString;
    }

    public int getQuestionLineInQuestionFile() {
        return l;
    }

    public QuestionDifficulty getQuestionDifficultyLevel() {
        return (QuestionDifficulty) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum(), y);
    }

    public QuizQuestionCategory getQuestionCategory() {
        return (QuizQuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), t);
    }

    public String getQuestionString() {
        return s;
    }

    @Override
    //i questionIndexInQuestionList should not be included in the equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return l == question.l &&
                y.equals(question.y) &&
                t.equals(question.t) &&
                Objects.equals(s, question.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, y, t, s);
    }
}
