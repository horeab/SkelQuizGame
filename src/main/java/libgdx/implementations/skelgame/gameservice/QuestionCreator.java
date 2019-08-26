package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.skelgame.question.Question;
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
