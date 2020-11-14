package libgdx.implementations.countries.hangman;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.Question;

public class CountriesAtoZGameService extends CountriesPressedLettersGameService {

    public CountriesAtoZGameService(Question question, List<String> allCountries, Map<Integer, List<Integer>> questionEntries, Map<Integer, List<String>> synonyms) {
        super(question, allCountries, questionEntries, new LinkedHashMap<>());
    }

    @Override
    public List<String> getPressedCorrectAnswers(ArrayList<GameAnswerInfo> pressedAnswers, List<String> foundCountries) {
        List<String> possibleAnswers = getPossibleAnswers();
        List<String> foundCountriesWithSameFirstLetter = new ArrayList<>();
        for (String foundCountry : foundCountries) {
            for (String possibleAnswer : possibleAnswers) {
                if (possibleAnswer.substring(0, 1).toLowerCase().equals(foundCountry.substring(0, 1).toLowerCase())) {
                    foundCountriesWithSameFirstLetter.add(possibleAnswer);
                }
            }
        }
        return super.getPressedCorrectAnswers(pressedAnswers, foundCountriesWithSameFirstLetter);
    }

    public List<String> getStartingLettersOfCountries() {
        Set<String> firstLetters = new LinkedHashSet<>();
        for (String country : getPossibleAnswers()) {
            firstLetters.add(country.substring(0, 1));
        }
        ArrayList<String> list = new ArrayList<>(firstLetters);
        java.util.Collections.sort(list);
        return list.size() > 24 ? list.subList(0, 24) : list;
    }

}
