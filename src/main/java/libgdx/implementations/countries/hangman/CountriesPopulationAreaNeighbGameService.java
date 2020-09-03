package libgdx.implementations.countries.hangman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.skelgame.question.Question;

public class CountriesPopulationAreaNeighbGameService extends CountriesPressedLettersGameService {

    public CountriesPopulationAreaNeighbGameService(Question question, List<String> allCountries, HashMap<Integer, List<Integer>> questionEntries, HashMap<Integer, List<String>> synonyms) {
        super(question, allCountries, questionEntries,synonyms);
    }

    @Override
    Map.Entry<Integer, List<Integer>> getQuestionsEntry(HashMap<Integer, List<Integer>> questionEntries) {
        if (question.getQuestionCategory() == CountriesCategoryEnum.cat3) {
            Map.Entry<Integer, List<Integer>> entry = null;
            for (Map.Entry<Integer, List<Integer>> e : questionEntries.entrySet()) {
                if (entry == null || entry.getValue().size() > e.getValue().size()) {
                    entry = e;
                }
            }
            questionEntries.remove(entry.getKey(),entry.getValue());
            return entry;
        }
        return super.getQuestionsEntry(questionEntries);
    }
}
