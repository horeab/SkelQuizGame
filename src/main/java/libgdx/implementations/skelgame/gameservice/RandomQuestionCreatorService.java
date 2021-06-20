package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionConfig;
import libgdx.campaign.RandomCategoryAndDifficulty;
import libgdx.implementations.skelgame.question.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomQuestionCreatorService {

    public Question[] createRandomQuestions(QuestionConfig questionConfig) {
        int questionAmount = questionConfig.getAmountOfQuestions();
        List<String> categsToUse = new ArrayList<>(questionConfig.getQuestionCategoryStringList());
        List<String> alreadyUsedCategs = new ArrayList<>();
        Question[] randomQuestions = new Question[questionAmount];
        for (int i = 0; i < questionAmount; i++) {
            RandomCategoryAndDifficulty randomCategoryAndDifficulty = questionConfig.getRandomCategoryAndDifficulty();
            int repeats1 = 0;
            if (!configContainsSingleCategAndDiff(questionConfig)) {
                while (alreadyUsedCategs.contains(randomCategoryAndDifficulty.getQuestionCategory().name())) {
                    randomCategoryAndDifficulty = questionConfig.getRandomCategoryAndDifficulty();
                    if (repeats1 > 100) {
                        break;
                    }
                    repeats1++;
                }
            }
            QuizQuestionCategory randomQuestionCategory = (QuizQuestionCategory) randomCategoryAndDifficulty.getQuestionCategory();
            QuestionCreator questionCreator = CreatorDependenciesContainer.getCreator(randomQuestionCategory.getCreatorDependencies()).getQuestionCreator(randomCategoryAndDifficulty.getQuestionDifficulty(), randomQuestionCategory);
            int repeats2 = 0;
            Question randomQuestion = questionCreator.createRandomQuestion();
            while (Arrays.asList(randomQuestions).contains(randomQuestion)
                    || !questionCreator.isQuestionValid(randomQuestion)
                    //try to use all question categories
                    || (!configContainsSingleCategAndDiff(questionConfig)
                    && alreadyUsedCategs.contains(randomQuestion.getQuestionCategory().name()) && !categsToUse.isEmpty())) {
                if (repeats2 > 100) {
                    break;
                }
                randomQuestion = questionCreator.createRandomQuestion();
                repeats2++;
            }
            randomQuestions[i] = randomQuestion;
            String qCategName = randomQuestion.getQuestionCategory().name();
            alreadyUsedCategs.add(qCategName);
            categsToUse.remove(qCategName);
        }
        return randomQuestions;
    }

    private boolean configContainsSingleCategAndDiff(QuestionConfig questionConfig) {
        return questionConfig.getQuestionDifficultyStringList().size() == 1
                && questionConfig.getQuestionCategoryStringList().size() == 1;
    }
}
