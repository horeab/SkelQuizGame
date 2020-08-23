package libgdx.implementations.screens.implementations.periodictable;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.periodictable.PeriodicTableCampaignLevelEnum;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableDifficultyLevel;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.model.FontConfig;

public class PeriodicTableContainers {

    public Table createAllElementsFound() {
        String allQPlayed = SpecificPropertiesUtils.getText("periodictable_elements_discovered", getTotalNrOfElementsFound() + "/" + PeriodicTableCampaignLevelEnum.values().length);
        MyWrappedLabel allQuestions = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE)).setText(allQPlayed).build());
        return allQuestions;
    }


    public Table createAllQuestionsFound() {
        String allQPlayed = SpecificPropertiesUtils.getText("periodictable_elements_discovered", getTotalNrOfElementsFound() + "/" + PeriodicTableCampaignLevelEnum.values().length);
        MyWrappedLabel allQuestions = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getBigFontDim()).setText(allQPlayed).build());
        return allQuestions;
    }

    public static int getTotalNrOfElementsFound() {
        int totalNrOfElementsFound = 0;
        for (int i = 0; i < PeriodicTableCampaignLevelEnum.values().length; i++) {
            totalNrOfElementsFound = totalNrOfElementsFound + (isElementFound(i) ? 1 : 0);
        }
        return totalNrOfElementsFound;
    }

    public static boolean isElementFound(int atomicNr) {
        return getTotalCorrectAnswersForElement(atomicNr) == PeriodicTableCategoryEnum.values().length;
    }

    public static int getTotalCorrectAnswersForElement(int atomicNr) {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        int totalAnswers = 0;
        for (PeriodicTableDifficultyLevel difficultyLevel : PeriodicTableDifficultyLevel.values()) {
            for (PeriodicTableCategoryEnum categoryEnum : PeriodicTableCategoryEnum.values()) {
                boolean questionAlreadyPlayed = campaignStoreService.isQuestionAlreadyPlayed(getQuestionId(atomicNr, categoryEnum, difficultyLevel));
                if (questionAlreadyPlayed) {
                    totalAnswers++;
                }
            }
        }
        return totalAnswers;
    }

    public static String getQuestionId(int questionLineInQuestionFile, QuestionCategory categName, QuestionDifficulty diffName) {
        return questionLineInQuestionFile + "_" + categName.name() + "_" + diffName.name();
    }
}
