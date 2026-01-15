package libgdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import libgdx.constants.Language;
import libgdx.controls.MyTextField;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.controls.textfield.MyTextFieldBuilder;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.preferences.InAppPurchasesPreferencesService;
import libgdx.resources.dimen.MainDimen;


public class Utils {

    public static boolean containsWordWithLength(String text, int minWordLength) {
        String[] words = text.split(" ");
        for (String w : words) {
            if (w.length() >= minWordLength) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> getListFromJsonString(String jsonListString, Class<T> clazz) {
        List<T> lst = new ArrayList<T>();
        if (StringUtils.isNotBlank(jsonListString)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(jsonListString).getAsJsonArray();

            for (final JsonElement json : array) {
                T entity = new Gson().fromJson(json, clazz);
                lst.add(entity);
            }
        }
        return lst;
    }

    public static boolean isValidExtraContent() {
//        return true;
        return Game.getInstance().getAppInfoService().isProVersion() || new InAppPurchasesPreferencesService().isPurchased(Game.getInstance().getSubGameDependencyManager().getExtraContentProductId());
    }

    public static RunnableAction createRunnableAction(Runnable runnable) {
        RunnableAction action = new RunnableAction();
        action.setRunnable(runnable);
        return action;
    }

    public static void fadeInActor(final Actor actor, float duration) {
        actor.addAction(Actions.sequence(Actions.fadeOut(0f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                actor.setVisible(true);
            }
        }), Actions.fadeIn(duration)));
    }

    public static RunnableAction createRemoveActorAction(final Actor actor) {
        return createRunnableAction(new Runnable() {
            @Override
            public void run() {
                actor.remove();
            }
        });
    }

    public static String getStringLetters(List<String> allStrings) {
        Set<String> extraChars = new HashSet<>();
        for (String q : allStrings) {
            extraChars.addAll(getStringLetters(q));
        }
        return StringUtils.join(extraChars);
    }

    public static Set<String> getStringLetters(String v) {
        char[] chars = v.toCharArray();
        Set<String> set = new HashSet<>();
        for (char c : chars) {
            set.add(Character.toString(c));
        }
        return set;
    }

    public static JSONObject getJsonObjectFromString(String jsonString) {
        JSONObject existingGameInfoJson = new JSONObject();
        try {
            if (!StringUtils.isBlank(jsonString)) {
                existingGameInfoJson = new JSONObject(jsonString);
            }
        } catch (JSONException e) {
            return null;
        }
        return existingGameInfoJson;
    }

    public static float getValueForPercent(float val, float percent) {
        return val * (percent / 100f);
    }

    public static Object getLastElement(final Collection c) {
        Object lastElement = null;
        if (c != null && !c.isEmpty()) {
            final Iterator itr = c.iterator();
            lastElement = itr.next();
            while (itr.hasNext()) {
                lastElement = itr.next();
            }
        }
        return lastElement;
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "xxx";
            case da:
                return "xxx";
            case de:
                return "xxx";
            case el:
                return "xxx";
            case en:
                return "xxx";
            case es:
                return "xxx";
            case fi:
                return "xxx";
            case fr:
                return "xxx";
            case hi:
                return "xxx";
            case hr:
                return "xxx";
            case hu:
                return "xxx";
            case id:
                return "xxx";
            case it:
                return "xxx";
            case ja:
                return "xxx";
            case ko:
                return "xxx";
            case ms:
                return "xxx";
            case nl:
                return "xxx";
            case nb:
                return "xxx";
            case pl:
                return "xxx";
            case pt:
                return "xxx";
            case ro:
                return "xxx";
            case ru:
                return "xxx";
            case sk:
                return "xxx";
            case sv:
                return "xxx";
            case th:
                return "xxx";
            case tr:
                return "xxx";
            case uk:
                return "xxx";
            case vi:
                return "xxx";
            case zh:
                return "xxx";
        }
        return null;
    }

    public static void createChangeLangPopup() {
        createChangeLangPopup(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public static void createChangeLangPopup(Runnable runAfterChange) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            MyPopup popup = new MyPopup(Game.getInstance().getAbstractScreen()) {
                @Override
                protected void addButtons() {
                    final MyTextField myTextField = new MyTextFieldBuilder().build();
                    getButtonTable().add(myTextField).row();
                    MyButton changeLangBtn = new ButtonBuilder().setWrappedText("Change Lang to", MainDimen.horizontal_general_margin.getDimen() * 10)
                            .setDefaultButton()
                            .build();
                    changeLangBtn.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (EnumUtils.isValidEnum(Language.class, myTextField.getTextField().getText())) {
                                Game.getInstance().setNewContext(cloneAppInfoService(Game.getInstance().getAppInfoService(), myTextField.getTextField().getText()));
                                runAfterChange.run();
                            }
                        }
                    });
                    addButton(changeLangBtn);
                }

                @Override
                protected String getLabelText() {
                    return "";
                }
            };
            popup.addToPopupManager();
        }
    }

    private static AppInfoService cloneAppInfoService(final AppInfoService currentAppInfoService, final String newLang) {
        return new AppInfoService() {
            @Override
            public String getAppName() {
                return currentAppInfoService.getAppName();
            }

            @Override
            public String getProVersionStoreAppId() {
                return currentAppInfoService.getProVersionStoreAppId();
            }

            @Override
            public void removeAds() {
                currentAppInfoService.removeAds();
            }

            @Override
            public String getStoreAppId() {
                return currentAppInfoService.getStoreAppId();
            }

            @Override
            public boolean isScreenShotMode() {
                return currentAppInfoService.isScreenShotMode();
            }

            @Override
            public void showRewardedVideoAd() {
                currentAppInfoService.isScreenShotMode();
            }

            @Override
            public String getGameIdPrefix() {
                return currentAppInfoService.getGameIdPrefix();
            }

            @Override
            public void showPopupAd(Runnable afterClose) {
                currentAppInfoService.showPopupAd(afterClose);
            }

            @Override
            public String getImplementationGameResourcesFolder() {
                return currentAppInfoService.getImplementationGameResourcesFolder();
            }

            @Override
            public String getLanguage() {
                return newLang;
            }

            @Override
            public String getMainResourcesFolder() {
                return currentAppInfoService.getMainResourcesFolder();
            }

            @Override
            public String getResourcesFolder() {
                return currentAppInfoService.getResourcesFolder();
            }

            @Override
            public boolean isPortraitMode() {
                return currentAppInfoService.isPortraitMode();
            }

            @Override
            public boolean isProVersion() {
                return currentAppInfoService.isProVersion();
            }

            @Override
            public float gameScreenTopMargin() {
                return currentAppInfoService.gameScreenTopMargin();
            }
        };
    }

}
