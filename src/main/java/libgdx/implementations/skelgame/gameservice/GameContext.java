package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class GameContext {

    private GameUser currentUserGameUser;
    private Long millisGameStarted;
    private int amountAvailableHints;

    GameContext(GameUser currentUserGameUser, int amountAvailableHints) {
        this.currentUserGameUser = currentUserGameUser;
        this.amountAvailableHints = amountAvailableHints;
    }

    public GameUser getCurrentUserGameUser() {
        return currentUserGameUser;
    }


    public Question getQuestion() {
        return getCurrentUserGameUser().getGameQuestionInfo().getQuestion();
    }

    public void useHint() {
        amountAvailableHints--;
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