package libgdx.resources.properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.utils.I18NBundle;

import org.apache.commons.lang3.StringUtils;

import java.util.MissingResourceException;

import libgdx.game.Game;

public class PropertiesUtils {

    public static String getPropertyValue(String key, String language, String filePath, String specificPropertiesFilePath, Object... params) {
        String specificProperty = getRawPropertyValue(language + "_" + key, specificPropertiesFilePath, params);
        String label;
        if (StringUtils.isNotBlank(specificProperty)) {
            label = specificProperty;
        } else {
            label = PropertiesUtils.getRawPropertyValue(key, filePath, params);
        }
        return label;
    }


    private static String getRawPropertyValue(String key, String filePath, Object... params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Integer) {
                params[i] = String.valueOf(params[i]);
            }
        }
        I18NBundleLoader.I18NBundleParameter param = new I18NBundleLoader.I18NBundleParameter(null, "UTF-8");
        Game.getInstance().getAssetManager().load(filePath, I18NBundle.class, param);
        I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal(filePath), "UTF-8");
        String label = null;
        if (PropertiesUtils.propertyExists(bundle, key)) {
            label = bundle.format(key, params);
        }
        //
        return label;
    }

    private static boolean propertyExists(I18NBundle bundle, String key) {
        try {
            bundle.get(key);
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

}
