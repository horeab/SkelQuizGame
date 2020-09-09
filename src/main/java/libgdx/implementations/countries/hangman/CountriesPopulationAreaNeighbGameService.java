package libgdx.implementations.countries.hangman;

import java.util.List;
import java.util.Map;

import libgdx.implementations.skelgame.question.Question;

public class CountriesPopulationAreaNeighbGameService extends CountriesPressedLettersGameService {

    public CountriesPopulationAreaNeighbGameService(Question question, List<String> allCountries, Map<Integer, List<Integer>> questionEntries, Map<Integer, List<String>> synonyms) {
        super(question, allCountries, questionEntries, synonyms);
    }

}
