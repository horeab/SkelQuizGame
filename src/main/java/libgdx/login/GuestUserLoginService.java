package libgdx.login;

import libgdx.constants.user.AccountCreationSource;
import libgdx.game.Game;
import libgdx.game.external.LoginService;
import libgdx.resources.gamelabel.MainGameLabel;

public class GuestUserLoginService implements LoginService {

    private GuestUserService guestUserService;

    public GuestUserLoginService() {
        guestUserService = new GuestUserService();
    }

    @Override
    public String getExternalId() {
        return guestUserService.getExternalId();
    }

    @Override
    public String getFullName() {
        return guestUserService.getFullName();
    }

    @Override
    public AccountCreationSource getAccountCreationSource() {
        return AccountCreationSource.INTERNAL;
    }

    @Override
    public boolean isUserLoggedIn() {
        return guestUserService.isLoggedIn();
    }

    @Override
    public void loginClick(AccountCreationSource accountCreationSource, final Runnable redirectAfterLogin) {
        if (isUserLoggedIn()) {
            guestUserService.logOutGuestUser();
            goToMainMenu();
        } else {
            guestUserService.createGuestUser(MainGameLabel.guest.getText());
            if (redirectAfterLogin != null) {
                redirectAfterLogin.run();
            }
        }
    }

    public void loginClick(AccountCreationSource accountCreationSource) {
        loginClick(accountCreationSource, null);
    }


    @Override
    public void goToMainMenu() {
        Game.getInstance().getScreenManager().showMainScreen();
    }
}
