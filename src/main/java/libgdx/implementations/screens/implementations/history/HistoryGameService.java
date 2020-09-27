package libgdx.implementations.screens.implementations.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import libgdx.implementations.skelgame.gameservice.UniqueAnswersQuizGameService;
import libgdx.implementations.skelgame.question.Question;

public abstract class HistoryGameService extends UniqueAnswersQuizGameService {

    public HistoryGameService(Question question) {
        super(question);
    }

    @Override
    public List<String> getAnswers() {
        String[] answersArray = question.getQuestionString().split(":")[1].split(",");
        return new ArrayList<>(Arrays.asList(answersArray));
    }

    public abstract int getSortYear(String s);

    public String getQuestionText(String s) {
        return s.split(":")[0];
    }

    protected String formatNrToCurrency(int nr) {
        char[] nrArray = Integer.toString(nr).toCharArray();
        int j = 0;
        String result = Integer.toString(nr);
        if (nrArray.length > 3) {
            result = "";
            for (int i = nrArray.length - 1; i >= 0; i--) {
                result = result + nrArray[i];
                if (j == 2) {
                    result = result + ".";
                    j = 0;
                } else {
                    j++;
                }
            }
            result = new StringBuffer(result).reverse().toString();
        }
        if (result.toCharArray()[0] == '.') {
            result = result.substring(1);
        }
        return result;
    }

    @Override
    public String getRandomUnpressedAnswerFromQuestion(Set<String> answersIds) {
        return getUnpressedCorrectAnswers(answersIds).get(0);
    }

    @Override
    public List<String> getAllAnswerOptions() {
        return Collections.emptyList();
    }

}