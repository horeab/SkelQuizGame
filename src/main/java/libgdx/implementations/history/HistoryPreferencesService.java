package libgdx.implementations.history;


import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import libgdx.preferences.SettingsService;

public class HistoryPreferencesService extends SettingsService {


    public HistoryPreferencesService() {
//        preferencesService.clear();
    }

    public Set<Integer> getLevelsWon(HistoryCampaignLevelEnum campaignLevelEnum) {
        return convertToInt(getLevelsWonFieldName(campaignLevelEnum));
    }

    public Set<Integer> getLevelsLost(HistoryCampaignLevelEnum campaignLevelEnum) {
        return convertToInt(getLevelsLostFieldName(campaignLevelEnum));
    }

    public Set<Integer> getAllLevelsPlayed(HistoryCampaignLevelEnum campaignLevelEnum) {
        Set<Integer> all = new HashSet<>();
        all.addAll(getLevelsLost(campaignLevelEnum));
        all.addAll(getLevelsWon(campaignLevelEnum));
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

    public void setLevelWon(int level, HistoryCampaignLevelEnum campaignLevelEnum) {
        Set<Integer> l = getLevelsWon(campaignLevelEnum);
        l.add(level);
        preferencesService.putString(getLevelsWonFieldName(campaignLevelEnum), StringUtils.join(l, ","));
        int levelsWonSize = getLevelsWon(campaignLevelEnum).size();
        if (levelsWonSize > getHighScore(campaignLevelEnum)) {
            setHighScore(levelsWonSize, campaignLevelEnum);
        }
    }

    public void setLevelLost(int level, HistoryCampaignLevelEnum campaignLevelEnum) {
        Set<Integer> l = getLevelsLost(campaignLevelEnum);
        l.add(level);
        preferencesService.putString(getLevelsLostFieldName(campaignLevelEnum), StringUtils.join(l, ","));
    }

    public void setHighScore(int score, HistoryCampaignLevelEnum campaignLevelEnum) {
        preferencesService.putInteger(getHighScoreFieldName(campaignLevelEnum), score);
    }

    public int getHighScore(HistoryCampaignLevelEnum campaignLevelEnum) {
        return preferencesService.getPreferences().getInteger(getHighScoreFieldName(campaignLevelEnum), 0);
    }

    private String getLevelsWonFieldName(HistoryCampaignLevelEnum campaignLevelEnum) {
        return campaignLevelEnum.name() + "levelsWon";
    }

    private String getLevelsLostFieldName(HistoryCampaignLevelEnum campaignLevelEnum) {
        return campaignLevelEnum.name() + "levelsLost";
    }

    private String getHighScoreFieldName(HistoryCampaignLevelEnum campaignLevelEnum) {
        return campaignLevelEnum.name() + "highScore";
    }

    public void clearLevelsPlayed(HistoryCampaignLevelEnum campaignLevelEnum) {
        preferencesService.putString(getLevelsWonFieldName(campaignLevelEnum), "");
        preferencesService.putString(getLevelsLostFieldName(campaignLevelEnum), "");
    }


}
