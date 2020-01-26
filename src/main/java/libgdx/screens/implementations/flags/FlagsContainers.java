package libgdx.screens.implementations.flags;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.flags.FlagsCategoryEnum;
import libgdx.implementations.flags.FlagsDifficultyLevel;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public class FlagsContainers {

    public Table createAllJudeteFound() {
        String allQPlayed = getTotalNrOfJudetFound() + "/" + JudeteleRomCampaignLevelEnum.values().length + " " + SpecificPropertiesUtils.getText("ro_judetelerom_found");
        MyWrappedLabel allQuestions = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(allQPlayed).build());
        return allQuestions;
    }


    public Table createAllQuestionsFound() {
        String allQPlayed = getTotalNrOfJudetFound() + "/" + JudeteleRomCampaignLevelEnum.values().length + " " + SpecificPropertiesUtils.getText("ro_judetelerom_found");
        MyWrappedLabel allQuestions = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(allQPlayed).build());
        return allQuestions;
    }

    public static int getTotalNrOfJudetFound() {
        int totalNrOfJudetFound = 0;
        for (int i = 0; i < JudeteleRomCampaignLevelEnum.values().length; i++) {
            totalNrOfJudetFound = totalNrOfJudetFound + (isContinentFound(i) ? 1 : 0);
        }
        return totalNrOfJudetFound;
    }

    public static boolean isContinentFound(int continentNr) {
        return getTotalCorrectAnswersForContinent(continentNr) == FlagsCategoryEnum.values().length;
    }

    public static int getTotalCorrectAnswersForContinent(int continentNr) {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        int totalAnswers = 0;
        for (FlagsDifficultyLevel difficultyLevel : FlagsDifficultyLevel.values()) {
            for (FlagsCategoryEnum categoryEnum : FlagsCategoryEnum.values()) {
                boolean questionAlreadyPlayed = campaignStoreService.isQuestionAlreadyPlayed(getQuestionId(continentNr, categoryEnum, difficultyLevel));
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
