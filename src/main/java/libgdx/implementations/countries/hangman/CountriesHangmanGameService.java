package libgdx.implementations.countries.hangman;

import java.util.List;

import libgdx.implementations.skelgame.question.Question;

public class CountriesHangmanGameService extends CountriesPressedLettersGameService {

    public CountriesHangmanGameService(Question question, List<String> possibleAnswers) {
        super(question, possibleAnswers);
    }

}
