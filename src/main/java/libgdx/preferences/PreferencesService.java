package libgdx.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesService {

    private Preferences preferences;

    public PreferencesService(String preferencesName) {
        this.preferences = Gdx.app.getPreferences(preferencesName);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void putLong(String key, long value) {
        preferences.putLong(key, value);
        preferences.flush();
    }

    public void putInteger(String key, int value) {
        preferences.putInteger(key, value);
        preferences.flush();
    }

    public void remove(String key) {
        preferences.remove(key);
        preferences.flush();
    }

    public void putBoolean(String key, boolean value) {
        preferences.putBoolean(key, value);
        preferences.flush();
    }

    public void putString(String key, String value) {
        preferences.putString(key, value);
        preferences.flush();
    }

    public void clear() {
        preferences.clear();
        preferences.flush();
    }

}
