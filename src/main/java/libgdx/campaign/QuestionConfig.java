package libgdx.campaign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import libgdx.utils.EnumUtils;

public class QuestionConfig {

    private List<String> l = new ArrayList<>();
    private List<String> c = new ArrayList<>();
    private int a;
    private int h;

    public QuestionConfig(int amountOfQuestions) {
        this(new ArrayList<QuestionDifficulty>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum()))),
                new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))),
                amountOfQuestions);
    }

    public QuestionConfig(QuestionCategory questionCategory) {
        this(Collections.singletonList(questionCategory));
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty) {
        this(Collections.singletonList(questionDifficulty), new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))));
    }

    public QuestionConfig(QuestionCategory questionCategory, int amountOfQuestions) {
        this(Collections.singletonList(questionCategory), amountOfQuestions);
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty, int amountOfQuestions) {
        this(Collections.singletonList(questionDifficulty), new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))), amountOfQuestions);
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        this(Collections.singletonList(questionDifficulty), Collections.singletonList(questionCategory));
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory, int amount) {
        this(Collections.singletonList(questionDifficulty), Collections.singletonList(questionCategory), amount);
    }

    public QuestionConfig(List<QuestionCategory> questionCategory) {
        this(questionCategory, 1);
    }

    public QuestionConfig(List<QuestionCategory> questionCategory, int amountOfQuestions) {
        this(new ArrayList<QuestionDifficulty>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum()))), questionCategory, amountOfQuestions);
    }

    public QuestionConfig(List<QuestionDifficulty> questionDifficulty, List<QuestionCategory> questionCategory) {
        this(questionDifficulty, questionCategory, 1);
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty, List<QuestionCategory> questionCategory, int amount) {
        this(Collections.singletonList(questionDifficulty), questionCategory, amount);
    }

    private QuestionConfig(List<QuestionDifficulty> questionDifficulty, List<QuestionCategory> questionCategory, int amount) {
        for (QuestionDifficulty item : questionDifficulty) {
            this.l.add(item.name());
        }
        for (QuestionCategory item : questionCategory) {
            this.c.add(item.name());
        }
        this.a = amount;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setH(int h) {
        this.h = h;
    }

    public QuestionConfig(List<String> l, List<String> c, int a, int h) {
        this.l = l;
        this.c = c;
        this.a = a;
        this.h = h;
    }

    public List<String> getQuestionDifficultyStringList() {
        return l;
    }

    public List<String> getQuestionCategoryStringList() {
        return c;
    }

    public int getAmount() {
        return a;
    }

    public int getAmountHints() {
        return h;
    }

    public RandomCategoryAndDifficulty getRandomCategoryAndDifficulty() {
        QuestionDifficulty randomQuestionDifficulty = getRandomQuestionDifficulty();
        List<QuestionCategory> categories = CampaignGame.getInstance().getSubGameDependencyManager().getQuestionConfigFileHandler().getQuestionCategoriesForDifficulty(randomQuestionDifficulty);
        Collections.shuffle(categories);
        for (QuestionCategory category : new ArrayList<>(categories)) {
            if (!c.contains(category.name())) {
                categories.remove(category);
            }
        }
        return new RandomCategoryAndDifficulty(categories.get(0), randomQuestionDifficulty);
    }

    private QuestionDifficulty getRandomQuestionDifficulty() {
        ArrayList<String> list = new ArrayList<String>(l);
        Collections.shuffle(list);
        return (QuestionDifficulty) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum(), list.get(0));
    }

    private QuestionCategory getRandomQuestionCategory() {
        ArrayList<String> list = new ArrayList<String>(c);
        Collections.shuffle(list);
        return (QuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), list.get(0));
    }

    public int getAmountOfQuestions() {
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionConfig that = (QuestionConfig) o;
        return a == that.a &&
                l.equals(that.l) &&
                c.equals(that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, c, a);
    }
}
