package libgdx.resources.gamelabel;

import libgdx.game.Game;
import libgdx.resources.Res;
import libgdx.resources.ResourceService;
import libgdx.resources.properties.PropertiesUtils;


public class GameLabelUtils {

    public static final ResourceService RESOURCE_SERVICE = Game.getInstance().getMainDependencyManager().createResourceService();

    public static Res getLabelRes(String language) {
        return getLabelResForRootFolder(language, "labels");
    }

    public static Res getMainLabelRes(String language) {
        return getLabelResForRootFolder(language, "main_labels");
    }

    private static Res getLabelResForRootFolder(String language, String labelFilePrefix) {
        return RESOURCE_SERVICE.getByName(labelFilePrefix + "_" + language);
    }

    public static String getText(String key, String language, String filePath, Object... params) {
        return PropertiesUtils.getPropertyValue(key, language, filePath, SpecificPropertiesUtils.getLabelFilePath(), params);
    }

    public static String getRawText(String key, Object... params) {
        String labelFilePath = SpecificPropertiesUtils.getLabelFilePath();
        return PropertiesUtils.getPropertyValue(key, "", labelFilePath, labelFilePath, params);
    }

}
