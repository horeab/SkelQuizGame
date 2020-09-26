package libgdx.implementations.screens.implementations.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import libgdx.implementations.skelgame.gameservice.UniqueAnswersQuizGameService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.MainGameLabel;

public class HistoryGameService extends UniqueAnswersQuizGameService {

    public HistoryGameService(Question question) {
        super(question);
    }

    @Override
    public List<String> getAnswers() {
        String[] answersArray = question.getQuestionString().split(":")[1].split(",");
        return new ArrayList<>(Arrays.asList(answersArray));
    }

    public static String getOptionText(String s) {
        int nr = getOptionRawText(s);
        String val = Math.abs(nr) > 9999 ? formatNrToCurrency(Math.abs(nr)) : String.valueOf(Math.abs(nr));
        val = nr < 0 ? MainGameLabel.l_bc_year.getText(val) : val;
        return val;
    }

    public static int getOptionRawText(String s) {
        return Integer.parseInt(s.split(":")[1]);
    }

    private static String formatNrToCurrency(int nr) {
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

    public static String getQuestionText(String s) {
        return s.split(":")[0];
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