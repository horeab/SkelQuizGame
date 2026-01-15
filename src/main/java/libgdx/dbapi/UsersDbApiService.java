package libgdx.dbapi;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.constants.user.AccountCreationSource;
import libgdx.game.Game;
import libgdx.game.model.BaseUserInfo;
import libgdx.utils.DateUtils;
import libgdx.utils.Utils;

public class UsersDbApiService extends DbApiService {

    public static final String COLUMN_NAME_LASTTIMEACTIVEDATE = "lastTimeActiveDate";
    public static final String COLUMN_NAME_EXTRAINFO = "extraInfo";
    public static final String COLUMN_NAME_EXTRAINFOCHANGE = "extraInfoChange";

    @DbApiServiceGet
    public List<BaseUserInfo> selectUsersForTournament() {
        Map<String, Object> params = new HashMap<>();
        params.put(GAMEID, Game.getInstance().getGameId());
        return Utils.getListFromJsonString(getInfoPost("selectUsersForTournament", formParametersString(params)), BaseUserInfo.class);
    }

    @DbApiServiceGet
    public List<BaseUserInfo> selectUserNameContainsText(String text) {
        Map<String, Object> params = new HashMap<>();
        params.put(TEXT, text.toUpperCase());
        params.put(GAMEID, Game.getInstance().getGameId());
        return Utils.getListFromJsonString(getInfoPost("selectUserNameContainsText", formParametersString(params)), BaseUserInfo.class);
    }

    @DbApiServiceSubmit
    public synchronized int createUser(String externalId, String fullName, AccountCreationSource accountCreationSource) {
        int createdUserId = -1;
        if (createUserApiCall(externalId, fullName, accountCreationSource)) {
            createdUserId = getUser(externalId, accountCreationSource).getId();
            new GameStatsDbApiService().createGameStats(createdUserId);
        }
        return createdUserId;
    }

    @DbApiServiceSubmit
    private synchronized boolean createUserApiCall(String externalId, String fullName, AccountCreationSource accountCreationSource) {
        Map<String, Object> params = new HashMap<>();
        params.put(EXTERNALID, externalId);
        params.put(FULLNAME, fullName);
        params.put(ACCOUNTCREATIONSOURCE, accountCreationSource.name());
        params.put(LASTTIMEACTIVEDATE, Long.toString(DateUtils.getNowMillis()));
        params.put(ENTITYCREATIONDATE, DateUtils.getNowDateString());
        params.put(GAMEID, Game.getInstance().getGameId());
        return "1".equals(getInfoPost("createUser", formParametersString(params)));
    }

    @DbApiServiceSubmit
    public void updateLastTimeActiveDateForUser(Integer userId) {
        updateColumnValue(userId, COLUMN_NAME_LASTTIMEACTIVEDATE, Long.toString(DateUtils.getNowMillis()));
    }

    @DbApiServiceGet
    public BaseUserInfo getUser(String externalId, AccountCreationSource accountCreationSource) {
        BaseUserInfo baseUserInfo = new Gson().fromJson(getUserJsonString(externalId, accountCreationSource), BaseUserInfo.class);
        return baseUserInfo == null || StringUtils.isBlank(baseUserInfo.getExternalId()) ? null : baseUserInfo;
    }

    @DbApiServiceGet
    public BaseUserInfo getUser(Integer userId) {
        BaseUserInfo baseUserInfo = new Gson().fromJson(getUserJsonString(userId), BaseUserInfo.class);
        return baseUserInfo == null || StringUtils.isBlank(baseUserInfo.getExternalId()) ? null : baseUserInfo;
    }

    @DbApiServiceGet
    public Date getLastTimeActiveDateForUser(Integer userId) {
        Long columnValue = getColumnValue(userId, COLUMN_NAME_LASTTIMEACTIVEDATE, Long.class);
        return columnValue != null ? new Date(columnValue) : null;
    }

    @DbApiServiceGet
    public int getUserId(String externalId, AccountCreationSource accountCreationSource) {
        Map<String, Object> params = new HashMap<>();
        params.put(EXTERNALID, externalId);
        params.put(ACCOUNTCREATIONSOURCE, accountCreationSource.name());
        params.put(GAMEID, Game.getInstance().getGameId());
        String userId = new Gson().fromJson(getInfoPost("getUserId", formParametersString(params)), String.class);
        return NumberUtils.isNumber(userId) ? new Gson().fromJson(userId, Integer.class) : -1;
    }

    private String getUserJsonString(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(USERID, userId);
        return getInfoPost("getUser", formParametersString(params));
    }

    private String getUserJsonString(String externalId, AccountCreationSource accountCreationSource) {
        Map<String, Object> params = new HashMap<>();
        params.put(EXTERNALID, externalId);
        params.put(ACCOUNTCREATIONSOURCE, accountCreationSource.name());
        params.put(GAMEID, Game.getInstance().getGameId());
        return getInfoPost("getUser", formParametersString(params));
    }

    @Override
    protected String getApiModuleName() {
        return "users";
    }
}
