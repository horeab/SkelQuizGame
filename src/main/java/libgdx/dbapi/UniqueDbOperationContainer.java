package libgdx.dbapi;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class UniqueDbOperationContainer {

    private Map<String, String> dbOperationWithUniqueId = new HashMap<>();

    private static UniqueDbOperationContainer instance;

    private UniqueDbOperationContainer() {
        super();
    }

    public static UniqueDbOperationContainer getInstance() {
        if (instance == null) {
            instance = new UniqueDbOperationContainer();
        }
        return instance;
    }

    public boolean isTransactionExecuted(String dbOperationId, String uniqueOperationId) {
        String uqId = dbOperationWithUniqueId.get(dbOperationId);
        return StringUtils.isNotBlank(uqId) && uqId.equals(uniqueOperationId);
    }

    public boolean isDbOperationValid(String dbOperationId, String uniqueOperationId) {
        String savedUniqueOperationId = dbOperationWithUniqueId.get(dbOperationId);
        if (StringUtils.isBlank(savedUniqueOperationId) || !savedUniqueOperationId.equals(uniqueOperationId)) {
            dbOperationWithUniqueId.put(dbOperationId, uniqueOperationId);
            return true;
        } else
            return false;
    }

    public void clear() {
        dbOperationWithUniqueId.clear();
    }
}
