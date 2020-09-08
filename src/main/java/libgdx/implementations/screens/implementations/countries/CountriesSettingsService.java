package libgdx.implementations.screens.implementations.countries;


import libgdx.campaign.CampaignLevel;
import libgdx.implementations.countries.CountriesCampaignLevelEnum;
import libgdx.preferences.SettingsService;

public class CountriesSettingsService extends SettingsService {

    private static final String SELECTED_LEVEL = "selectedLevel";


    CampaignLevel getSelectedLevel() {
        try {
            return CountriesCampaignLevelEnum.valueOf(preferencesService.getPreferences().getString(SELECTED_LEVEL, CountriesCampaignLevelEnum.LEVEL_0_0.getName()));
        } catch (Exception e) {
            preferencesService.clear();
            return getSelectedLevel();
        }
    }

    void setSelectedLevel(CampaignLevel campaignLevelEnum) {
        preferencesService.putString(SELECTED_LEVEL, campaignLevelEnum.getName());
    }
}
