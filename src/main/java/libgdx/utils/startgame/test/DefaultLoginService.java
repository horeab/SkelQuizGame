package libgdx.utils.startgame.test;

import libgdx.constants.user.AccountCreationSource;
import libgdx.game.Game;
import libgdx.game.external.LoginService;

public class DefaultLoginService implements LoginService {

    private boolean isUserLoggedIn = false;

    @Override
    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    @Override
    public void goToMainMenu() {
        Game.getInstance().getScreenManager().showMainScreen();
    }

    @Override
    public void loginClick(AccountCreationSource accountCreationSource, final Runnable redirectAfterLogin) {
        isUserLoggedIn = !isUserLoggedIn();
        redirectAfterLogin.run();
    }

    @Override
    public String getExternalId() {
        return null;
    }

    @Override
    public AccountCreationSource getAccountCreationSource() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }
}
