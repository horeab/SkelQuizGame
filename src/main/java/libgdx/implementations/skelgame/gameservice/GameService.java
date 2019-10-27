package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.question.GameAnswerInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class GameService {

    protected Question question;

    public GameService(Question question) {
        this.question = question;
    }

    public boolean addAnswerToGameInfo(GameUser gameUser, GameAnswerInfo gameAnswerInfo) {
        boolean isAnswerCorrectInQuestion = isAnswerCorrectInQuestion(gameAnswerInfo.getAnswer());
        gameUser.addAnswerToGameQuestionInfo(gameAnswerInfo.getAnswer(), gameAnswerInfo.getMillisAnswered());
        return isAnswerCorrectInQuestion;
    }

    public boolean compareAnswerStrings(String answer1, String answer2) {
        return answer1.equals(answer2);
    }

    private boolean isGameFinished(Set<String> answersIds) {
        return isGameFinishedSuccessful(answersIds) || isGameFinishedFailed(answersIds);
    }

    public Image getQuestionImage() {
        Image image = null;
        String[] split = question.getQuestionString().split(":");
        int imageSplitPos = getImageToBeDisplayedPositionInString();
        if (split.length == imageSplitPos + 1 && StringUtils.isNotBlank(split[imageSplitPos])) {
            image = GraphicUtils.getImage(Game.getInstance().getMainDependencyManager().createResourceService().getByName("img_cat" + question.getQuestionCategory().getIndex() + "_" + split[imageSplitPos]));
        }
        return image;
    }

    public String getQuestionToBeDisplayed() {
        String questionString = question.getQuestionString();
        String questionToBeDisplayed = questionString.contains(":") ? questionString.split(":")[getQuestionToBeDisplayedPositionInString()] : "";
        if (StringUtils.isBlank(questionToBeDisplayed)) {
            Game game = Game.getInstance();
            String generalQuestionToBeDisplayed = SpecificPropertiesUtils.getText(game.getAppInfoService().getLanguage() + "_" + game.getGameIdPrefix() + "_" + question.getQuestionCategory().name() + "_question");
            questionToBeDisplayed = generalQuestionToBeDisplayed != null ? generalQuestionToBeDisplayed : "";
        }
        return questionToBeDisplayed;
    }

    private List<String> createWrongAnswerList(GameQuestionInfo gameQuestionInfo, List<String> allAnswers, List<String> correctAnswers) {
        List<String> newAllAnswersList = new ArrayList<>(allAnswers);
        Collections.shuffle(newAllAnswersList);
        newAllAnswersList.removeAll(gameQuestionInfo.getAnswerIds());
        newAllAnswersList.removeAll(correctAnswers);
        return newAllAnswersList;
    }

    public void processNewGameQuestionInfo(GameUser gameUser, GameQuestionInfo gameQuestionInfo) {
    }

    public abstract int getImageToBeDisplayedPositionInString();

    protected abstract int getQuestionToBeDisplayedPositionInString();

    public abstract boolean isGameFinishedSuccessful(Set<String> answersIds);

    public abstract boolean isGameFinishedFailed(Set<String> answersIds);

    public abstract int getNrOfWrongAnswersPressed(Set<String> answerIds);

    public abstract boolean isAnswerCorrectInQuestion(String answer);

    public abstract String getRandomUnpressedAnswerFromQuestion(Set<String> answersIds);

    public abstract float getPercentOfCorrectAnswersPressed(Set<String> answerIds);

    public abstract List<String> getAllAnswerOptions();

    public abstract List<String> getUnpressedCorrectAnswers(Set<String> answerIds);

    protected abstract int getSimulatePressedLetterCorrectAnswerFactor();

    protected abstract List<Long> getFastIntervalsToPressAnswer();

    protected abstract List<Long> getSlowIntervalsToPressAnswer();
}
