package libgdx.implementations.skelgame.gameservice;

import libgdx.game.Game;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class HangmanGameService extends GameService {

    public static final String STANDARD_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final int GAME_OVER_WRONG_LETTERS = 6;
    private static final List<String> CHARS_TO_BE_IGNORED = Arrays.asList(" ", "-", "'");
    private Set<String> normalizedWordLetters;
    private String availableLetters;


    public HangmanGameService(Question question) {
        super(question);
        availableLetters = SpecificPropertiesUtils.getText(Game.getInstance().getAppInfoService().getLanguage() + "_hangman_available_letters");
        normalizedWordLetters = getNormalizedWordLetters(getHangmanWord(question.getQuestionString()));
    }

    @Override
    public boolean isAnswerCorrectInQuestion(String answer) {
        return isLetterCorrectInWord(getHangmanWord(question.getQuestionString()), answer);
    }

    private boolean isLetterCorrectInWord(String hangmanWord, String answer) {
        return compareAnswerStrings(hangmanWord, answer);
    }

    public String getHangmanWord(String questionString) {
        return questionString.contains(":") ? questionString.split(":")[2] : questionString;
    }

    @Override
    protected int getQuestionToBeDisplayedPositionInString() {
        return 1;
    }

    @Override
    public int getImageToBeDisplayedPositionInString() {
        return 3;
    }

    public String getCurrentWordState(String hangmanWord, Set<String> answerIds) {
        Set<String> wordLettersToProcess = new HashSet<>(normalizedWordLetters);
        wordLettersToProcess.removeAll(answerIds);
        String processedHangmanWord = processHangmanString(hangmanWord);
        for (String letter : wordLettersToProcess) {
            processedHangmanWord = replaceLetter(processedHangmanWord, letter.toLowerCase());
            processedHangmanWord = replaceLetter(processedHangmanWord, letter.toUpperCase());
        }
        char[] hangmanWordArray = hangmanWord.toCharArray();
        char[] processedHangmanWordArray = processedHangmanWord.toCharArray();
        for (int i = 0; i < processedHangmanWordArray.length; i++) {
            hangmanWordArray[i] = processedHangmanWordArray[i] == '_' ? '_' : hangmanWordArray[i];
        }
        return new String(hangmanWordArray);
    }

    private String processHangmanString(String string) {
        return !availableLettersHaveSpecialCharacters() ? normalize(string) : string;
    }

    private String replaceLetter(String hangmanWord, String letter) {
        return hangmanWord.replace(letter, "_");
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

    private String getHangmanWordLastLetter(String hangmanWord) {
        return hangmanWord.substring(hangmanWord.length() - 1).toLowerCase();
    }

    private String getHangmanWordFirstLetter(String hangmanWord) {
        return hangmanWord.substring(0, 1).toLowerCase();
    }

    @Override
    public float getPercentOfCorrectAnswersPressed(Set<String> answerIds) {
        HashSet<String> correctLetters = new HashSet<>(answerIds);
        correctLetters.retainAll(normalizedWordLetters);
        return (correctLetters.size() / Float.valueOf(normalizedWordLetters.size())) * 100;
    }

    @Override
    public void processNewGameQuestionInfo(GameUser gameUser, GameQuestionInfo gameQuestionInfo) {
        Question question = gameQuestionInfo.getQuestion();
        String hangmanWord = processHangmanString(getHangmanWord(question.getQuestionString()));
        String firstLetter = getHangmanWordFirstLetter(hangmanWord);
        String lastLetter = getHangmanWordLastLetter(hangmanWord);
        gameUser.addAnswerToGameQuestionInfo(gameQuestionInfo, firstLetter, 1L);
        if (!compareAnswerStrings(firstLetter.toLowerCase(), lastLetter.toLowerCase())) {
            gameUser.addAnswerToGameQuestionInfo(gameQuestionInfo, lastLetter, 1L);
        }
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
//
//    @Override
//    protected List<Long> getFastIntervalsToPressAnswer() {
//        return Arrays.asList(25L, 30L, 35L, 40L);
//    }
//
//    @Override
//    protected List<Long> getSlowIntervalsToPressAnswer() {
//        return Arrays.asList(45L, 50L, 55L, 60L);
//    }
}
