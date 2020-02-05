package libgdx.implementations.paintings;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGame;
import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.kennstde.KennstDeQuestionCategoryEnum;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

public class PaintingsDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        list.add(new ImageCategIncrementRes(0, 16, PaintingsQuestionCategoryEnum.cat0, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 15, PaintingsQuestionCategoryEnum.cat1, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 34, PaintingsQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 31, PaintingsQuestionCategoryEnum.cat3, ImageCategIncrementRes.JPG));
        list.add(new ImageCategIncrementRes(0, 13, PaintingsQuestionCategoryEnum.cat4, ImageCategIncrementRes.JPG));
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
    public Class<PaintingsSpecificResource> getSpecificResourceTypeEnum() {
        return PaintingsSpecificResource.class;
    }

    @Override
    public Class<PaintingsCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return PaintingsCampaignLevelEnum.class;
    }

    @Override
    public Class<PaintingsQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return PaintingsQuestionCategoryEnum.class;
    }

    @Override
    public Class<PaintingsQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return PaintingsQuestionDifficultyLevel.class;
    }

    @Override
    public String getExtraContentProductId() {
        return "extracontent.paintings";
    }
    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
