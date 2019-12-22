package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.game.Game;
import libgdx.implementations.skelgame.question.Question;
import libgdx.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QuestionCreator {

    private QuestionDifficulty questionDifficulty;
    private QuestionCategory questionCategory;
    private QuestionConfigFileHandler configFileHandler;

    public QuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        this.questionDifficulty = questionDifficulty;
        this.questionCategory = questionCategory;
        this.configFileHandler = getConfigFileHandler();
    }

    public QuestionCreator() {
        this.configFileHandler = getConfigFileHandler();
    }

    public Question createRandomQuestion() {
        Integer randomLine = getRandomLine(questionDifficulty, questionCategory);
        return new Question(randomLine, questionDifficulty, questionCategory, getQuestionStringForQuestionId(randomLine, questionDifficulty, questionCategory));
    }

    private String getQuestionStringForQuestionId(int lineNrFromQuestionId, QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        StringBuilder text = new StringBuilder();
        Scanner scanner = new Scanner(configFileHandler.getFileText(difficultyLevelToCreate, categoryEnumToCreate));
        int lineNr = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (lineNrFromQuestionId == lineNr) {
                text.append(line);
                break;
            }
            lineNr++;
        }
        scanner.close();
        return StringUtils.capitalize(text.toString().trim());
    }


    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        CampaignGameDependencyManager subGameDependencyManager = (CampaignGameDependencyManager) Game.getInstance().getSubGameDependencyManager();
        for (QuestionDifficulty difficulty : (QuestionDifficulty[]) EnumUtils.getValues(subGameDependencyManager.getQuestionDifficultyTypeEnum())) {
            for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(subGameDependencyManager.getQuestionCategoryTypeEnum())) {
                Scanner scanner = new Scanner(configFileHandler.getFileText(difficulty, category));
                int ind = 0;
                while (scanner.hasNextLine()) {
                    questions.add(new Question(ind, difficulty, category, getQuestionStringForQuestionId(ind, difficulty, category)));
                    ind++;
                    scanner.nextLine();
                }
            }
        }
        return questions;
    }

    public List<Question> getAllQuestionsOnLineNr(int lineNr) {
        List<Question> questions = new ArrayList<>();
        CampaignGameDependencyManager subGameDependencyManager = (CampaignGameDependencyManager) Game.getInstance().getSubGameDependencyManager();
        for (QuestionDifficulty difficulty : (QuestionDifficulty[]) EnumUtils.getValues(subGameDependencyManager.getQuestionDifficultyTypeEnum())) {
            for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(subGameDependencyManager.getQuestionCategoryTypeEnum())) {
                Scanner scanner = new Scanner(configFileHandler.getFileText(difficulty, category));
                int ind = 0;
                while (scanner.hasNextLine()) {
                    if (ind == lineNr) {
                        questions.add(new Question(lineNr, difficulty, category, getQuestionStringForQuestionId(lineNr, difficulty, category)));
                        break;
                    }
                    ind++;
                    scanner.nextLine();
                }
            }
        }
        return questions;
    }

    public List<Question> getAllQuestions(List<QuestionDifficulty> difficultyLevels, QuestionCategory categoryEnumToCreate) {
        List<Question> questions = new ArrayList<>();
        for (QuestionDifficulty difficultyLevel : difficultyLevels) {
            Scanner scanner = new Scanner(configFileHandler.getFileText(difficultyLevel, categoryEnumToCreate));
            int lineNr = 0;
            while (scanner.hasNextLine()) {
                questions.add(new Question(lineNr, difficultyLevel, categoryEnumToCreate, getQuestionStringForQuestionId(lineNr, difficultyLevel, categoryEnumToCreate)));
                lineNr++;
                scanner.nextLine();
            }
            scanner.close();
        }
        return questions;
    }

    protected boolean isQuestionValid(Question question) {
        return true;
    }

    private Integer getRandomLine(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        Scanner scanner = new Scanner(configFileHandler.getFileText(difficultyLevelToCreate, categoryEnumToCreate));
        int count = 0;
        while (scanner.hasNextLine()) {
            count++;
            scanner.nextLine();
        }
        return new Random().nextInt(count);
    }

    protected QuestionConfigFileHandler getConfigFileHandler() {
        return new QuestionConfigFileHandler();
    }
}
