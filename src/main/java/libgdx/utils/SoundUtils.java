package libgdx.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.MusicIconButtonBuilder;
import libgdx.controls.button.builders.SoundIconButtonBuilder;
import libgdx.game.Game;
import libgdx.preferences.SettingsService;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class SoundUtils {

    private static Sound getSound(Res resource) {
        return Game.getInstance().getAssetManager().get(resource.getPath(), Sound.class);
    }

    private static Music getMusic(Res resource) {
        return Game.getInstance().getAssetManager().get(resource.getPath(), Music.class);
    }

    public static void playSound(Res resource) {
        if (new SettingsService().isSoundOn()) {
            getSound(resource).play();
        }
    }

    public static void playMusic(Res res) {
        Music music = getMusic(res);
        if (new SettingsService().isMusicOn()) {
            music.setLooping(true);
            music.setVolume(0.03f);
            music.play();
        } else {
            music.stop();
        }
    }

    public static void addSoundTable(AbstractScreen screen, Res music, float y) {
        Table table = createSoundMusicButtonsTable(music);
        float x = ScreenDimensionsManager.getScreenWidth() - getMarginBetweenMusicAndSoundButtons(music) * 3 - MainButtonSize.SOUND_BUTTON.getWidth() / 1.5f;
        table.setPosition(x, y);
        screen.addActor(table);
    }

    public static Table createSoundMusicButtonsTable(Res music) {
        Table table = new Table();
        if (music != null) {
            MyButton musicButton = new MusicIconButtonBuilder().createMusicButton(music);
            table.add(musicButton).height(musicButton.getHeight()).width(musicButton.getWidth());
            SoundUtils.playMusic(music);
        }
        MyButton soundButton = new SoundIconButtonBuilder().createSoundButton();
        table.add(soundButton).width(soundButton.getWidth()).height(soundButton.getHeight()).padLeft(getMarginBetweenMusicAndSoundButtons(music));
        return table;
    }

    private static float getMarginBetweenMusicAndSoundButtons(Res music) {
        return music == null ? 0 : MainDimen.horizontal_general_margin.getDimen() * 5f;
    }

    public static void addSoundTable(AbstractScreen screen, Res music) {
        addSoundTable(screen, music, ScreenDimensionsManager.getScreenHeight() - MainButtonSize.SOUND_BUTTON.getHeight());
    }
}
