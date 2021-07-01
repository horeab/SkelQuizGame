package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.Question;
import libgdx.implementations.skelgame.question.QuestionParser;

import java.util.*;

public class ImageClickGameService extends GameService {

    private QuestionParser questionParser = new QuestionParser();

    public ImageClickGameService(Question question) {
        super(question);
    }

    public Map<MyButton, ImageClickInfo> getAnswerOptionsCoordinates(List<MyButton> allAnswerOptionsButtons, QuestionDifficulty questionDifficultyLevel, QuizQuestionCategory questionCategory) {
        List<Question> allQuestions = questionParser.getAllQuestions(questionDifficultyLevel, questionCategory);
        Map<MyButton, ImageClickInfo> buttonWithCoordinates = new HashMap<>();
        for (Question question : allQuestions) {
            for (MyButton button : allAnswerOptionsButtons) {
                if (button.getText().toLowerCase().equals(questionParser.getAnswers(question.getQuestionString()).get(0).toLowerCase())) {
                    buttonWithCoordinates.put(button, getAnswerOptionCoordinates(question.getQuestionString()));
                    break;
                }
            }
        }
        return buttonWithCoordinates;
    }

    private ImageClickInfo getAnswerOptionCoordinates(String questionString) {
        String[] s = questionString.split(":")[4].split(",");
        ImageClickInfo imageClickInfo = new ImageClickInfo();
        imageClickInfo.x = Float.parseFloat(s[0]);
        imageClickInfo.y = Float.parseFloat(s[1]);
        if (s.length == 3) {
            imageClickInfo.arrowWidth = Float.parseFloat(s[2]);
        }
        return imageClickInfo;
    }


    public List<String> getAnswers(Question question) {
        return questionParser.getAnswers(question.getQuestionString());
    }

    @Override
    public int getImageToBeDisplayedPositionInString() {
        return 5;
    }

    @Override
    protected int getQuestionToBeDisplayedPositionInString() {
        return 2;
    }

    @Override
    public List<String> getAllAnswerOptions() {
        return questionParser.getAllAnswerOptions(question);
    }

    @Override
    public boolean isAnswerCorrectInQuestion(String answer) {
        return compareAnswerStrings(answer.toLowerCase(), getAnswers(question).get(0).toLowerCase());
    }

    @Override
    public boolean isGameFinishedFailed(Set<String> answersIds) {
        return !answersIds.isEmpty() && !isAnswerCorrectInQuestion(answersIds.iterator().next());
    }

    @Override
    public boolean isGameFinishedSuccessful(Set<String> answersIds) {
        return !answersIds.isEmpty() && isAnswerCorrectInQuestion(answersIds.iterator().next());
    }

    @Override
    public List<String> getUnpressedCorrectAnswers(Set<String> answersIds) {
        return getAnswers(question);
    }

    @Override
    public int getNrOfWrongAnswersPressed(Set<String> answersIds) {
        return 0;
    }

    @Override
    public String getRandomUnpressedAnswerFromQuestion(Set<String> answersIds) {
        List<String> answers = getAnswers(question);
        Collections.shuffle(answers);
        return answers.get(0);
    }

    @Override
    protected int getSimulatePressedLetterCorrectAnswerFactor() {
        //[0,100) IF number is small, more correct answers will be found
        return 40;
    }

    @Override
    protected List<Long> getFastIntervalsToPressAnswer() {
        return Arrays.asList(4500L, 5000L, 5500L, 6000L);
    }

    @Override
    protected List<Long> getSlowIntervalsToPressAnswer() {
        return getFastIntervalsToPressAnswer();
    }


    @Override
    public float getPercentOfCorrectAnswersPressed(Set<String> answersIds) {
        return 1f;
    }
}
