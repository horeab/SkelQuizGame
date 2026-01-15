package libgdx.login;

import org.apache.commons.lang3.StringUtils;

import libgdx.constants.user.AccountCreationSource;
import libgdx.dbapi.UsersDbApiService;
import libgdx.preferences.PreferencesService;
import libgdx.utils.DateUtils;

public class GuestUserService {

    private static final String LOGIN_EXTERNAL_ID = "GuestUserExternalId";
    private static final String LOGIN_FULLNAME = "GuestUserFullName";
    private PreferencesService preferencesService;

    private UsersDbApiService usersDbApiService = new UsersDbApiService();

    public GuestUserService() {
        preferencesService = new PreferencesService("GuestUserService");
    }

    public void createGuestUser(String guestLabel) {
        String externalId = String.valueOf(DateUtils.getNowMillis());
        String fullName = guestLabel + externalId.substring(externalId.length() - 5, externalId.length());
        usersDbApiService.createUser(externalId, fullName, AccountCreationSource.INTERNAL);
        logInGuestUser(externalId, fullName);
    }

    public void logOutGuestUser() {
        String externalId = getExternalId();
        if (StringUtils.isNotBlank(externalId)) {
            usersDbApiService.deleteEntity(usersDbApiService.getUserId(externalId, AccountCreationSource.INTERNAL));
            preferencesService.remove(LOGIN_EXTERNAL_ID);
            preferencesService.remove(LOGIN_FULLNAME);
        }
    }

    private void logInGuestUser(String externalId, String fullName) {
        preferencesService.putString(LOGIN_EXTERNAL_ID, externalId);
        preferencesService.putString(LOGIN_FULLNAME, fullName);
    }

    public boolean isLoggedIn() {
        return StringUtils.isNotBlank(getExternalId());
    }

    public String getExternalId() {
        return preferencesService.getPreferences().getString(LOGIN_EXTERNAL_ID, null);
    }

    public String getFullName() {
        return preferencesService.getPreferences().getString(LOGIN_FULLNAME, null);
    }
}
