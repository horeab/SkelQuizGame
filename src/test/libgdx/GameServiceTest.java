package libgdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.controls.popup.RatingService;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.LoginService;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.Question;
import libgdx.preferences.PreferencesService;
import libgdx.preferences.SettingsService;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.startgame.test.DefaultLoginService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpriteBatch.class, RatingService.class, SettingsService.class, AbstractScreen.class, PreferencesService.class})
public abstract class GameServiceTest implements ApplicationListener {

    public void testQuestions() throws Exception {
        CampaignGameDependencyManager subGameDependencyManager = QuizGame.getInstance().getSubGameDependencyManager();
        for (Language lang : getAllLang()) {
            QuestionCreator quizQuestionCreator = createQuestionsCreator(lang.name());
            for (QuestionCategory categoryEnum : (QuestionCategory[]) EnumUtils.getValues(subGameDependencyManager.getQuestionCategoryTypeEnum())) {
                List<Question> questions = quizQuestionCreator.getAllQuestions(Arrays.asList(getQuestionDifficulties(categoryEnum)), categoryEnum);
                assertAllQuestions(lang.name(), questions);
            }
        }
    }

    protected Language getStartLang(){
        return Language.cs;
    }

    protected List<Language> getAllLang() {
        List<Language> langs = new ArrayList<>();
        boolean fLang = false;
        for (Language l : Language.values()) {
            if (l == getStartLang()) {
                fLang = true;
            }
            if (fLang) {
                langs.add(l);
            }
        }
        return langs;
    }

    public QuestionDifficulty[] getQuestionDifficulties(QuestionCategory questionCategory) {
        CampaignGameDependencyManager subGameDependencyManager = QuizGame.getInstance().getSubGameDependencyManager();
        return (QuestionDifficulty[]) EnumUtils.getValues(subGameDependencyManager.getQuestionDifficultyTypeEnum());
    }

    public QuestionCreator createQuestionsCreator(String lang) throws Exception {
        CampaignGameDependencyManager subGameDependencyManager = QuizGame.getInstance().getSubGameDependencyManager();
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
            GameService gameService = GameServiceContainer.getGameService(question);
            int imageToBeDisplayedPositionInString = gameService.getImageToBeDisplayedPositionInString();
            if (split.length == imageToBeDisplayedPositionInString + 1 && StringUtils.isNotBlank(split[imageToBeDisplayedPositionInString])) {
                String imageId = split[split.length - 1];
                assertTrue(lang + " - " + question.getQuestionString(), split[0].equals(imageId) || Integer.parseInt(imageId) == 999 || Integer.parseInt(imageId) == 0);
                assertNotNull(lang + " - " + question.getQuestionString(), gameService.getQuestionImage());
            }
            String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
            if (StringUtils.isNotBlank(questionToBeDisplayed) && !isNotAlphaLang(lang)) {
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
        } else if (gameService instanceof ImageClickGameService) {
            assertTrue(gameService.getAllAnswerOptions().size() == 4 || gameService.getAllAnswerOptions().size() == 10);
        } else if (gameService instanceof UniqueAnswersQuizGameService) {
            assertTrue(gameService.getAllAnswerOptions().size() > 2);
            assertTrue(((UniqueAnswersQuizGameService) gameService).getAnswers().size() == 1);
        }
        for (String answer : gameService.getAllAnswerOptions()) {
            assertTrue(StringUtils.isNotBlank(answer));
            assertTrue(lang + " - " + answer, removeDiacritics(answer).matches(getAnswersRegex()));
        }
    }

    public boolean isNotAlphaLang(String lang) {
        return Arrays.asList(Language.el, Language.ja, Language.hi, Language.ko, Language.ru, Language.uk, Language.th, Language.zh).contains(Language.valueOf(lang));
    }

    public String getQuestionsRegex() {
        return "^[a-zA-Z0-9, \\-\\'\\?\\.\\/\\,\\(\\)\\\"\\%]*$";
    }

    public String getAnswersRegex() {
        return "^[a-zA-Z0-9#, \\-\\'\\/\\.\\;\\(\\)\\,\\\"\\%]*$";
    }

    protected String removeDiacritics(String string) {
        return string.replaceAll("[^\\p{ASCII}]", "");
    }

    protected abstract void testAllQuestions() throws Exception;

    protected abstract AppInfoService getAppInfoService();


    @Before
    public void setup() throws Exception {
        createMocks();
        QuizGame.getInstance().getAssetManager().finishLoading();
    }

    private void createMocks() throws Exception {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(this, conf);
        Gdx.gl = PowerMockito.mock(GL20.class);
        Gdx.gl20 = PowerMockito.mock(GL20.class);

        ShaderProgram shaderProgram = Mockito.mock(ShaderProgram.class);
        PowerMockito.when(shaderProgram.isCompiled()).thenReturn(true);
        PowerMockito.mockStatic(SpriteBatch.class);
        PowerMockito.when(SpriteBatch.createDefaultShader()).thenReturn(shaderProgram);

        Gdx.graphics = PowerMockito.mock(Graphics.class);
        PowerMockito.when(Gdx.graphics.getHeight()).thenReturn(853);
        PowerMockito.when(Gdx.graphics.getWidth()).thenReturn(480);

        Preferences preferences = Mockito.mock(Preferences.class);
        PreferencesService preferencesService = Mockito.mock(PreferencesService.class);
        PowerMockito.when(preferencesService.getPreferences()).thenReturn(preferences);
        PowerMockito.whenNew(PreferencesService.class).withAnyArguments().thenReturn(preferencesService);

        QuizGame game = new QuizGame(getAppInfoService()) {
            @Override
            protected LoginService createLoginService() {
                return new DefaultLoginService();
            }
        };
        game.create();

        QuizGame.getInstance().setScreen(null);
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }
}
