package libgdx.game.external;

import libgdx.constants.user.AccountCreationSource;

public interface LoginService {

    String getExternalId();

    String getFullName();

    AccountCreationSource getAccountCreationSource();

    boolean isUserLoggedIn();

    void loginClick(AccountCreationSource accountCreationSource, Runnable redirectAfterLogin);

    void goToMainMenu();

}
