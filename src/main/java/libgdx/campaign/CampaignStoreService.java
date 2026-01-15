package libgdx.campaign;

import java.util.ArrayList;
import java.util.List;

import libgdx.preferences.PreferencesService;
import libgdx.utils.EnumUtils;

public class CampaignStoreService {

    private static final String CAMPAIGN_LEVEL = "CampaignLevel";
    private static final String PREF_NAME = "campaignStoreService";
    public static final String TEXT_SPLIT = "#";

    private PreferencesService preferencesService = new PreferencesService(PREF_NAME);

    public CampaignStoreService() {
//        this.preferencesService.clear();
    }

    void createCampaignLevel(CampaignLevel campaignLevelEnum) {
        preferencesService.putInteger(formCampaignLevelKey(campaignLevelEnum), -1);
    }

    public void reset() {
        long maxStars = getAllScoreWon();
        this.preferencesService.clear();
        updateAllScoreWon(maxStars);
    }

    private String formCampaignLevelKey(CampaignLevel campaignLevelEnum) {
        return CAMPAIGN_LEVEL + campaignLevelEnum.getIndex();
    }

    List<CampaignStoreLevel> getAllCampaignLevels() {
        ArrayList<CampaignStoreLevel> levels = new ArrayList<>();
        for (CampaignLevel levelEnum : (CampaignLevel[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum())) {
            int val = preferencesService.getPreferences().getInteger(formCampaignLevelKey(levelEnum), -1);
            if (val != -1) {
                CampaignStoreLevel level = new CampaignStoreLevel(levelEnum);
                level.setScore(getScoreWon(levelEnum));
                level.setStatus(preferencesService.getPreferences().getInteger(formCampaignLevelStatusKey(levelEnum)));
                levels.add(level);
            }
        }
        return levels;
    }

    public void putJson(String json) {
        preferencesService.putString(formJsonKey(), json);
    }

    public String getJson() {
        return preferencesService.getPreferences().getString(formJsonKey(), "");
    }

    public void putQuestionPlayed(String questionId) {
        preferencesService.putString(formQuestionPlayedKey(), getAllQuestionsPlayed() + TEXT_SPLIT + questionId);
    }

    public void putHintPlayedAtQuestion() {
        preferencesService.putInteger(formHintPlayedKey(), getNrOfQuestionsPlayed());
    }

    public int getHintPlayedAtQuestion() {
        return preferencesService.getPreferences().getInteger(formHintPlayedKey(), -1);
    }


    public boolean isQuestionAlreadyPlayed(String questionId) {
        return getAllQuestionsPlayed().contains(TEXT_SPLIT + questionId);
    }

    public String getAllQuestionsPlayed() {
        return preferencesService.getPreferences().getString(formQuestionPlayedKey(), "");
    }

    public void incrementNrOfQuestionsPlayed() {
        preferencesService.putInteger(formNrOfQuestionsPlayedKey(), getNrOfQuestionsPlayed() + 1);
    }

    public int getNrOfQuestionsPlayed() {
        return preferencesService.getPreferences().getInteger(formNrOfQuestionsPlayedKey(), 0);
    }

    public long getAllScoreWon() {
        return preferencesService.getPreferences().getLong(formAllStarsWonKey());
    }

    public void updateAllScoreWon(long scoreWon) {
        preferencesService.putLong(formAllStarsWonKey(), scoreWon);
    }

    public long getScoreWon(CampaignLevel levelEnum) {
        return preferencesService.getPreferences().getLong(formCampaignLevelScoreWonKey(levelEnum));
    }

    public void levelFinishedCampaign(CampaignLevel campaignLevelEnum, long scoreWon) {
        updateStatus(campaignLevelEnum, CampaignLevelStatusEnum.FINISHED);
        updateScoreWon(campaignLevelEnum, scoreWon);
    }


    public void updateScoreWon(CampaignLevel campaignLevelEnum, long scoreWon) {
        preferencesService.putLong(formCampaignLevelScoreWonKey(campaignLevelEnum), scoreWon);
    }

    Integer getCrosswordLevel(CampaignLevel campaignLevelEnum) {
        return preferencesService.getPreferences().getInteger(formCampaignLevelKey(campaignLevelEnum), -1);
    }

    void updateLevel(CampaignLevel campaignLevelEnum) {
        preferencesService.putInteger(formCampaignLevelKey(campaignLevelEnum), campaignLevelEnum.getIndex());
    }

    void updateStatus(CampaignLevel campaignLevelEnum, CampaignLevelStatusEnum campaignLevelStatusEnum) {
        preferencesService.putInteger(formCampaignLevelStatusKey(campaignLevelEnum), campaignLevelStatusEnum.getStatus());
    }

    private String formJsonKey() {
        return "JsonKey(";
    }

    private String formQuestionPlayedKey() {
        return "QuestionIdPlayed";
    }

    private String formHintPlayedKey() {
        return "HintPlayed";
    }

    private String formNrOfQuestionsPlayedKey() {
        return "NrOfQuestionsPlayed";
    }

    private String formAllStarsWonKey() {
        return "AllScoreWon";
    }

    private String formCampaignLevelScoreWonKey(CampaignLevel campaignLevelEnum) {
        return formCampaignLevelKey(campaignLevelEnum) + "ScoreWon";
    }

    private String formCampaignLevelStatusKey(CampaignLevel campaignLevelEnum) {
        return formCampaignLevelKey(campaignLevelEnum) + "CampaignLevelStatusEnum";
    }

}
