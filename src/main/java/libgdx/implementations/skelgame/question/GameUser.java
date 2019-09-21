package libgdx.implementations.skelgame.question;

import libgdx.implementations.skelgame.gameservice.CreatorDependenciesContainer;
import libgdx.implementations.skelgame.gameservice.DependentAnswersQuizGameService;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.resources.Resource;
import libgdx.utils.SoundUtils;

import java.util.LinkedList;
import java.util.List;

public class GameUser {

    private List<GameQuestionInfo> allQuestionInfos = new LinkedList<>();
    private List<GameQuestionInfo> openQuestionInfos = new LinkedList<>();
    private int wonQuestions = 0;
    private int lostQuestions = 0;

    private void setWonQuestion(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo.isQuestionOpen()) {
            gameQuestionInfo.setStatus(GameQuestionInfoStatus.WON);
            wonQuestions++;
            openQuestionInfos.remove(gameQuestionInfo);
        }
    }

    private void setLostQuestion(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo.isQuestionOpen()) {
            gameQuestionInfo.setStatus(GameQuestionInfoStatus.LOST);
            lostQuestions++;
            openQuestionInfos.remove(gameQuestionInfo);
        }
    }

    public int getWonQuestions() {
        return wonQuestions;
    }

    public int getLostQuestions() {
        return lostQuestions;
    }

    public int getFinishedQuestions() {
        return wonQuestions + lostQuestions;
    }

    public void addAnswerToGameQuestionInfo(GameQuestionInfo gameQuestionInfo, String answerId, Long millisAnswered) {
        if (gameQuestionInfo != null) {
            gameQuestionInfo.addAnswer(answerId, millisAnswered);
            setQuestionFinishedStatus(gameQuestionInfo);
        }
    }

    public void addAnswerToGameQuestionInfo(String answerId, Long millisAnswered) {
        addAnswerToGameQuestionInfo(getGameQuestionInfo(), answerId, millisAnswered);
    }

    private void setQuestionFinishedStatus(GameQuestionInfo gameQuestionInfo) {
        GameService gameService = CreatorDependenciesContainer.getCreator(gameQuestionInfo.getQuestion().getQuestionCategory().getCreatorDependencies()).getGameService(gameQuestionInfo.getQuestion());
        boolean userSuccess = gameService.isGameFinishedSuccessful(gameQuestionInfo.getAnswerIds());
        if (userSuccess) {
//            playSound(Resource.sound_correct_answer);
            setWonQuestion(gameQuestionInfo);
        } else {
            boolean userFail = gameService.isGameFinishedFailed(gameQuestionInfo.getAnswerIds());
            if (userFail) {
//                playSound(Resource.sound_wrong_answer);
                setLostQuestion(gameQuestionInfo);
            }
        }
    }

    private void playSound(Resource sound_correct_answer) {
        SoundUtils.playSound(sound_correct_answer);
    }

    public GameQuestionInfo getGameQuestionInfo() {
        return openQuestionInfos.isEmpty() ? null : openQuestionInfos.get(0);
    }

    public Long getTotalAnswerMillisForGameQuestionInfoList() {
        long totalAnswerMillisForGameQuestionInfoList = 0;
        for (GameQuestionInfo gameQuestionInfo : allQuestionInfos) {
            totalAnswerMillisForGameQuestionInfoList = totalAnswerMillisForGameQuestionInfoList + gameQuestionInfo.getTotalAnswerMillis();
        }
        return totalAnswerMillisForGameQuestionInfoList;
    }

    public GameQuestionInfo getGameQuestionInfo(int questionIndex) {
        return allQuestionInfos.get(questionIndex);
    }

    public boolean userHasMultipleQuestions() {
        return getTotalNrOfQuestions() > 1;
    }

    public List<GameQuestionInfo> getAllQuestionInfos() {
        return allQuestionInfos;
    }

    public int getTotalNrOfQuestions() {
        return allQuestionInfos.size();
    }

    public void addGameQuestionInfoToList(GameQuestionInfo gameQuestionInfo) {
        allQuestionInfos.add(gameQuestionInfo);
        openQuestionInfos.add(gameQuestionInfo);
    }

    public List<GameQuestionInfo> getOpenQuestionInfos() {
        return openQuestionInfos;
    }

}
