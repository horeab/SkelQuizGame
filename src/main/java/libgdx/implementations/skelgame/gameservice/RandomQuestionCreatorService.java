package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfig;
import libgdx.campaign.RandomCategoryAndDifficulty;
import libgdx.implementations.skelgame.question.Question;

import java.util.Arrays;

public class RandomQuestionCreatorService {

    public Question[] createRandomQuestions(QuestionConfig questionConfig) {
        int questionAmount = questionConfig.getAmountOfQuestions();
        Question[] randomQuestions = new Question[questionAmount];
        for (int i = 0; i < questionAmount; i++) {
            RandomCategoryAndDifficulty randomCategoryAndDifficulty = questionConfig.getRandomCategoryAndDifficulty();
            QuizQuestionCategory randomQuestionCategory = (QuizQuestionCategory) randomCategoryAndDifficulty.getQuestionCategory();
            QuestionCreator questionCreator = CreatorDependenciesContainer.getCreator(randomQuestionCategory.getCreatorDependencies()).getQuestionCreator(randomCategoryAndDifficulty.getQuestionDifficulty(), randomQuestionCategory);
            int repeats = 0;
            Question randomQuestion = questionCreator.createRandomQuestion();
            while (Arrays.asList(randomQuestions).contains(randomQuestion) || !questionCreator.isQuestionValid(randomQuestion)) {
                if (repeats > 100) {
                    break;
                }
                randomQuestion = questionCreator.createRandomQuestion();
                repeats++;
            }
            randomQuestions[i] = randomQuestion;
        }
        return randomQuestions;
    }
}
