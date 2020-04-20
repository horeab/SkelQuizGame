package libgdx.implementations.kennstde;

import libgdx.campaign.*;
import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KennstDeDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        if (Game.getInstance().getAppInfoService().getLanguage().equals(Language.ro.name())) {
            //RO
            list.add(new ImageCategIncrementRes(1, KennstDeQuestionCategoryEnum.cat0, ImageCategIncrementRes.JPG, "images_ro"));
            list.add(new ImageCategIncrementRes(0, 9, KennstDeQuestionCategoryEnum.cat3, ImageCategIncrementRes.JPG, "images_ro"));
            list.add(new ImageCategIncrementRes(0, 11, KennstDeQuestionCategoryEnum.cat4, ImageCategIncrementRes.JPG, "images_ro"));
        } else {
            list.add(new ImageCategIncrementRes(0, 1, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(3, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(6, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(8, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(13, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(15, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));
            list.add(new ImageCategIncrementRes(17, KennstDeQuestionCategoryEnum.cat2, ImageCategIncrementRes.JPG));

            list.add(new ImageCategIncrementRes(0, 14, KennstDeQuestionCategoryEnum.cat3, ImageCategIncrementRes.JPG));

            list.add(new ImageCategIncrementRes(0, 14, KennstDeQuestionCategoryEnum.cat3, ImageCategIncrementRes.JPG));
        }
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
    public Class<KennstDeSpecificResource> getSpecificResourceTypeEnum() {
        return KennstDeSpecificResource.class;
    }

    @Override
    public Class<KennstDeCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return KennstDeCampaignLevelEnum.class;
    }

    @Override
    public Class<KennstDeQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return KennstDeQuestionCategoryEnum.class;
    }

    @Override
    public Class<KennstDeQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return KennstDeQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
