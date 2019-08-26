package libgdx.implementations.skelgame.question;

import libgdx.utils.Utils;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameQuestionInfo {

    private Question question;
    private GameQuestionInfoStatus status = GameQuestionInfoStatus.OPEN;
    private Set<GameAnswerInfo> answers = new LinkedHashSet<>();

    public GameQuestionInfo(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public Set<GameAnswerInfo> getAnswers() {
        return answers;
    }

    void addAnswer(String answerId, Long millisAnswered) {
        answers.add(new GameAnswerInfo(answerId, millisAnswered));
    }

    public Long getTotalAnswerMillis() {
        return getAnswers().isEmpty() ? 0L : ((GameAnswerInfo) Utils.getLastElement(getAnswers())).getMillisAnswered();
    }

    public Set<String> getAnswerIds() {
        Set<String> answerIds = new LinkedHashSet<>();
        for (GameAnswerInfo gameAnswerInfo : answers) {
            answerIds.add(gameAnswerInfo.getAnswer());
        }
        return answerIds;
    }

    void setStatus(GameQuestionInfoStatus status) {
        this.status = status;
    }

    public GameQuestionInfoStatus getStatus() {
        return status;
    }

    public boolean isQuestionOpen() {
        return status == GameQuestionInfoStatus.OPEN;
    }

    @Override
    public String toString() {
        return "GameQuestionInfo{" +
                "question=" + question +
                ", answers=" + answers +
                '}';
    }
}
