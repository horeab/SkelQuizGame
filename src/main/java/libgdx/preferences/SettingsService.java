package libgdx.preferences;


public class SettingsService {

    protected PreferencesService preferencesService = new PreferencesService("SettingsService");

    public boolean isSoundOn() {
        return preferencesService.getPreferences().getBoolean(getToggleSoundKey(), true);
    }

    public void toggleSound() {
        preferencesService.putBoolean(getToggleSoundKey(), !isSoundOn());
    }

    public boolean isMusicOn() {
        return preferencesService.getPreferences().getBoolean(getToggleMusicKey(), true);
    }

    public void toggleMusic() {
        preferencesService.putBoolean(getToggleMusicKey(), !isMusicOn());
    }


    private String getToggleSoundKey() {
        return "ToggleSoundKey";
    }

    private String getToggleMusicKey() {
        return "ToggleMusicKey";
    }
}
