package libgdx.implementations.hangmanarena;

import libgdx.campaign.*;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HangmanArenaDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
                Scanner scanner = new Scanner(questionConfigFileHandler.getFileText(difficultyLevel, category));
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine());
                }
                scanner.close();
            }
        }
        return text.toString();
    }


    @Override
    public String getExtraContentProductId() {
        return "extracontent.hangmanclassic";
    }

    @Override
    public Class<HangmanArenaSpecificResource> getSpecificResourceTypeEnum() {
        return HangmanArenaSpecificResource.class;
    }

    @Override
    public Class<HangmanArenaCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return HangmanArenaCampaignLevelEnum.class;
    }

    @Override
    public Class<HangmanArenaQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return HangmanArenaQuestionCategoryEnum.class;
    }

    @Override
    public Class<HangmanArenaQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return HangmanArenaQuestionDifficultyLevel.class;
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
