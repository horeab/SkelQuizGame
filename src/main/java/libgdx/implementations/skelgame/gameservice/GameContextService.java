package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionConfig;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;

public class GameContextService {

    private int AMOUNT_AVAILABLE_HINTS = 3;

    public GameContext createGameContext(QuestionConfig questionConfig) {
        return createGameContext(AMOUNT_AVAILABLE_HINTS, questionConfig);
    }

    public GameContext createGameContext(int availableHints, QuestionConfig questionConfig) {
        Question[] randomQuestions = new RandomQuestionCreatorService().createRandomQuestions(questionConfig);
        GameUser gameUser = createGameUser(randomQuestions);
        return createGameContext(gameUser, availableHints);
    }

    public GameContext createGameContext(Question... questions) {
        if (questions.length == 0) {
            return createGameContext();
        }
        GameUser gameUser = createGameUser(questions);
        return createGameContext(gameUser, AMOUNT_AVAILABLE_HINTS);
    }

    private GameContext createGameContext(GameUser gameUser, int amountAvailableHints) {
        return new GameContext(gameUser, amountAvailableHints);
    }

    private GameUser createGameUser(Question... questions) {
        GameUser gameUser = new GameUser();
        for (Question question : questions) {
            addNewQuestionInfo(gameUser, question);
        }
        return gameUser;
    }

    private void addNewQuestionInfo(GameUser gameUser, Question question) {
        GameQuestionInfo gameQuestionInfo = new GameQuestionInfo(question);
        gameUser.addGameQuestionInfoToList(gameQuestionInfo);
        CreatorDependenciesContainer.getCreator(question.getQuestionCategory().getCreatorDependencies()).getGameService(gameQuestionInfo.getQuestion()).processNewGameQuestionInfo(gameUser, gameQuestionInfo);
    }

}
