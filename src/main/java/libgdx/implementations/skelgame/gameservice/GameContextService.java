package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionConfig;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;

import java.util.ArrayList;
import java.util.List;

public class GameContextService {

    public GameContext createGameContext(Question... questions) {
        int AMOUNT_AVAILABLE_HINTS = 0;
        return createGameContext(AMOUNT_AVAILABLE_HINTS, questions);
    }

    public GameContext createGameContext(int amountAvailableHints, Question... questions) {
        GameUser gameUser = createGameUser(questions);
        List<String> categs = new ArrayList<>();
        List<String> diff = new ArrayList<>();
        for (Question question : questions) {
            categs.add(question.getQuestionCategory().name());
            diff.add(question.getQuestionDifficultyLevel().name());
        }
        QuestionConfig questionConfig = new QuestionConfig(diff, categs, questions.length, amountAvailableHints);
        return createGameContext(gameUser, questionConfig);
    }

    public GameContext createGameContext(QuestionConfig questionConfig) {
        Question[] randomQuestions = new RandomQuestionCreatorService().createRandomQuestions(questionConfig);
        GameUser gameUser = createGameUser(randomQuestions);
        return createGameContext(gameUser, questionConfig);
    }

    private GameContext createGameContext(GameUser gameUser, QuestionConfig questionConfig) {
        return new GameContext(gameUser, questionConfig);
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
