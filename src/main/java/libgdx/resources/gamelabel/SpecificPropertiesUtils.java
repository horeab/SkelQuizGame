package libgdx.resources.gamelabel;

import libgdx.game.Game;

public class SpecificPropertiesUtils {

    public static String getQuestionCategoryLabel(int catNr) {
        String language = Game.getInstance().getAppInfoService().getLanguage();
        String key = language + "_" + Game.getInstance().getAppInfoService().getGameIdPrefix() + "_question_category_" + catNr;
        return GameLabelUtils.getText(key, language, getLabelFilePath());
    }

    public static String getQuestionCampaignLabel(int campaignNr) {
        String language = Game.getInstance().getAppInfoService().getLanguage();
        String key = language + "_" + Game.getInstance().getAppInfoService().getGameIdPrefix() + "_campaign_" + campaignNr;
        return GameLabelUtils.getText(key, language, getLabelFilePath());
    }

    public static String getText(String key, Object... params) {
        return GameLabelUtils.getText(key, Game.getInstance().getAppInfoService().getLanguage(), getLabelFilePath(), params);
    }

    public static String getLabelFilePath() {
        return Game.getInstance().getMainDependencyManager().createResourceService().getByName("specific_labels").getPath();
    }
}
