package libgdx.dbapi;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import libgdx.game.model.GameStats;

public class GameStatsDbApiService extends DbApiService {

    @DbApiServiceSubmit
    public void createGameStats(int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(USERID, userId);
        submitInfoPost("createGameStats", formParametersString(params));
    }

    @DbApiServiceGet
    public GameStats getGameStats(int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(USERID, userId);
        GameStats gameStats = new Gson().fromJson(getInfoPost("getGameStats", formParametersString(params)), GameStats.class);
        return gameStats != null && gameStats.getUserId() != 0 ? gameStats : null;
    }

    @DbApiServiceSubmit
    public void incrementGameStatsTournamentsStarted(int userId, String uniqueOperationId) {
        executeIncrementStats(userId, "incrementTournamentsStarted", uniqueOperationId);
    }

    @DbApiServiceSubmit
    public void incrementGameStatsTournamentsWon(int userId, String uniqueOperationId) {
        executeIncrementStats(userId, "incrementTournamentsWon", uniqueOperationId);
    }

    @DbApiServiceSubmit
    public void incrementGameStatsQuestionsStarted(int userId, String uniqueOperationId) {
        executeIncrementStats(userId, "incrementGameStatsQuestionsStarted", uniqueOperationId);
    }

    @DbApiServiceSubmit
    public void incrementGameStatsQuestionsWon(int userId, String uniqueOperationId) {
        executeIncrementStats(userId, "incrementGameStatsQuestionsWon", uniqueOperationId);
    }

    private void executeIncrementStats(int userId, final String dbOperation, String uniqueId) {
        if (UniqueDbOperationContainer.getInstance().isDbOperationValid(dbOperation, uniqueId)) {
            final Map<String, Object> params = new HashMap<>();
            params.put(USERID, userId);

            submitInfoPost(dbOperation, formParametersString(params));
        }
    }

    @Override
    protected String getApiModuleName() {
        return "gamestats";
    }
}
