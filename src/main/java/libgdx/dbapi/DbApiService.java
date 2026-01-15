package libgdx.dbapi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class DbApiService {

    public final static Integer API_VERSION_VALUE = 7;

    //TODO  ---VALUE CHANGED---  Should be on remote server
    public final static String BASE_URL = "http://46.101.134.86/" + formApiVersion() + "/gameApi/api";
//    public final static String BASE_URL = "http://localhost/" + formApiVersion() + "/gameApi/api";

    protected static final String EXTERNALID = "externalId";
    protected static final String FULLNAME = "fullName";
    protected static final String ACCOUNTCREATIONSOURCE = "accountCreationSource";
    protected static final String LASTTIMEACTIVEDATE = "lastTimeActiveDate";
    protected static final String ENTITYCREATIONDATE = "entityCreationDate";
    protected static final String GAMEID = "gameId";
    protected static final String API_VERSION_NAME = "apiVersion";
    protected static final String COLUMNVALUE = "columnValue";
    protected static final String KEYNAME = "keyname";
    protected static final String COLUMNNAME = "columnName";
    protected static final String ENTITYID = "entityId";
    protected static final String GAMECONFIG = "gameConfig";
    protected static final String TEXT = "text";
    protected static final String VALUETOINCREMENT = "valueToIncrement";
    protected static final String COINSAMOUNT = "coinsAmount";
    protected static final String TRANSACTIONTYPE = "transactionType";
    protected static final String TRANSACTIONTYPELIST = "transactionTypeList";
    protected static final String EXPERIENCEGAIN = "experienceGain";
    protected static final String USERID = "userId";
    protected static final String LEVEL = "level";
    protected static final String USER1ID = "user1Id";
    protected static final String USER2ID = "user2Id";
    protected static final String QUESTIONS = "questions";
    protected static final String INITIALUSERSTATUS = "initialUserStatus";


    protected String getInfoPost(String apiMethodName, String postParams) {
        return executePost(apiMethodName, postParams);
    }

    protected synchronized Thread submitInfoPost(final String apiMethodName, final String postParams) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                executePost(apiMethodName, postParams);
            }
        });
        thread.start();
        return thread;
    }

    private synchronized String executePost(String apiMethodName, String postParams) {
        HttpURLConnection con = null;

        try {
            //Create con
            URL url = new URL(getBaseUrl() + "" + "/" + getApiModuleName() + "/" + apiMethodName + ".php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Accept", "application/json; charset=utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(postParams.getBytes().length));
            con.setUseCaches(false);
            con.setDoOutput(true);

//            System.out.print(apiMethodName + " xxx ");

            //Send request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(postParams.getBytes("UTF-8"));
            wr.close();

            //Get Response
            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return response.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    @DbApiServiceGet
    public <TType> TType getColumnValueForUserId(Integer userId, String columnName, Class<TType> resultType) {
        Map<String, Object> params = new HashMap<>();
        params.put(USERID, userId);
        params.put(COLUMNNAME, columnName);
        return userId != null ? new Gson().<TType>fromJson(getInfoPost("getColumnValueForUserId", formParametersString(params)), resultType) : null;
    }

    @DbApiServiceSubmit
    public synchronized <TType> void updateColumnValueForUserId(Integer userId, String columnName, TType columnValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(USERID, userId);
        params.put(COLUMNNAME, columnName);
        params.put(COLUMNVALUE, columnValue);
        submitInfoPost("updateColumnValueForUserId", formParametersString(params));
    }

    @DbApiServiceGet
    public <TType> TType getColumnValue(Integer entityId, String columnName, Class<TType> resultType) {
        Map<String, Object> params = new HashMap<>();
        params.put(ENTITYID, entityId);
        params.put(COLUMNNAME, columnName);
        return entityId != null ? new Gson().<TType>fromJson(getInfoPost("getColumnValue", formParametersString(params)), resultType) : null;
    }

    @DbApiServiceSubmit
    public synchronized void incrementColumnValue(Integer entityId, String columnName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ENTITYID, entityId);
        params.put(COLUMNNAME, columnName);
        submitInfoPost("incrementColumnValue", formParametersString(params));
    }

    @DbApiServiceSubmit
    public synchronized <TType> Thread updateColumnValue(Integer entityId, String columnName, TType columnValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(ENTITYID, entityId);
        params.put(COLUMNNAME, columnName);
        params.put(COLUMNVALUE, columnValue);
        return submitInfoPost("updateColumnValue", formParametersString(params));
    }

    @DbApiServiceSubmit
    public void deleteEntity(Integer entityId) {
        Map<String, Object> params = new HashMap<>();
        params.put(ENTITYID, entityId);
        submitInfoPost("deleteEntity", formParametersString(params));
    }

    protected String formParametersString(Map<String, Object> params) {
        params.put(API_VERSION_NAME, formApiVersion());
        StringBuilder formedParams = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            formedParams.append(new Gson().toJson(param.getKey()) + ":" + new Gson().toJson(param.getValue()));
            i++;
            if (i < params.size()) {
                formedParams.append(",");
            }
        }
        return "{" + formedParams.toString() + "}";
    }

    private static String formApiVersion() {
        return "v" + API_VERSION_VALUE;
    }

    protected abstract String getApiModuleName();

    protected String getBaseUrl() {
        return BASE_URL;
    }
}
