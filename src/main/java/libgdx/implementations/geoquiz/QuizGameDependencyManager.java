package libgdx.implementations.geoquiz;

import libgdx.campaign.*;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizGameDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        list.add(new ImageCategIncrementRes(0, 34, QuizQuestionCategoryEnum.cat0, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 49, QuizQuestionCategoryEnum.cat1, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 49, QuizQuestionCategoryEnum.cat2, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 49, QuizQuestionCategoryEnum.cat3, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 35, QuizQuestionCategoryEnum.cat4, ImageCategIncrementRes.JPG));
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(QuizGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(QuizGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
        return "extraContent";
    }

    @Override
    public Class<QuizGameSpecificResource> getSpecificResourceTypeEnum() {
        return QuizGameSpecificResource.class;
    }

    @Override
    public Class<QuizCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return QuizCampaignLevelEnum.class;
    }

    @Override
    public Class<QuizQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return QuizQuestionCategoryEnum.class;
    }

    @Override
    public Class<QuizQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return QuizQuestionDifficultyLevel.class;
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
