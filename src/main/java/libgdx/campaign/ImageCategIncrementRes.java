package libgdx.campaign;

import libgdx.resources.IncrementingRes;

public class ImageCategIncrementRes extends IncrementingRes {

    public final static String JPG = "jpg";
    public final static String PNG = "png";

    private QuestionCategory questionCategory;

    public ImageCategIncrementRes(int beginIndex, int endIndex, QuestionCategory questionCategory, String imgExtension) {
        this(beginIndex, endIndex, questionCategory, imgExtension, "images");
    }


    public ImageCategIncrementRes(int beginIndex, int endIndex, QuestionCategory questionCategory, String imgExtension, String imagesFolderName) {
        super(beginIndex, endIndex,
                CampaignGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + imagesFolderName + "/" + getCategNr(questionCategory) + "/%s." + imgExtension,
                "img_" + getCategNr(questionCategory) + "_%s");
        this.questionCategory = questionCategory;
    }

    public ImageCategIncrementRes(int index, QuestionCategory questionCategory, String imgExtension, String imagesFolderName) {
        this(index, index, questionCategory, imgExtension, imagesFolderName);
    }

    public ImageCategIncrementRes(int index, QuestionCategory questionCategory, String imgExtension) {
        this(index, index, questionCategory, imgExtension);
    }

    private static String getCategNr(QuestionCategory questionCategory) {
        return "cat" + questionCategory.getIndex();
    }

    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }
}
