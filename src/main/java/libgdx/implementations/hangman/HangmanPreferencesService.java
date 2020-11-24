package libgdx.implementations.hangman;


import libgdx.preferences.SettingsService;

public class HangmanPreferencesService extends SettingsService {


    public HangmanPreferencesService() {
//        preferencesService.clear();
    }


    public void setHighScore(int score) {
        preferencesService.putInteger(getHighScoreFieldName(), score);
    }

    public int getHighScore() {
        return preferencesService.getPreferences().getInteger(getHighScoreFieldName(), 0);
    }

    private String getHighScoreFieldName() {
        return "HangmanPreferencesServiceHighScore";
    }


}
