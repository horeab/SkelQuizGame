package libgdx;

import org.apache.commons.lang3.StringUtils;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
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
import libgdx.implementations.skelgame.gameservice.ImageClickGameService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.implementations.skelgame.gameservice.UniqueAnswersQuizGameService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.utils.EnumUtils;
import libgdx.utils.startgame.test.DefaultAppInfoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class GameServiceTest extends TestMain {

    public void testQuestions() throws Exception {
        CampaignGameDependencyManager subGameDependencyManager = CampaignGame.getInstance().getSubGameDependencyManager();
        for (Language lang : startFromLanguage(getStartLanguage())) {
            QuestionCreator quizQuestionCreator = createQuestionsCreator(lang.name());
            for (QuestionCategory categoryEnum : (QuestionCategory[]) EnumUtils.getValues(subGameDependencyManager.getQuestionCategoryTypeEnum())) {
                List<Question> questions = quizQuestionCreator.getAllQuestions(Arrays.asList(getQuestionDifficulties(categoryEnum)), categoryEnum);
                assertAllQuestions(lang, questions);
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

    public void assertAllQuestions(Language lang, List<Question> questions) {
        for (Question question : questions) {
//            System.out.println(question.getQuestionString());
            printInfo(question.getQuestionString());
            String[] split = question.getQuestionString().split(":");
            //question id is same as the image
            GameService gameService = GameServiceContainer.getGameService(CreatorDependenciesContainer.getCreator(question.getQuestionCategory().getCreatorDependencies()).getGameServiceClass(), question);
            int imageToBeDisplayedPositionInString = gameService.getImageToBeDisplayedPositionInString();
            if (split.length == imageToBeDisplayedPositionInString + 1 && StringUtils.isNotBlank(split[imageToBeDisplayedPositionInString])) {
                String imageId = split[split.length - 1];
                assertTrue(lang + " - " + question.getQuestionString(), Integer.parseInt(imageId) == question.getQuestionCategory().getIndex());
                assertNotNull(lang + " - " + question.getQuestionString(), gameService.getQuestionImage());
            }
            String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
            if (StringUtils.isNotBlank(questionToBeDisplayed) && !notLatinLangs(lang)) {
                assertTrue(lang + " - " + questionToBeDisplayed, removeDiacritics(questionToBeDisplayed).matches(getQuestionsRegex()));
            }
            try {
                assertAnswerOptions(lang, gameService);
            } catch (Exception e) {
                fail(e.getMessage() + " - " + lang + " - " + question.getQuestionString());
            }
        }
    }

    private boolean notLatinLangs(Language language) {
        return Arrays.asList(Language.el, Language.hi, Language.ja, Language.ko, Language.ru, Language.th, Language.uk, Language.zh).contains(language);
    }

    private void printInfo(String info) {
        System.out.println(info.split(":")[2]);
    }

    public void assertAnswerOptions(Language lang, GameService gameService) {
        if (gameService instanceof DependentAnswersQuizGameService) {
            for (String answer : gameService.getAllAnswerOptions()) {
                assertTrue(answer.length() >= 2 || answer.equals("Ý"));//Ý is Italy in vi
            }
//            System.out.println(gameService.getAllAnswerOptions().toString());
            assertTrue(((DependentAnswersQuizGameService) gameService).getAnswers().size() == 1);
            assertEquals(lang + " - " + gameService.getAllAnswerOptions().toString(), 4, gameService.getAllAnswerOptions().size());
        } else if (gameService instanceof ImageClickGameService) {
            assertEquals(4, gameService.getAllAnswerOptions().size());
        } else if (gameService instanceof UniqueAnswersQuizGameService) {
            assertTrue(gameService.getAllAnswerOptions().size() > 2);
            assertTrue(((UniqueAnswersQuizGameService) gameService).getAnswers().size() == 1);
        }
        for (String answer : gameService.getAllAnswerOptions()) {
            assertTrue(StringUtils.isNotBlank(answer));
            if (!notLatinLangs(lang)) {
                assertTrue(lang + " - " + answer, removeDiacritics(answer).matches(getAnswersRegex()));
            }
        }
    }

    public String getQuestionsRegex() {
        return "^[a-zA-Z0-9, \\-\\'\\?\\.\\/\\,\\(\\)\\\"\\%]*$";
    }

    public String getAnswersRegex() {
        return "^[a-zA-Z0-9#, \\-\\'\\/\\.\\;\\(\\)\\,\\\"\\%]*$";
    }

    protected Language getStartLanguage() {
        return Language.cs;
    }

    protected String removeDiacritics(String string) {
        return string.replaceAll("[^\\p{ASCII}]", "");
    }

    private List<Language> startFromLanguage(Language language) {
        List<Language> res = new ArrayList<>();
        boolean foundStart = false;
        for (Language lang : Language.values()) {
            if (lang == language) {
                foundStart = true;
            }
            if (foundStart) {
                res.add(lang);
            }
        }
        return res;
    }

    protected abstract DefaultAppInfoService getAppInfoService();

    protected abstract void testAllQuestions() throws Exception;

}
