package libgdx.implementations.screens.implementations.countries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.flags.FlagsCampaignLevelEnum;
import libgdx.implementations.flags.FlagsDifficultyLevel;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.question.Question;
import libgdx.implementations.skelgame.question.QuestionParser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.model.FontColor;

public class CountriesContainers {

    private static Map<CampaignLevel, List<Question>> allQuestions = new HashMap<>();

    public static void reset() {
        allQuestions.clear();
        init();
    }

    public static void init() {
        if (allQuestions == null || allQuestions.isEmpty()) {
            QuestionParser questionParser = new QuestionParser();
            for (CampaignLevel campaignLevel : FlagsCampaignLevelEnum.values()) {
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                if (!allQuestions.containsKey(campaignLevel)) {
                    allQuestions.put(campaignLevel, new ArrayList<>());
                }
                List<Question> allQuestions = questionParser.getAllQuestions(
                        enumService.getDifficultyEnum(),
                        (QuizQuestionCategory) enumService.getCategoryEnum());
                for (Question question : allQuestions) {
                    if (question.getQuestionDifficultyLevel() == enumService.getDifficultyEnum()) {
                        CountriesContainers.allQuestions.get(campaignLevel).add(question);
                    }
                }
            }
        }
    }


    public static MyWrappedLabel createFlagsCounter(int leftCountriesToPlay, float labelWidth, MainResource background) {
        MyWrappedLabel countryCounter = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(FontColor.GRAY)
                .setWrappedLineLabel(labelWidth).setText(SpecificPropertiesUtils.getText("flags_amount", leftCountriesToPlay)).build());
        if (background != null) {
            countryCounter.setBackground(GraphicUtils.getNinePatch(background));
        }
        return countryCounter;
    }

    public static List<Question> getAllQuestions(CampaignLevel campaignLevel) {
        if (allQuestions == null || allQuestions.isEmpty()) {
            init();
        }
        Set<CampaignLevel> campaignLevelsToGetQuestions = new HashSet<>();
        campaignLevelsToGetQuestions.add(campaignLevel);
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
        FlagsCampaignLevelEnum levelEnum0 = FlagsCampaignLevelEnum.valueOf("LEVEL_0_" + enumService.getCategory());
        FlagsCampaignLevelEnum levelEnum1 = FlagsCampaignLevelEnum.valueOf("LEVEL_1_" + enumService.getCategory());
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        if (enumService.getDifficultyEnum() == FlagsDifficultyLevel._2) {
            if (!campaignStoreService.isQuestionAlreadyPlayed(levelEnum0.getName())) {
                campaignLevelsToGetQuestions.add(levelEnum0);
            }
            if (!campaignStoreService.isQuestionAlreadyPlayed(levelEnum1.getName())) {
                campaignLevelsToGetQuestions.add(levelEnum1);
            }
        } else if (enumService.getDifficultyEnum() == FlagsDifficultyLevel._1) {
            if (!campaignStoreService.isQuestionAlreadyPlayed(levelEnum0.getName())) {
                campaignLevelsToGetQuestions.add(levelEnum0);
            }
        }
        ArrayList<Question> questions = new ArrayList<>();
        for (CampaignLevel l : campaignLevelsToGetQuestions) {
            questions.addAll(allQuestions.get(l));
        }
        return questions;
    }
}
