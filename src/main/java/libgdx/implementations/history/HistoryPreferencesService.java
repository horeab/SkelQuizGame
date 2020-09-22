package libgdx.implementations.history;


import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import libgdx.preferences.SettingsService;

public class HistoryPreferencesService extends SettingsService {

    private static final String LEVELS_WON = "levelsWon";
    private static final String LEVELS_LOST = "levelsLost";

    public Set<Integer> getLevelsWon() {
        return convertToInt(LEVELS_WON);
    }

    public Set<Integer> getLevelsLost() {
        return convertToInt(LEVELS_LOST);
    }

    public Set<Integer> getAllLevelsPlayed() {
        Set<Integer> all = new HashSet<>();
        all.addAll(getLevelsLost());
        all.addAll(getLevelsWon());
        return all;
    }

    private Set<Integer> convertToInt(String fieldName) {
        String[] list = preferencesService.getPreferences().getString(fieldName, "").split(",");
        Set<Integer> res = new HashSet<>();
        for (String i : list) {
            if (StringUtils.isNotBlank(i)) {
                res.add(Integer.parseInt(i));
            }
        }
        return res;
    }

    public void setLevelWon(int i) {
        Set<Integer> l = getLevelsWon();
        l.add(i);
        preferencesService.putString(LEVELS_WON, StringUtils.join(l, ","));
    }

    public void setLevelLost(int i) {
        Set<Integer> l = getLevelsWon();
        l.add(i);
        preferencesService.putString(LEVELS_LOST, StringUtils.join(l, ","));
    }

}
