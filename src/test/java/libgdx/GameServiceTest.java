package libgdx;

import org.apache.commons.lang3.StringUtils;
import org.powermock.api.mockito.PowerMockito;

import java.util.Arrays;
import java.util.List;

import libgdx.campaign.CampaignGame;
import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.skelgame.gameservice.CreatorDependenciesContainer;
import libgdx.implementations.skelgame.gameservice.DependentAnswersQuizGameService;
import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.implementations.skelgame.question.Question;
import libgdx.utils.EnumUtils;
import libgdx.utils.startgame.test.DefaultAppInfoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class GameServiceTest extends TestMain{

    public void testQuestions() throws Exception {
        CampaignGameDependencyManager subGameDependencyManager = CampaignGame.getInstance().getSubGameDependencyManager();
        for (Language lang : getAllLang()) {
            QuestionCreator quizQuestionCreator = createQuestionsCreator(lang.name());
            for (QuestionCategory categoryEnum : (QuestionCategory[]) EnumUtils.getValues(subGameDependencyManager.getQuestionCategoryTypeEnum())) {
                List<Question> questions = quizQuestionCreator.getAllQuestions(Arrays.asList(getQuestionDifficulties(categoryEnum)), categoryEnum);
                assertAllQuestions(lang.name(), questions);
            }
        }
    }

    public QuestionDifficulty[] getQuestionDifficulties(QuestionCategory questionCategory) {
        CampaignGameDependencyManager subGameDependencyManager = CampaignGame.getInstance().getSubGameDependencyManager();
        return (QuestionDifficulty[]) EnumUtils.getValues(subGameDependencyManager.getQuestionDifficultyTypeEnum());
    }

    public QuestionCreator createQuestionsCreator(String lang) throws Exception {
        CampaignGameDependencyManager subGameDependencyManager = CampaignGame.getInstance().getSubGameDependencyManager();
        QuestionCreator quizQuestionCreator = new QuestionCreator() {
            @Override
            public QuestionConfigFileHandler getConfigFileHandler() {
                return new QuestionConfigFileHandler() {
                    @Override
                    protected String getLanguage() {
                        return lang;
                    }
                };
            }
        };
        PowerMockito.whenNew(QuestionCreator.class).withAnyArguments().thenReturn(quizQuestionCreator);
        return quizQuestionCreator;
    }

    public void assertAllQuestions(String lang, List<Question> questions) {
        for (Question question : questions) {
//            System.out.println(question.getQuestionString());
            printInfo(question.getQuestionString());
            String[] split = question.getQuestionString().split(":");
            //question id is same as the image
            GameService gameService = GameServiceContainer.getGameService(CreatorDependenciesContainer.getCreator(question.getQuestionCategory().getCreatorDependencies()).getGameServiceClass(), question);
            int imageToBeDisplayedPositionInString = gameService.getImageToBeDisplayedPositionInString();
            if (split.length == imageToBeDisplayedPositionInString + 1 && StringUtils.isNotBlank(split[imageToBeDisplayedPositionInString])) {
                String imageId = split[split.length - 1];
                assertTrue(lang + " - " + question.getQuestionString(), split[0].equals(imageId) || Integer.parseInt(imageId) == 999);
                assertNotNull(lang + " - " + question.getQuestionString(), gameService.getQuestionImage());
            }
            String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
            if (StringUtils.isNotBlank(questionToBeDisplayed)) {
                assertTrue(lang + " - " + questionToBeDisplayed, removeDiacritics(questionToBeDisplayed).matches(getQuestionsRegex()));
            }
            try {
                assertAnswerOptions(lang, gameService);
            } catch (Exception e) {
                fail(e.getMessage() + " - " + lang + " - " + question.getQuestionString());
            }
        }
    }

    private void printInfo(String info) {
        System.out.println(info.split(":")[2]);
    }

    public void assertAnswerOptions(String lang, GameService gameService) {
        if (gameService instanceof DependentAnswersQuizGameService) {
            for (String answer : gameService.getAllAnswerOptions()) {
                assertTrue(answer.length() >= 2);
            }
//            System.out.println(gameService.getAllAnswerOptions().toString());
            assertTrue(((DependentAnswersQuizGameService) gameService).getAnswers().size() == 1);
            assertEquals(gameService.getAllAnswerOptions().toString(), 4, gameService.getAllAnswerOptions().size());
        }
        for (String answer : gameService.getAllAnswerOptions()) {
            assertTrue(StringUtils.isNotBlank(answer));
            assertTrue(lang + " - " + answer, removeDiacritics(answer).matches(getAnswersRegex()));
        }
    }

    public String getQuestionsRegex() {
        return "^[a-zA-Z0-9, \\-\\'\\?\\.\\/\\,\\(\\)\\\"\\%]*$";
    }

    public String getAnswersRegex() {
        return "^[a-zA-Z0-9#, \\-\\'\\/\\.\\;\\(\\)\\,\\\"\\%]*$";
    }

    protected List<Language> getAllLang() {
        return Arrays.asList(Language.values());
    }

    protected String removeDiacritics(String string) {
        return string.replaceAll("[^\\p{ASCII}]", "");
    }

    protected abstract DefaultAppInfoService getAppInfoService();

    protected abstract void testAllQuestions() throws Exception;

}
