package libgdx.controls.popup;


import com.badlogic.gdx.Preferences;

import libgdx.game.Game;
import libgdx.preferences.PreferencesService;
import libgdx.screen.AbstractScreen;
import libgdx.utils.InternetUtils;

public abstract class RatingService<TScreen extends AbstractScreen> {

    //TODO ---VALUE CHANGED--- should be 2
    private final static int LAUNCHES_UNTIL_PROMPT = 2;

    private TScreen abstractScreen;
    private PreferencesService preferencesService = new PreferencesService("apprater");

    public RatingService(TScreen abstractScreen) {
        this.abstractScreen = abstractScreen;
    }

    public TScreen getScreen() {
        return abstractScreen;
    }

    public void appLaunched() {
        Preferences preferences = preferencesService.getPreferences();
        if (alreadyRated()) {
            //TODO ---VALUE CHANGED--- should be return
            return;
        }

        // Increment launch counter
        long launchCount = preferences.getLong("launch_count", 0) + 1;
        preferencesService.putLong("launch_count", launchCount);

        // Get date of first launch
        Long dateFirstLaunch = preferences.getLong("date_firstlaunch", 0);
        if (dateFirstLaunch == 0) {
            dateFirstLaunch = System.currentTimeMillis();
            preferences.putLong("date_firstlaunch", dateFirstLaunch);
        }

        // Wait at least n days before opening
        if (launchCount % LAUNCHES_UNTIL_PROMPT == 0 && launchCount != 0) {
            RatingPopup ratingPopup = createRatingPopup();
            ratingPopup.addToPopupManager();
        }
    }

    protected abstract RatingPopup createRatingPopup();

    public boolean alreadyRated() {
        //TODO ---VALUE CHANGED--- not false
//        return false;
        return preferencesService.getPreferences().getBoolean("dontshowagain", false);
    }

    public void rateNow() {
        preferencesService.putBoolean("dontshowagain", true);
        InternetUtils.openAppUrl(Game.getInstance().getAppInfoService().getStoreAppId(), true);
    }
}
