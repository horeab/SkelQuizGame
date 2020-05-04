package libgdx.implementations.skelgame.question;

import org.apache.commons.lang3.StringUtils;

import libgdx.campaign.CampaignGame;
import libgdx.campaign.QuestionDifficulty;
import libgdx.game.Game;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.CreatorDependenciesContainer;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionParser {

    public List<String> getAnswers(String questionString) {
        try {
            return Arrays.asList(questionString.split(":")[2]);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<String> getAllAnswerOptions(Question question) {
        List<String> answerOptions = new ArrayList<>(getAnswers(question.getQuestionString()));
        List<Question> allQuestions = getAllQuestions(question.getQuestionDifficultyLevel(), question.getQuestionCategory());
        for (String fileId : new QuestionParser().getAnswerIds(question.getQuestionString(), 3)) {
            try {
                answerOptions.addAll(getAnswers(getQuestionForFileId(fileId, allQuestions).getQuestionString()));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        Collections.shuffle(answerOptions);
        List<String> result = new ArrayList<>();
        for (String answer : answerOptions) {
            result.add(StringUtils.capitalize(answer));
        }
        return result;
    }

    public List<Question> getAllQuestions(QuestionDifficulty questionDifficultyLevel, QuizQuestionCategory questionCategory) {
        return CreatorDependenciesContainer.getCreator(questionCategory.getCreatorDependencies())
                .getQuestionCreator(questionDifficultyLevel, questionCategory)
                .getAllQuestions(Arrays.asList((QuestionDifficulty[])
                                EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())),
                        questionCategory);
    }

    private Question getQuestionForFileId(String fileId, List<Question> allQuestions) {
        for (Question question : allQuestions) {
            if (question.getQuestionString().split(":")[0].equals(fileId)) {
                return question;
            }
        }
        return null;
    }

    private List<String> getAnswerIds(String questionString, int position) {
        String[] answers = questionString.split(":")[position].split(",");
        List<String> toReturn = new ArrayList<>();
        for (String answer : answers) {
            toReturn.add(answer.trim());
        }
        return toReturn;
    }
}
