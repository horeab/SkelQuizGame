package libgdx.campaign;

import libgdx.controls.button.ButtonSkin;
import libgdx.game.Game;
import libgdx.resources.Res;
import libgdx.resources.SpecificResource;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.EnumUtils;


public class CampaignLevelEnumService {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private CampaignLevel campaignLevel;

    public CampaignLevelEnumService(CampaignLevel campaignLevel) {
        this.campaignLevel = campaignLevel;
    }

    public static CampaignLevel getCampaignLevelForDiffAndCat(QuestionDifficulty difficultyLevel, QuestionCategory category) {
        return (CampaignLevel) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum(), "LEVEL_" + difficultyLevel.getIndex() + "_" + category.getIndex());
    }

    public static CampaignLevel getNextLevel(CampaignLevel currentCampaignLevelEnum) {
        CampaignLevel[] values = (CampaignLevel[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        for (CampaignLevel campaignLevelEnum : values) {
            if (currentCampaignLevelEnum != null && campaignLevelEnum.getIndex() == currentCampaignLevelEnum.getIndex() + 1) {
                return campaignLevelEnum;
            }
        }
        return null;
    }

    public String getLabelText() {
        return getCategory() != null ? new SpecificPropertiesUtils().getQuestionCategoryLabel(getCategory()) : null;
    }

    public <T extends Enum & ButtonSkin> T getButtonSkin(Class<T> enumClass) {
        return getCategory() != null ? EnumUtils.getEnumValue(enumClass, "CAMPAIGN_LEVEL_" + getDifficulty()) : EnumUtils.getEnumValue(enumClass, "CAMPAIGN_LEVEL_WALL");
    }

    public QuestionConfig getQuestionConfig(int nrOfQuestions, int nrOfHints) {
        QuestionDifficulty difficulty = getDifficultyEnum();
        QuestionConfig questionConfig;
        if (getCategory() != null) {
            QuestionCategory category = getCategoryEnum();
            questionConfig = new QuestionConfig(difficulty, category);
        } else {
            questionConfig = new QuestionConfig(difficulty);
        }
        questionConfig.setH(nrOfHints);
        questionConfig.setA(nrOfQuestions);
        return questionConfig;
    }

    public QuestionDifficulty getDifficultyEnum() {
        return (QuestionDifficulty) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum(), "_" + getDifficulty());
    }

    public QuestionCategory getCategoryEnum() {
        return (QuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), "cat" + getCategory());
    }

    public static QuestionCategory getCategoryEnum(String name) {
        return (QuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), "cat" + getCategory(name));
    }

    public QuestionConfig getQuestionConfig(int nrOfQuestions) {
        return getQuestionConfig(nrOfQuestions, 0);
    }

    public GameTypeStage getGameTypeStage() {
        Integer category = getCategory();
        if (category != null && category > 4) {
            category = category - 5 * getDifficulty();
        }
        return EnumUtils.getEnumValue(GameTypeStage.class, "CAMPAIGN_LEVEL_" + getDifficulty() + (category != null ? "_" + category : ""));
    }

    public int getDifficulty() {
        return Integer.valueOf(getSplit(campaignLevel.getName())[1]);
    }

    public Integer getCategory() {
        return getCategory(campaignLevel.getName());
    }

    public Res getIcon() {
        return (SpecificResource) EnumUtils.getEnumValue(Game.getInstance().getSubGameDependencyManager().getSpecificResourceTypeEnum(), "campaign_level_" + getDifficulty() + "_" + getCategory());
    }

    private static String[] getSplit(String name) {
        return name.split("_");
    }

    public static Integer getCategory(String name) {
        String[] split = getSplit(name);
        return split.length == 3 ? Integer.valueOf(split[2]) : null;
    }

}
