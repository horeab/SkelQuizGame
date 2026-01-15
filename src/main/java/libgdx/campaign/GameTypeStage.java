package libgdx.campaign;

import java.util.ArrayList;
import java.util.List;

public enum GameTypeStage {

    CAMPAIGN_LEVEL_0_0(GameType.CAMPAIGN),
    CAMPAIGN_LEVEL_0_1( GameType.CAMPAIGN),
    CAMPAIGN_LEVEL_0_2( GameType.CAMPAIGN),
    CAMPAIGN_LEVEL_0_3( GameType.CAMPAIGN),
    CAMPAIGN_LEVEL_0_4(GameType.CAMPAIGN),
    CAMPAIGN_LEVEL_0( GameType.CAMPAIGN),;

    private GameType gameType;

    GameTypeStage(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public static List<GameTypeStage> getGameTypeStages(GameType gameType) {
        List<GameTypeStage> stages = new ArrayList<>();
        for (GameTypeStage stage : values()) {
            if (stage.getGameType() == gameType) {
                stages.add(stage);
            }
        }
        return stages;
    }

}
