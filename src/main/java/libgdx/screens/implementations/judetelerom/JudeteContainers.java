package libgdx.screens.implementations.judetelerom;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomDifficultyLevel;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.GameLabelUtils;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public class JudeteContainers {

    public Table createAllJudeteFound() {
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
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        boolean isJudetFound = true;
        for (JudeteleRomDifficultyLevel difficultyLevel : JudeteleRomDifficultyLevel.values()) {
            for (JudeteleRomCategoryEnum categoryEnum : JudeteleRomCategoryEnum.values()) {
                boolean questionAlreadyPlayed = campaignStoreService.isQuestionAlreadyPlayed(getQuestionId(judeNr, categoryEnum.name(), difficultyLevel.name()));
                if (!questionAlreadyPlayed) {
                    isJudetFound = false;
                    break;
                }
            }
        }
        return isJudetFound;
    }

    public static String getQuestionId(int questionLineInQuestionFile, String categName, String diffName) {
        return questionLineInQuestionFile + "_" + categName + "_" + diffName;
    }
}
