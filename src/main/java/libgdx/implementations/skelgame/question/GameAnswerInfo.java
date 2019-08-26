package libgdx.implementations.skelgame.question;

import java.util.Objects;

public class GameAnswerInfo {

    private String answer;
    private Long millisAnswered;

    public GameAnswerInfo(String answer, Long millisAnswered) {
        this.answer = answer;
        this.millisAnswered = millisAnswered;
    }

    public String getAnswer() {
        return answer;
    }

    public Long getMillisAnswered() {
        return millisAnswered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameAnswerInfo that = (GameAnswerInfo) o;
        return Objects.equals(answer, that.answer) &&
                Objects.equals(millisAnswered, that.millisAnswered);
    }

    @Override
    public int hashCode() {

        return Objects.hash(answer, millisAnswered);
    }

    @Override
    public String toString() {
        return "GameAnswerInfo{" +
                "answer='" + answer + '\'' +
                ", millisAnswered=" + millisAnswered +
                '}';
    }
}
