package libgdx.utils.startgame.test;

import libgdx.game.Game;
import libgdx.game.external.AppInfoService;

public class DefaultAppInfoService implements AppInfoService {

    @Override
    public String getGameIdPrefix() {
        throw new RuntimeException("Should have valid GameId");
    }

    @Override
    public void showPopupAd(Runnable afterClose) {
        afterClose.run();
    }

    @Override
    public String getProVersionStoreAppId() {
        return null;
    }

    @Override
    public void showRewardedVideoAd() {
    }

    @Override
    public void removeAds() {

    }

    @Override
    public boolean isProVersion() {
        return false;
    }

    @Override
    public float gameScreenTopMargin() {
        return 0;
    }

    @Override
    public boolean isScreenShotMode() {
        return false;
    }

    @Override
    public String getAppName() {
        return "CHANGE TEST NAME";
    }

    @Override
    public String getStoreAppId() {
        return "com.bogdanICE.Raspunde";
    }

    @Override
    public String getMainResourcesFolder() {
        return "main_resources/main/";
    }

    @Override
    public String getResourcesFolder() {
        return "tournament_resources/main/";
    }

    @Override
    public String getLanguage() {
        return "en";
    }

    @Override
    public boolean isPortraitMode() {
        return true;
    }

    @Override
    public String getImplementationGameResourcesFolder() {
        String gameId = Game.getInstance().getGameIdPrefix();
        String[] gameIdParts = gameId.split("_");
        String path = "tournament_resources/implementations/" + gameIdParts[0] + "/";
        if (gameIdParts.length > 1) {
            path = path + gameIdParts[1] + "/";
        }
        return path;
    }
}
