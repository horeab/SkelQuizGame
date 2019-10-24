package libgdx.implementations.skelgame.gameservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import libgdx.implementations.skelgame.question.Question;

//id:question:answer_options:answer:image
//0:Question Question Question?:Answer 1##Answer 2##Answer 3##Answer 4##Answer 5:0,2:3
public class UniqueAnswersQuizGameService extends QuizGameService {

    public UniqueAnswersQuizGameService(Question question) {
        super(question);
    }

    @Override
    public List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        String[] answersIdsArray = question.getQuestionString().split(":")[3].split(",");
        String[] answerOptionsArray = getAnswerOptionsArray();
        for (String answerId : answersIdsArray) {
            answers.add(answerOptionsArray[Integer.parseInt(answerId)]);
        }
        Collections.shuffle(answers);
        return answers;
    }

    @Override
    public String getRandomUnpressedAnswerFromQuestion(Set<String> answersIds) {
        return getUnpressedCorrectAnswers(answersIds).get(0);
    }

    @Override
    public List<String> getAllAnswerOptions() {
        List<String> answerOptions = new ArrayList<>();
        for (String answer : getAnswerOptionsArray()) {
            answerOptions.add(answer.trim());
        }
        Collections.shuffle(answerOptions);
        return answerOptions;
    }

    private String[] getAnswerOptionsArray() {
        return question.getQuestionString().split(":")[2].split("##");
    }
}
