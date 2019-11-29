package libgdx.implementations.judetelerom;

import libgdx.campaign.*;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JudeteleRomDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        list.add(new ImageCategIncrementRes(0, 40, JudeteleRomCategoryEnum.cat1, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 40, JudeteleRomCategoryEnum.cat2, ImageCategIncrementRes.JPG));
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(JudeteleRomGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(JudeteleRomGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<JudeteleRomSpecificResource> getSpecificResourceTypeEnum() {
        return JudeteleRomSpecificResource.class;
    }

    @Override
    public Class<JudeteleRomCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return JudeteleRomCampaignLevelEnum.class;
    }

    @Override
    public Class<JudeteleRomCategoryEnum> getQuestionCategoryTypeEnum() {
        return JudeteleRomCategoryEnum.class;
    }

    @Override
    public Class<JudeteleRomDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return JudeteleRomDifficultyLevel.class;
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
