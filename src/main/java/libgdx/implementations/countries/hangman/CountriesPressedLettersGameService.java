package libgdx.implementations.countries.hangman;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import libgdx.game.Game;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public class CountriesPressedLettersGameService extends GameService {

    public static final String STANDARD_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final int GAME_OVER_WRONG_LETTERS = 6;
    private static final List<String> CHARS_TO_BE_IGNORED = Arrays.asList(" ", "-", "'");
    private Set<String> normalizedWordLetters = new HashSet<>();
    private String availableLetters;
    private List<String> possibleAnswers = new ArrayList<>();
    private List<String> possibleAnswersRaw = new ArrayList<>();
    private List<String> allCountries;
    private List<String> possibleAnswersLowerCase = new ArrayList<>();
    private Integer possAnswersLine;
    HashMap<Integer, List<String>> questionEntries;

    public CountriesPressedLettersGameService(Question question, List<String> allCountries, HashMap<Integer, List<String>> questionEntries) {
        super(question);
        this.allCountries = allCountries;
        Map.Entry<Integer, List<String>> firstEntry = getQuestionsEntry(questionEntries);
        possAnswersLine = firstEntry.getKey();
        this.questionEntries = questionEntries;
        populatePossibleAnswersFromIndexes(firstEntry.getValue());
        availableLetters = SpecificPropertiesUtils.getText(Game.getInstance().getAppInfoService().getLanguage() + "_" + Game.getInstance().getGameIdPrefix() + "_available_letters");
        for (String answer : this.possibleAnswers) {
            normalizedWordLetters.addAll(getNormalizedWordLetters(answer));
            possibleAnswersLowerCase.add(answer.toLowerCase());
        }
    }

    public List<String> getAllCountries() {
        return allCountries;
    }

    public HashMap<Integer, List<String>> getQuestionEntries() {
        return questionEntries;
    }

    Map.Entry<Integer, List<String>> getQuestionsEntry(HashMap<Integer, List<String>> questionEntries) {
        return questionEntries.entrySet().iterator().next();
    }

    private void populatePossibleAnswersFromIndexes(List<String> indexes) {
        for (String index : indexes) {
            String countryName = getCountryName(Integer.parseInt(index));
            possibleAnswers.add(countryName);
        }
        possibleAnswersRaw = new ArrayList<>(possibleAnswers);
        if (question.getQuestionCategory() == CountriesCategoryEnum.cat2) {
            Collections.sort(possibleAnswers);
        }
    }

    public List<String> getPossibleAnswersRaw() {
        return possibleAnswersRaw;
    }

    public Integer getQuestionIndex() {
        return possAnswersLine;
    }

    public String getCountryName(int index) {
        return allCountries.get(index - 1);
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public List<String> getPossibleAnswersLowerCase() {
        return possibleAnswersLowerCase;
    }

    @Override
    public boolean isAnswerCorrectInQuestion(String answer) {
        return true;
    }

    public List<String> getPressedCorrectAnswers(ArrayList<GameAnswerInfo> pressedAnswers, List<String> foundCountries) {
        String pressedAnswer = getPressedAnswers(pressedAnswers);
        List<String> correctAnswers = new ArrayList<>();
        List<String> possibleAnswersList = new ArrayList<>(possibleAnswers);
        possibleAnswersList.removeAll(foundCountries);
        for (String possibleAnswer : possibleAnswersList) {
            String posAnsw = possibleAnswer.toLowerCase();
            for (String charToBeIgnored : CHARS_TO_BE_IGNORED) {
                posAnsw = posAnsw.replace(charToBeIgnored, "");
            }
            if (posAnsw.startsWith(pressedAnswer)) {
                correctAnswers.add(possibleAnswer);
            }
        }
        return correctAnswers;
    }

    private boolean isLetterCorrectInWord(String hangmanWord, String answer) {
        return compareAnswerStrings(hangmanWord, answer);
    }

    @Override
    protected int getQuestionToBeDisplayedPositionInString() {
        return 1;
    }

    @Override
    public int getImageToBeDisplayedPositionInString() {
        return 3;
    }

    private String processHangmanString(String string) {
        return !availableLettersHaveSpecialCharacters() ? normalize(string) : string;
    }

    @Override
    public boolean isGameFinishedSuccessful(Set<String> answersIds) {
        return answersIds.containsAll(normalizedWordLetters);
    }

    @Override
    public List<String> getAllAnswerOptions() {
        return new ArrayList<>(Arrays.asList(getAvailableLetters().split(",")));
    }

    @Override
    public int getNrOfWrongAnswersPressed(Set<String> stringSet) {
        Set<String> answerIds = new HashSet<>(stringSet);
        answerIds.removeAll(normalizedWordLetters);
        return answerIds.size();
    }

    public String getPressedAnswers(ArrayList<GameAnswerInfo> pressedAnswers) {
        StringBuilder answers = new StringBuilder();
        for (GameAnswerInfo answer : pressedAnswers) {
            answers.append(answer.getAnswer());
        }
        return answers.toString();
    }

    @Override
    public boolean isGameFinishedFailed(Set<String> answersIds) {
        return getNrOfWrongAnswersPressed(answersIds) >= GAME_OVER_WRONG_LETTERS;
    }

    public Set<String> getNormalizedWordLetters() {
        return normalizedWordLetters;
    }

    public Set<String> getWordLetters(String hangmanWord) {
        hangmanWord = hangmanWord.toLowerCase();
        if (StringUtils.indexOfAny(hangmanWord, (String[]) CHARS_TO_BE_IGNORED.toArray()) != -1) {
            for (String charToBeIgnored : CHARS_TO_BE_IGNORED) {
                hangmanWord = hangmanWord.replace(charToBeIgnored, "");
            }
        }
        Set<String> result = new HashSet<String>();
        for (char c : hangmanWord.toCharArray()) {
            result.add(Character.toString(c));
        }
        return result;
    }

    public Set<String> getNormalizedWordLetters(String hangmanWord) {
        return getWordLetters(processHangmanString(hangmanWord));
    }

    @Override
    public List<String> getUnpressedCorrectAnswers(Set<String> answerIds) {
        return getUnpressedCorrectAnswers(answerIds, normalizedWordLetters);
    }

    public List<String> getUnpressedCorrectAnswers(Set<String> answerIds, Set<String> wordLetters) {
        List<String> correctWordLetters = new ArrayList<>(wordLetters);
        Collections.shuffle(correctWordLetters);
        for (String letter : new ArrayList<>(correctWordLetters)) {
            for (String answer : answerIds) {
                if (compareAnswerStrings(letter, answer)) {
                    correctWordLetters.remove(letter);
                }
            }
        }
        return correctWordLetters;
    }

    @Override
    public String getRandomUnpressedAnswerFromQuestion(Set<String> answersIds) {
        List<String> list = getUnpressedCorrectAnswers(answersIds);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public float getPercentOfCorrectAnswersPressed(Set<String> answerIds) {
        HashSet<String> correctLetters = new HashSet<>(answerIds);
        correctLetters.retainAll(normalizedWordLetters);
        return (correctLetters.size() / Float.valueOf(normalizedWordLetters.size())) * 100;
    }

    @Override
    public boolean compareAnswerStrings(String hangmanWord, String answer) {
        hangmanWord = processHangmanString(hangmanWord);
        answer = processHangmanString(answer);
        return hangmanWord.toLowerCase().contains(answer.toLowerCase());
    }

    private String normalize(String answer) {
        return removeDiacritic(answer);
    }

    private static final String tab00c0 = "AAAAAAACEEEEIIII" +
            "DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
            "aaaaaaaceeeeiiii" +
            "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
            "AaAaAaCcCcCcCcDd" +
            "DdEeEeEeEeEeGgGg" +
            "GgGgHhHhIiIiIiIi" +
            "IiJjJjKkkLlLlLlL" +
            "lLlNnNnNnnNnOoOo" +
            "OoOoRrRrRrSsSsSs" +
            "SsTtTtTtUuUuUuUu" +
            "UuUuWwYyYZzZzZzF";

    /**
     * Returns string without diacritics - 7 bit approximation.
     *
     * @param source string to convert
     * @return corresponding string without diacritics
     */
    public static String removeDiacritic(String source) {
        char[] vysl = new char[source.length()];
        char one;
        for (int i = 0; i < source.length(); i++) {
            one = source.charAt(i);
            if (one >= '\u00c0' && one <= '\u017f') {
                one = tab00c0.charAt((int) one - '\u00c0');
            }
            vysl[i] = one;
        }
        return new String(vysl);
    }

    private boolean availableLettersHaveSpecialCharacters() {
        return !getAvailableLetters().replace(",", "").equals(STANDARD_LETTERS);
    }

    public String getAvailableLetters() {
        return availableLetters;
    }

    @Override
    protected int getSimulatePressedLetterCorrectAnswerFactor() {
        //[0,100) IF number is small, more correct answers will be found
        return 52;
    }

    @Override
    protected List<Long> getFastIntervalsToPressAnswer() {
        return Arrays.asList(2500L, 3000L, 3500L, 4000L);
    }

    @Override
    protected List<Long> getSlowIntervalsToPressAnswer() {
        return Arrays.asList(4500L, 5000L, 5500L, 6000L);
    }


}