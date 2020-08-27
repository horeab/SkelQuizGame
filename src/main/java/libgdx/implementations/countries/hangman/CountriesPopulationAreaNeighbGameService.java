package libgdx.implementations.countries.hangman;

import java.util.HashMap;
import java.util.List;

import libgdx.implementations.skelgame.question.Question;

public class CountriesPopulationAreaNeighbGameService extends CountriesPressedLettersGameService {

    public CountriesPopulationAreaNeighbGameService(Question question, List<String> allCountries, HashMap<Integer, List<String>> possibleAnswers) {
        super(question, allCountries, possibleAnswers);
    }

}
