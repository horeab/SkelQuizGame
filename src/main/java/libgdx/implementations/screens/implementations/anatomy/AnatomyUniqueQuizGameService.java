package libgdx.implementations.screens.implementations.anatomy;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.game.Game;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.implementations.skelgame.gameservice.UniqueAnswersQuizGameService;
import libgdx.implementations.skelgame.question.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnatomyUniqueQuizGameService extends UniqueAnswersQuizGameService {

    private QuestionConfigFileHandler fileHandler = new QuestionConfigFileHandler();

    public AnatomyUniqueQuizGameService(Question question) {
        super(question);
    }

    @Override
    protected String[] getCorrectAnswerIds() {
        return new String[]{"0"};
    }

    @Override
    public String getQuestionToBeDisplayed() {
        String questionString = question.getQuestionString();
        return questionString.contains(":") ? questionString.split(":")[getQuestionToBeDisplayedPositionInString()] : questionString;
    }

    @Override
    protected String[] getAnswerOptionsArray() {
        CampaignLevel currentCamp = CampaignLevelEnumService.getCampaignLevelForDiffAndCat(AnatomyQuestionDifficultyLevel._0, question.getQuestionCategory());
        CampaignLevel rootCampaignLevel = AnatomyGameScreen.getRootCampaignLevelForValue(currentCamp);

        String[] answerIndexes = getAnswerIndexes(currentCamp);
        List<String> rootLangWords = getRootLangWords(rootCampaignLevel);

        int length = 0;
        if (answerIndexes != null) {
            length = answerIndexes.length;
        }
        String[] answers = new String[length];

        if (answerIndexes != null) {
            int i = 0;
            for (String index : answerIndexes) {
                answers[i] = rootLangWords.get(Integer.valueOf(index));
                i++;
            }
        }

        return answers;
    }

    private List<String> getRootLangWords(CampaignLevel rootCamp) {
        CampaignLevelEnumService rootCampLevelEnumService = new CampaignLevelEnumService(rootCamp);
        String rootWordsForLangText = fileHandler.getFileText("questions/" + Game.getInstance().getAppInfoService().getLanguage()
                + "/diff0/questions_diff0_cat" + rootCampLevelEnumService.getCategory() + ".txt");
        List<String> langAnswers = new ArrayList<>();
        Scanner scanner = new Scanner(rootWordsForLangText);
        while (scanner.hasNextLine()) {
            langAnswers.add(scanner.nextLine().split(":")[2]);
        }
        return langAnswers;
    }

    private String[] getAnswerIndexes(CampaignLevel currentCamp) {
        CampaignLevelEnumService currentCampLevelEnumService = new CampaignLevelEnumService(currentCamp);
        String engQuestion = fileHandler.getFileText("questions/en/diff0/questions_diff0_cat"
                + currentCampLevelEnumService.getCategory() + ".txt");
        Scanner scanner2 = new Scanner(engQuestion);
        int i = 0;
        while (scanner2.hasNextLine()) {
            if (i == question.getQuestionLineInQuestionFile()) {
                return scanner2.nextLine().split(":")[2].split("##");
            }
            scanner2.nextLine();
            i++;
        }
        return null;
    }
}
