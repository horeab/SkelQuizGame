package libgdx;

import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.utils.startgame.test.DefaultAppInfoService;
import org.junit.Test;

public class AstronomyGameServiceTest extends GameServiceTest {

    @Test
    public void testAllQuestions() throws Exception {
        testQuestions();
    }

    @Override
    protected AppInfoService getAppInfoService() {
        return new DefaultAppInfoService() {
            @Override
            public String getGameIdPrefix() {
                return GameIdEnum.astronomy.name();
            }
        };
    }

}
