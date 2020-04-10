package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionConfig;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class GameContext {

    private GameUser currentUserGameUser;
    private Long millisGameStarted;
    private int amountAvailableHints;
    private QuestionConfig questionConfig;

    GameContext(GameUser currentUserGameUser, QuestionConfig questionConfig) {
        this.currentUserGameUser = currentUserGameUser;
        this.questionConfig = questionConfig;
        this.amountAvailableHints = questionConfig.getAmountHints();
    }

    public GameUser getCurrentUserGameUser() {
        return currentUserGameUser;
    }

    public QuestionConfig getQuestionConfig() {
        return questionConfig;
    }

    public Question getQuestion() {
        return getCurrentUserGameUser().getGameQuestionInfo().getQuestion();
    }

    public void useHint() {
        amountAvailableHints--;
    }

    public void addHint() {
            amountAvailableHints++;
    }

    public int getAmountAvailableHints() {
        return amountAvailableHints;
    }

    public List<HintButtonType> getAvailableHints() {
        List<HintButtonType> availableHints = new ArrayList<>();
        for (int i = 0; i < amountAvailableHints; i++) {
            availableHints.add(getCurrentUserCreatorDependencies().getHintButtonType());
        }
        return availableHints;
    }

    public void startMillis() {
        if (millisGameStarted == null) {
            millisGameStarted = DateUtils.getNowMillis();
        }
    }

    public Long getMillisGameInProgress() {
        return DateUtils.getNowMillis() - millisGameStarted;
    }

    public CreatorDependencies getCurrentUserCreatorDependencies() {
        return CreatorDependenciesContainer.getCreator(getQuestion().getQuestionCategory().getCreatorDependencies());
    }

}