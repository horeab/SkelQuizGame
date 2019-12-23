package libgdx.screens.implementations.judetelerom;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomDifficultyLevel;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public class JudeteContainers {

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
            totalNrOfJudetFound = totalNrOfJudetFound + (isJudetFound(i) ? 1 : 0);
        }
        return totalNrOfJudetFound;
    }

    public static boolean isJudetFound(int judeNr) {
        return getTotalCorrectAnswersForJudet(judeNr) == JudeteleRomCategoryEnum.values().length;
    }

    public static int getTotalCorrectAnswersForJudet(int judeNr) {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        int totalAnswers = 0;
        for (JudeteleRomDifficultyLevel difficultyLevel : JudeteleRomDifficultyLevel.values()) {
            for (JudeteleRomCategoryEnum categoryEnum : JudeteleRomCategoryEnum.values()) {
                boolean questionAlreadyPlayed = campaignStoreService.isQuestionAlreadyPlayed(getQuestionId(judeNr, categoryEnum, difficultyLevel));
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
